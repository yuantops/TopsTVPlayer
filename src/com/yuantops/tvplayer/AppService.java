package com.yuantops.tvplayer;

import java.io.UnsupportedEncodingException;

import com.yuantops.tvplayer.api.SocketClient;
import com.yuantops.tvplayer.bean.DLNABody;
import com.yuantops.tvplayer.bean.DLNAHead;
import com.yuantops.tvplayer.bean.DLNAMessage;
import com.yuantops.tvplayer.bean.NetworkConstants;
import com.yuantops.tvplayer.bean.RoutingLabel;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
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
	
	private static final int TRY_TIME_LIMIT = 3;
	
	private MyBinder myBinder;	
	private SocketClient socketClient;
	private AppContext appContext;
	
	@Override
	public void onCreate() {

		Log.v(TAG, "onCreate()");
		// The service is being created
		super.onCreate();
		myBinder = new MyBinder();
		appContext = (AppContext) this.getApplication();
		
		//初始化socketClient，int()方法会新开后台线程监听socket连接
		socketClient = new SocketClient(appContext.getSocketServerIP(), NetworkConstants.DLNA_PROXY_PORT, this);
		socketClient.init();
		
		for(int count = 0; count < TRY_TIME_LIMIT; count++) {
			if (socketClient.isPrepared()) {
				register(socketClient);
				break;
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Log.d(TAG, "cannot connect socket server...");
	}
	
	/**
	 * 向SockerServer发送注册(NOTIFY)信息
	 * @param socketClient2
	 */
	private void register(SocketClient socketClient2) {
		RoutingLabel orginLabel = new RoutingLabel("Agent", "0",
				appContext.getClientIP_hex(), appContext.getClientIP(), String.valueOf(NetworkConstants.LOCAL_DEFAULT_CONN_PORT), "android_client");
		RoutingLabel desitLabel = new RoutingLabel("Proxy", "0",
				"00000000", appContext.getSocketServerIP(), String.valueOf(NetworkConstants.DLNA_PROXY_PORT), "");
		
		DLNABody nofityBody = new DLNABody();
		DLNAHead nofityHead = null;
		nofityHead = new DLNAHead("NOTIFY", orginLabel, desitLabel,
				orginLabel, desitLabel, "0");		
		DLNAMessage nofityMessage = new DLNAMessage(nofityHead,
				nofityBody);
		socketClient2.sendMessage(nofityMessage.printDLNAMessage());		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onBind()");
		return myBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// All clients have unbound with unbindService()
		Log.v(TAG, "onUnBind()");
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
		/**
		 * 发送字符串信息
		 * @param msg
		 * @return
		 */
		public int sendMessage(String msg) {
			socketClient.sendMessage(msg);
			return 0;
		} 
		
		/**
		 * 根据报文的转发目的地、正文生成报文，并发送该字符串
		 * @param destIP
		 * @param msgBody
		 * @return
		 */
		public int sendMessage(String destIP, DLNABody msgBody) {
			RoutingLabel label1 = new RoutingLabel("Control", "0",
					appContext.getClientIP_hex(), appContext.getClientIP(), String.valueOf(NetworkConstants.LOCAL_DEFAULT_CONN_PORT), "android_client");
			RoutingLabel label2 = new RoutingLabel("Control", "0",
					"00000000", "239.255.255.250", "1900", "");
			RoutingLabel label3 = new RoutingLabel("Agent", "0",
					appContext.getClientIP_hex(), appContext.getClientIP(), String.valueOf(NetworkConstants.LOCAL_DEFAULT_CONN_PORT), "android_client");
			RoutingLabel label4 = new RoutingLabel("Agent", "0",
					"00000000", destIP, "1901", "");
			
			DLNAHead head = null;

			try {
				head = new DLNAHead("FORWARD", label1, label2, label3,
						label4, String.valueOf(msgBody.printDLNABody()
								.getBytes("UTF-8").length));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DLNAMessage commonMessage = new DLNAMessage(head, msgBody);
			socketClient.sendMessage(commonMessage.printDLNAMessage());
			return 0;
		}
	}	
	
}
