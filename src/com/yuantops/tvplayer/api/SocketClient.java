package com.yuantops.tvplayer.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

import com.yuantops.tvplayer.util.SocketMsgDispatcher;

import android.content.Context;
import android.util.Log;

/**
 * 与SocketServer建立socket连接的客户端
 * 
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Jan 13, 2015
 */
public class SocketClient {
	private static final String TAG = SocketClient.class.getSimpleName();
	
	private final static int SLEEP_TIME_SOCKET = 2000;
	private final static int RETRY_TIME = 3;
	private final static int READ_WRITE_BUFFER_SIZE = 512;

	private String ip;
	private int port;
	
	private Context mContext;
	private boolean runFlag ;//监听开关
	private SocketChannel socketChannel;
	private Selector selector;
	private ByteBuffer readBuffer;
	private ByteBuffer writeBuffer;	

		
	/*public SocketClient (String ip, int port, SocketMsgDispatcher mHandler) {
		this(ip, port);
		this.mHandler = mHandler;
	}*/
	
	public SocketClient (String ip, int port, Context context) {
		this(ip, port);
		this.mContext = context;
	}
	
	public SocketClient (String ip, int port) {
		this.ip = ip;
		this.port = port;
		
		this.runFlag = false;		
		this.readBuffer = ByteBuffer.allocate(READ_WRITE_BUFFER_SIZE);
		this.writeBuffer = ByteBuffer.allocate(READ_WRITE_BUFFER_SIZE);
	}
		
	/**
	 * 新开线程，完成：1.建立socket连接 2.开启监听循环
	 */
	public void init() {
		Log.v(TAG, "init() invoked");
		if (this.ip == null || this.port == 0) {
			Log.d(TAG, "init() returned due to null port/ip ");
			return ;
		}
		new InitThread().start();
	}	
	
	/**
	 * 对应init()的线程
	 * @author yuan (Email: yuan.tops@gmail.com)
	 * @date Jan 16, 2015 
	 */
	private class InitThread extends Thread {
		@Override
		public void run() {
			establishConnection();
			keepReadingSocket();
		}
	}	
	
	/**
	 * 新建selector,socketChannel；向selector注册socketChannel的可读监听
	 */
	private void establishConnection() {
		Log.v(TAG, "establishConnection() invoked");
		try {
			this.selector = Selector.open();
			this.socketChannel = SocketChannel.open();

			int attempt = 0;
			do {
				this.socketChannel.connect(new InetSocketAddress(ip, port));
				if (this.socketChannel.isConnected()) {
					attempt = RETRY_TIME;
				} else {
					Thread.sleep(SLEEP_TIME_SOCKET);
					attempt++;
				}
			} while (!this.socketChannel.isConnected() && attempt < RETRY_TIME);

			//将socketChannel 设为非阻塞模式，监听可读状态
			if (this.isPrepared()) {
				this.runFlag = true;
				this.socketChannel.configureBlocking(false);
				this.socketChannel.register(selector, SelectionKey.OP_READ);
				Log.d(TAG, "socket establishment success");
			} else {
				Log.d(TAG, "socket establishment failed after reaching attempt limit");
			}

		} catch (IOException e) {			
			Log.d(TAG, "socket establish error");
			e.printStackTrace();
		} catch (InterruptedException e) {
			Log.d(TAG, "Thread sleep error");
			e.printStackTrace();
		}	
		Log.v(TAG, "establishConnection() end");
	}
	
	
	/**
	 * 进入循环，监听socket，读取数据 
	 */
	private void keepReadingSocket() {
		Log.v(TAG, "keepReadingSocket() invoked");		
		
		while (runFlag) {
			
			Log.v(TAG, "start one select loop......");
			
			Set<SelectionKey> selectedKeys = null;
			try {
				Log.v(TAG, "select starts blocking...");
				selector.select();
				
				//selector.select()方法会堵塞，期间runFlag可能改变，因此需要再次检验
				if (!runFlag) {
					Log.v(TAG, "keepReading terminates after select()");
					return;
				}				
				Log.v(TAG, "select ends blocking...");
				selectedKeys = selector.selectedKeys();
				if (selectedKeys.size() == 0) { 
					Log.v(TAG, "no channel ready, exit loop");
					continue;	
				}
			} catch (IOException e1) {
				Log.d(TAG, "Exception caught when select channels");
				e1.printStackTrace();
			}					
			
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();	
			
			while (keyIterator.hasNext()) {
				Log.v(TAG, "selectedKeys size: " + selectedKeys.size());
				SelectionKey key = keyIterator.next();
				if (key.isReadable()) {
					Log.v(TAG, "socket channel readable selected");			        
			        try {		
			        	readBuffer.clear();
			        	((SocketChannel) key.channel()).read(readBuffer);				        	
			        } catch (IOException e) {
			        	System.out.println("Exception caught when reading socket");
			        	e.printStackTrace();
			        }
			        				        
			        readBuffer.flip();
			        if (readBuffer.remaining() != 0) {
			        	 Charset charset=Charset.forName("UTF-8");
					        CharsetDecoder decoder = charset.newDecoder();
					        CharBuffer charBuffer;
							try {
								charBuffer = decoder.decode(readBuffer);
								SocketMsgDispatcher.processMsg(mContext, charBuffer.toString());
								Log.v(TAG, "decoded string from socket>>>>>>\n" + charBuffer.toString());
							} catch (CharacterCodingException e) {
								Log.d(TAG, "Exception caught when decoding from bytes");
								e.printStackTrace();
							}
			        }   
			       	readBuffer.clear();			    
			    } 

				keyIterator.remove();
			}
		}
		Log.v(TAG, "keepReadingSocket() end");
	}
	
