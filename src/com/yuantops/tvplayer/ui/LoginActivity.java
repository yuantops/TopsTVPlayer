package com.yuantops.tvplayer.ui;

import com.yuantops.tvplayer.AppContext;
import com.yuantops.tvplayer.AppManager;
import com.yuantops.tvplayer.AppService;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.api.HttpClientAPI;
import com.yuantops.tvplayer.bean.DLNABody;
import com.yuantops.tvplayer.bean.NetworkConstants;
import com.yuantops.tvplayer.util.CyptoUtils;
import com.yuantops.tvplayer.util.SocketMsgDispatcher;
import com.yuantops.tvplayer.util.StringUtils;
import com.yuantops.tvplayer.util.UIRobot;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity implements OnClickListener {
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	private boolean mBound = false;//是否绑定service
	private boolean mRegistered = false;//是否注册广播接收器
	
	private EditText acountEditText;
	private EditText pwdEditText;
	private TextView ipTextView;
	private TextView deviceTextView;
	private CheckBox checkbox;
	private Button loginButton;
	private Button registerButton;	
	private EditText serverIPEditText;
	private Button serverIPRefresh;
	private ImageView loginQRCode;// 登录二维码，主要包含当前设备的IP地址
	
	private AppContext globalAppContext = (AppContext) getApplication();
	
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.v(TAG, "onServiceDisconnected()");
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v(TAG, "onServiceConnected()");
		}
	};
	
	private IntentFilter intentFilter = new IntentFilter(SocketMsgDispatcher.LOGIN_BROADCAST);
	
	private BroadcastReceiver loginBrdReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			DLNABody params = (DLNABody) arg1.getParcelableExtra("Params");
			String loginAccount = params.getValue("LoginAccount");
			String loginRecordId = params.getValue("LoginId");
			
			globalAppContext.setLoginInfo(loginAccount, loginRecordId);
			UIRobot.gotoHomePage(LoginActivity.this);
		}		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		AppManager.getInstance().addActivity(this);
		
		globalAppContext.initIPAddress();
		initViewComponents();
		
		//取出保存的服务器IP，如果不为空，用它给server_ip变量赋值
		if(!StringUtils.isEmpty(globalAppContext.getLoginInfoParams("server_ip"))) {
			NetworkConstants.server_ip = globalAppContext.getLoginInfoParams("server_ip");
		}
		
		//如果网络可用，1) 以绑定方式启动service 2)注册处理登录的broadcast receiver
		if(globalAppContext.isNetworkConnected()) {
			Intent intent = new Intent(this, AppService.class);
	        bindService(intent, conn, Context.BIND_AUTO_CREATE);
	        mBound = true;
	        
	        registerReceiver(loginBrdReceiver, intentFilter);
	        mRegistered = true;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mBound) {
			unbindService(conn);
	        mBound = false;
		}
		if(mRegistered) {
			this.unregisterReceiver(loginBrdReceiver);
			 mRegistered = false;
		}
	}
	
	/**
	 * 初始化layout组件
	 */
	private void initViewComponents () {
		acountEditText = (EditText) findViewById(R.id.edtuser);
		pwdEditText = (EditText) findViewById(R.id.edtpsd);
		ipTextView = (TextView) findViewById(R.id.edtip);
		deviceTextView = (TextView) findViewById(R.id.edtdevicetype);
		loginButton = (Button) findViewById(R.id.loginacccount);
		registerButton = (Button) findViewById(R.id.loginregedit);
		checkbox = (CheckBox) findViewById(R.id.checkbox);		
		serverIPEditText = (EditText) findViewById(R.id.server_ip);
		serverIPRefresh = (Button) findViewById(R.id.save_server_ip);		
		
		//添加OnClickListener
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		serverIPRefresh.setOnClickListener(this);

		//取出保存在机器上的 本机IP,设备类型,服务器IP地址
		deviceTextView.setText(globalAppContext.getDeviceType());
		ipTextView.setText(globalAppContext.getLocalIP());
		serverIPEditText.setText(globalAppContext.getLoginInfoParams("server_ip"));
		
		//如果上次登录时选择了“记住我”，那么显示上次登录的帐号，密码，勾选复选框
		if(globalAppContext.getLoginInfoParams("isRememberMe").equals("true")) {
			acountEditText.setText(globalAppContext.getLoginInfoParams("account"));
			pwdEditText.setText(globalAppContext.getLoginInfoParams("password"));
			checkbox.setChecked(true);
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.loginacccount){			
			if(globalAppContext.isNetworkConnected() && !StringUtils.isEmpty(acountEditText.getText().toString()) && !StringUtils.isEmpty(pwdEditText.getText().toString()) ) {
				new Thread() {
					@Override
					public void run() {
						String recoredId = HttpClientAPI.loginAuth(acountEditText.getText().toString(), CyptoUtils.encode(globalAppContext.ENCRYPT_KEY, pwdEditText.getText().toString()));
						//TODO 要新建一个进程，处理登录事宜
						if
					}
				}.start();
			}
		} else if(v.getId() == R.id.loginregedit) {
			
		} else if(v.getId() == R.id.save_server_ip) {
			
		}
	}
}
