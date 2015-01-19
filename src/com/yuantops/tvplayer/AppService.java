package com.yuantops.tvplayer;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.yuantops.tvplayer.api.SocketClient;
import com.yuantops.tvplayer.bean.NetworkConstants;
import com.yuantops.tvplayer.util.SocketMsgDispatcher;

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
	
	private MyBinder myBinder;	
	private SocketClient socketClient;
	
	@Override
	public void onCreate() {
		// The service is being created
		super.onCreate();
		myBinder = new MyBinder();
		
		//初始化socketClient，int()方法会新开后台线程监听socket连接
		socketClient = new SocketClient(((AppContext)this.getApplication()).getServerIP(), NetworkConstants.DLNA_PROXY_PORT, this);
		socketClient.init();
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
		//销毁socketClient客户端
		socketClient.destroy();
	}
	
	class MyBinder extends Binder {
		public int sendMessage(String msg) {
			socketClient.sendMessage(msg);
			return 0;
		}
	}	
	
}
