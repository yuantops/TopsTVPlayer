package com.yuantops.tvplayer.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * 处理与SocketServer通信
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Jan 13, 2015 
 */
public class SocketClient {
	private static final String TAG =  SocketClient.class.getSimpleName();
	
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;
	
	private SocketChannel socketChannel = null;
	private Selector selector = null;
	
	public SocketClient(String ip, int port){
		try {
			this.socketChannel = SocketChannel.open();
			this.socketChannel.connect(new InetSocketAddress(ip, port));
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
