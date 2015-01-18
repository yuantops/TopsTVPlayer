package com.yuantops.tvplayer.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yuantops.tvplayer.bean.DLNABody;

/**
 * 处理传入的DLNA报文，根据消息类型向外分发broadcast
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Jan 13, 2015 
 */
public class SocketMsgDispatcher {
	private static String TAG = SocketMsgDispatcher.class.getSimpleName();
	
	public static final String LOGIN_BROADCAST = "com.yuantops.tvplayer.LOGIN_BROADCAST";
	public static final String CHAT_BROADCAST = "com.yuantops.tvplayer.CHAT_BROADCAST";
	public static final String PLAY_BROADCAST = "com.yuantops.tvplayer.PLAY_BROADCAST";
	
	//根据收到的字符串还原出DLNA报文，生成DLNA报文对象实体
	//根据报文正文类型，做出下一步处置
	
	public static void processMsg (Context mContext, String dlnaMessage) {
		if (dlnaMessage.contains("NOTIFY") || dlnaMessage.contains("M-SEARCH")) {
			return;
		}
		
		DLNABody body  = retrieveMsgBody(dlnaMessage);
		Intent broadcastIntent = null;
		
		if (body.getValue("ACTION").equals("LOGIN")) {
			broadcastIntent = new Intent(LOGIN_BROADCAST);
		} else if (body.getValue("ACTION").equals("CHAT")) {
			broadcastIntent = new Intent(CHAT_BROADCAST);			
		} else if (body.getValue("ACTION").equals("PLAY_BROADCAST")) {
			broadcastIntent = new Intent(PLAY_BROADCAST);
		}
		
		broadcastIntent.putExtra("Params", body);
		mContext.sendBroadcast(broadcastIntent);		
	}
		
	/**
	 * 由DLNA报文生成DLNABody对象
	 * @param message
	 * @return
	 */
	private static DLNABody retrieveMsgBody(String message) {		
		String[] lines = message.split("\\r\\n\\r\\n");
		if (StringUtils.isEmpty(lines[1])) {
			Log.d(TAG, "Exception caught since empty dlna body retrieved");
			return null;
		}
		
		String dlnaBodyString = lines[1];
		String[] bodyLines = dlnaBodyString.split("\\r\\n");
		DLNABody body = new DLNABody();
		String[] record = null;
		for (String str:bodyLines) {
			record = str.split(":");
			body.addRecord(record[0], record[1]);
		}
		
		Log.v(TAG, "print dlna body\n"+body.printDLNABody());
		
		return body;		
	}
	
	
	
}
