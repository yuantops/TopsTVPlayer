package com.yuantops.tvplayer.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.yuantops.tvplayer.util.SocketMessageHandler;

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
	private final static int BLOCK_TIME_SOCKET = 1000;
	private final static int RETRY_TIME = 3;

	private SocketChannel socketChannel = null;
	private Selector selector = null;
	private ByteBuffer readBuffer = null;
	private ByteBuffer writeBuffer = null;

	public SocketClient(String ip, int port) {
		try {
			this.selector = Selector.open();
			this.socketChannel = SocketChannel.open();

			int count = 0;
			do {
				this.socketChannel.connect(new InetSocketAddress(ip, port));
				if (this.socketChannel.isConnected()) {
					count = RETRY_TIME;
				} else {
					Thread.sleep(SLEEP_TIME_SOCKET);
					count++;
				}
			} while (!this.socketChannel.isConnected() && count < RETRY_TIME);

			if (this.isClientAvailable()) {
				this.socketChannel.configureBlocking(false);
				this.socketChannel.register(selector, SelectionKey.OP_READ);
			} else {
				Log.d(TAG, "new Socket Client failed");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "socket establish error");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "Thread sleep error");
			e.printStackTrace();
		}
	}

	/**
	 * 向socket写字符串
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		if (!isClientAvailable()) {
			return;
		}
		new WriteToSocketThread(message).start();
	}

	/**
	 * 从socket读取数据，交给传入的handler处理
	 * 
	 * @param handler
	 */
	public void readMessage(SocketMessageHandler handler) {
		// TODO
	}

	/**
	 * 长期监听socket，从中读入数据
	 * @author yuan (Email: yuan.tops@gmail.com) *
	 * @date Jan 14, 2015 
	 */
	private class ReadSocketThread extends Thread {
		private SocketMessageHandler mHandler = null;

		public ReadSocketThread(SocketMessageHandler handler) {
			this.mHandler = handler;
		}

		@Override
		public void run() {
			while (true) {
				if (!SocketClient.this.isClientAvailable()) {
					Log.d(TAG,
							"+++++++++socket not prepared when reading from socket ");
					return;
				}
				try {
					if (SocketClient.this.selector.select(BLOCK_TIME_SOCKET) > 0) {
						Set<SelectionKey> selectedKeys = selector
								.selectedKeys();
						Iterator<SelectionKey> keyIterator = selectedKeys
								.iterator();
						while (keyIterator.hasNext()) {
							SelectionKey key = keyIterator.next();
							if (key.isValid() && key.isReadable()) {
								// a channel is ready for reading
								int bodyBytesLength = 0;
								BufferedReader socketReader = null;
								try {
									SocketChannel channel = (SocketChannel) key.channel();
									socketReader = new BufferedReader(new InputStreamReader(channel.socket().getInputStream()));
									
									StringBuilder dlnaStrBuidler = new StringBuilder();
									String lineBuf = socketReader.readLine();
									if (lineBuf == null){
										return;
									} 
									while (!lineBuf.equals("")) {
										dlnaStrBuidler.append(lineBuf+"\r\n");
										if (lineBuf.contains("length")) {
											bodyBytesLength = Integer.parseInt(lineBuf.split(":")[1]);
										}
										lineBuf = socketReader.readLine();
									}
									dlnaStrBuidler.append("\r\n");
									
									if (bodyBytesLength > 0) {
										readBuffer = ByteBuffer.allocate(bodyBytesLength);
										channel.read(readBuffer);
										dlnaStrBuidler.append(((ByteBuffer)(readBuffer.flip())).asCharBuffer().toString());
										readBuffer.clear();
									}	
									
									Log.d(TAG+"dlna Message String", dlnaStrBuidler.toString());
									
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									if (socketReader != null) {
										try {
											socketReader.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} 
							}
							keyIterator.remove();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 向socket中写入数据的线程
	 * 
	 * @author yuan (Email: yuan.tops@gmail.com) *
	 * @date Jan 14, 2015
	 */
	private class WriteToSocketThread extends Thread {
		private String MsgToSend;

		public WriteToSocketThread(String Msg) {
			this.MsgToSend = Msg;
		}

		@Override
		public void run() {
			if (!SocketClient.this.isClientAvailable()) {
				return;
			}
			try {
				writeBuffer = ByteBuffer.wrap(MsgToSend.getBytes("UTF-8"));
				synchronized (socketChannel) {
					socketChannel.write(writeBuffer);
				}
			} catch (UnsupportedEncodingException e) {
				Log.d(TAG, "++++++not supported encoding");
				e.printStackTrace();
			} catch (IOException e) {
				Log.d(TAG, "++++++IO error when writing to socket");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 当前的socketClient是否成功可用
	 * 
	 * @return
	 */
	private boolean isClientAvailable() {
		return this.socketChannel.isConnected() && this.selector.isOpen();
	}
}