	/**
	 * 新开线程，向socket写字符串 
	 * @param message
	 */	
	public void sendMessage(String str) {
		new sendMessageThread(str).start();
	}
	
	/**
	 * 对应sendMessage的线程：向socket中发送字符串
	 * @author yuan (Email: yuan.tops@gmail.com) 
	 * @date Jan 16, 2015 
	 */
	private class sendMessageThread extends Thread {
		String msg;
		
		public sendMessageThread(String msg) {
			this.msg = msg;
		}
		
		@Override
		public void run() {
			writeSocket(msg);
		}
	}
	
	/**
	 * 向socket写字符串 
	 * @param message
	 */
	private void writeSocket(String str) {
		Log.v(TAG, "sendMessage() invoked");
		if (!isPrepared()) {
			Log.v(TAG, "sendMessage() returned due to channel unprepared");
			return;
		}	

		synchronized (this.writeBuffer) {
			try {
				writeBuffer.clear();
				writeBuffer.put(str.getBytes("UTF-8"));
				writeBuffer.flip();
				Log.v(TAG, "bytes of the message: "
						+ str.getBytes("UTF-8").length);
				Log.v(TAG, "bytes awaits to be sent in writeBuffer: "
						+ writeBuffer.remaining());
			} catch (UnsupportedEncodingException e) {
				Log.d(TAG, "Exception caught when getting bytes from string");
				e.printStackTrace();
			}
			try {
				this.socketChannel.write(writeBuffer);
				Log.v(TAG,
						"bytes left in writeBuffer: " + writeBuffer.remaining());
			} catch (IOException e) {
				Log.d(TAG, "Exception caught when writing data into socket");
				e.printStackTrace();
			}
		}
		Log.v(TAG, "sendMessage() end");
	}
			
	/**
	 * 暂停监听socket，不能写入/读出数据
	 */
	public void pauseSocket() {
		Log.v(TAG, "pauseSocket() invoked...");
		this.runFlag = false;
	}
	
	/**
	 * 新开线程，恢复监听socket
	 */
	public void resumeSocket() {
		this.runFlag = true;
		new Thread(){
			@Override
			public void run() {
				keepReadingSocket();
			}
		}.start();
	}
	
	/**
	 * 彻底销毁socket连接
	 */
	public void destroy() {
		Log.v(TAG, "destroy() invoked");
		this.runFlag = false;
		try {
			if (selector != null) {
				selector.close();
			}
			if (socketChannel != null ) {
				socketChannel.close();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	/**
	 * socketClient对象是否可用 
	 * @return
	 */
	public boolean isPrepared() {
		if (this.socketChannel == null || this.selector == null) {
			return false;
		} else {
			return this.socketChannel.isConnected() && this.selector.isOpen();
		}
	}	
}