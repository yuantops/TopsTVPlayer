package com.yuantops.tvplayer;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.yuantops.tvplayer.api.SocketClient;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * 全局运行的后台服务,维持与DLNA server的socket通信
 * 
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Jan 13, 2015
 */
public class AppService extends Service {
	private static final String TAG = AppService.class.getSimpleName();
	
	private MyBinder myBinder = null;
	
	private SocketClient socketClient = null;
	private SocketChannel socketClientChannel = null;
	private Selector selector = null;

	private SocketMsgHandler socketMsgHandler = null;
	
	@Override
	public void onCreate() {
		// The service is being created
		super.onCreate();
		myBinder = new MyBinder();
		socketClientChannel = establishSocketConnection();
		selector = openAndRegisterSelector();
		Log.v(TAG, "onCreate()");
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return myBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// All clients have unbound with unbindService()
		return false;
	}

	@Override
	public void onRebind(Intent intent) {
		// A client is binding to the service with bindService(),
		// after onUnbind() has already been called
	}

	@Override
	public void onDestroy() {
		// The service is no longer used and is being destroyed
	}
	
	class MyBinder extends Binder {
		public int sendMessage(String msg) {
			// TODO 启动线程发送信息
			return 0;
		}
	}
	
	private SocketChannel establishSocketConnection(){
		SocketChannel channel = null;
		
	}
	
	/**
	 * 处理传入的DLNA报文，并根据报文类型，做下一步处理
	 * @author yuan (Email: yuan.tops@gmail.com) *
	 * @date Jan 13, 2015 
	 */
	public static class SocketMsgHandler extends Handler{
		//TODO
		//根据收到的字符串还原出DLNA报文，生成DLNA报文对象实体
		//根据报文正文类型，做出下一步处置
	}
	
	/**
	 * 从socket中接收数据，处理成字符串形式的DLNA报文
	 * @author yuan (Email: yuan.tops@gmail.com) *
	 * @date Jan 13, 2015 
	 */
	class ReadMsgFromSocketThread extends Thread{
		
	}
	
	/**
	 * 向socket中发送数据
	 * @author yuan (Email: yuan.tops@gmail.com) *
	 * @date Jan 13, 2015 
	 */
	class SendMsgToSocketThread extends Thread{
		
	}

}
