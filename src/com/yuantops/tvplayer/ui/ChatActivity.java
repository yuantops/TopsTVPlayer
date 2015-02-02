package com.yuantops.tvplayer.ui;

import com.yuantops.tvplayer.AppService;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.AppService.MyBinder;
import com.yuantops.tvplayer.bean.DLNABody;
import com.yuantops.tvplayer.util.SocketMsgDispatcher;
import com.yuantops.tvplayer.util.StringUtils;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChatActivity extends Activity {
	private static final String TAG = ChatActivity.class.getSimpleName();
	
	private EditText chatbox;
	private EditText chat_input;
	private Button msg_send;
	
	private MyBinder mBinder;	
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			mBinder = (MyBinder) arg1;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub			
		}		
	};

	private IntentFilter chatBroadcastIntentFilter = new IntentFilter();	
	private BroadcastReceiver inboundChatRecvr = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			DLNABody msgDLNA = (DLNABody) intent.getSerializableExtra("Params");
			Message msg = new Message();
			msg.what = 1;
			msg.obj = msgDLNA;
			uiUpdater.sendMessage(msg);
		}		
	};
	
	private Handler uiUpdater = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				DLNABody content = (DLNABody) msg.obj;
				chatbox.append(content.printDLNABody());
				chat_input.setText("");
				break;
			case 1:
				DLNABody content1 = (DLNABody) msg.obj;
				chatbox.append(content1.printDLNABody());
				break;
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_chat);
		
		chatbox = (EditText) findViewById(R.id.chatbox);
		chat_input = (EditText) findViewById(R.id.chatbox_input);
		msg_send = (Button) findViewById(R.id.chatbox_send);		
		
		Intent serviceIntent = new Intent(this, AppService.class);
		bindService(serviceIntent, conn, Service.BIND_AUTO_CREATE);

		chatBroadcastIntentFilter.addAction(SocketMsgDispatcher.CHAT_BROADCAST);
		registerReceiver(inboundChatRecvr, chatBroadcastIntentFilter);
		
		msg_send.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {				
				String inputString = chat_input.getText().toString();
				if (!StringUtils.isEmpty(inputString)) {
					DLNABody msgToSend = new DLNABody();
					msgToSend.addRecord("ACTION", "CHAT");
					msgToSend.addRecord("random", inputString);
					mBinder.sendMessage("192.168.1.106", msgToSend);
					mBinder.sendMessage("192.168.1.100", msgToSend);
					
					Message uiUpdateNotice = new Message();
					uiUpdateNotice.what = 0;
					uiUpdateNotice.obj = msgToSend;
					uiUpdater.sendMessage(uiUpdateNotice);
				}
			}			
		});
	}
	
	public void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}
}
