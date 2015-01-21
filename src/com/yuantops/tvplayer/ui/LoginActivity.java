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
	
	private AppContext appContext = null;
	
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
			DLNABody params = (DLNABody) arg1.getSerializableExtra("Params");
			
			//登录ID "0":认证失败；非零:认证成功
			if(params.getValue("RECORDID").equals("0")) {
				UIRobot.showToast(LoginActivity.this, "Login Auth Failed.");
				return;
			}
			
			String loginAccount = params.getValue("ACCOUNT");
			String loginRecordId = params.getValue("RECORDID");
			//密码为空:第三方登录(二维码扫描),不保存帐号、密码到磁盘; 
			//非空:本机以http方式登录，判断是否勾选"记住密码"
			String chec = checkbox.isChecked() ? "true":"false";
			Log.v(TAG, "checkbox is checked"+chec);
			
			if((!StringUtils.isEmpty(params.getValue("PASSWORD"))) ) {
				Log.v(TAG, "save login params in Broadcast Receiver");
				String loginPassword = params.getValue("PASSWORD");
				appContext.saveLoginInfoParams(checkbox.isChecked(), loginAccount, loginPassword, appContext.getWebServerIP());
			}

			appContext.setLogged(loginAccount, loginRecordId);
			UIRobot.gotoHomePage(LoginActivity.this);
		}		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		AppManager.getInstance().addActivity(this);
		
		appContext = (AppContext) this.getApplication();
		appContext.initClientIP();
		initViewComponents();
						
		//如果a)网络可用且b)服务器IP合法，1) 以绑定方式启动service 2)注册处理登录的broadcast receiver
		if(appContext.isNetworkConnected() &&  StringUtils.isValidIPAddress(appContext.getSocketServerIP())) {
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
				
		//取出本机IP,设备类型
		deviceTextView.setText(appContext.getDeviceType());
		ipTextView.setText(appContext.getClientIP());
		
		
		//如果上次登录时选择了“记住我”，那么显示上次登录的帐号，密码，勾选复选框
		if(appContext.getLoginInfoParams("isRememberMe").equals("true")) {
			acountEditText.setText(appContext.getLoginInfoParams("account"));
			pwdEditText.setText(CyptoUtils.decode(AppContext.ENCRYPT_KEY, appContext.getLoginInfoParams("password")));
			serverIPEditText.setText(appContext.getLoginInfoParams("severip"));
			checkbox.setChecked(true);
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.loginacccount){
			String loginAccount = acountEditText.getText().toString();
			String loginPassword = pwdEditText.getText().toString();
			String severIP = serverIPEditText.getText().toString();
			
			if(!appContext.isNetworkConnected()) {
				//如果无网络连接，提示无法请稍候再试
				UIRobot.showToast(this, "No network! Please try again later.");
			} else if(StringUtils.isEmpty(loginAccount)) {
				//帐号为空
				UIRobot.showToast(this, "Login Account cannot be empty!");
			} else if(StringUtils.isEmpty(loginPassword)) {
				//密码为空
				UIRobot.showToast(this, "Password cannot be empty!");
			} else if(StringUtils.isEmpty(severIP) || !StringUtils.isValidIPAddress(severIP)) {
				//服务器IP不合法
				UIRobot.showToast(this, "Please input valid server IP address!");
 			} else {
 				
 				//更新serverIP
 				appContext.setServerIP(severIP,severIP); 				
 				
				//如果还没绑定后台服务，绑定后台服务，并注册登录broadcast receiver
				if(!mBound || !mRegistered) {
					Intent intent = new Intent(this, AppService.class);
			        bindService(intent, conn, Context.BIND_AUTO_CREATE);
			        mBound = true;
			        
			        registerReceiver(loginBrdReceiver, intentFilter);
			        mRegistered = true;
				}
								
				//新开一个线程，1)去服务器认证 2)若认证成功，发送登录成功的广播。保存登录帐号，密码，服务器IP以便下次登录时显示
				new LoginAuthThread(this,loginAccount,CyptoUtils.encode(AppContext.ENCRYPT_KEY, loginPassword)).start();
 			}			
		} else if(v.getId() == R.id.loginregedit) {
			UIRobot.gotoRegisterPage(this);
			
		} else if(v.getId() == R.id.save_server_ip) {
			if(StringUtils.isValidIPAddress(serverIPEditText.getText().toString())) {
				//保存服务器信息到磁盘
				appContext.saveLoginInfoParams("severip", serverIPEditText.getText().toString());
				//如果还未绑定后台服务、未注册广播接收器，分别绑定、注册
				if (!mBound) {
					Intent intent = new Intent(this, AppService.class);
			        bindService(intent, conn, Context.BIND_AUTO_CREATE);
			        mBound = true;
				}
				if (!mRegistered) {
					registerReceiver(loginBrdReceiver, intentFilter);
			        mRegistered = true;
				}
			}
		}
	}
	
	/**
	 * 到服务器登录认证，结果以广播形式发出
	 * @author yuan (Email: yuan.tops@gmail.com) *
	 * @date Jan 19, 2015 
	 */
	class LoginAuthThread extends Thread {
		private Context mContext;
		private String mAccount;
		private String mPassword;
		
		public LoginAuthThread(Context context, String account, String password) {
			this.mContext = context;
			this.mAccount = account;
			this.mPassword = password;
		}
		
		@Override
		public void run() {
			String recordID = HttpClientAPI.loginAuth(appContext.getWebServerIP(), mAccount, mPassword);
			DLNABody loginAuthResult = new DLNABody();
			loginAuthResult.addRecord("ACTION", "LOGIN");
			loginAuthResult.addRecord("ACCOUNT", mAccount);
			loginAuthResult.addRecord("PASSWORD", mPassword);
			loginAuthResult.addRecord("RECORDID", recordID);
			
			Log.v(TAG, "body content in Intent:\n"+loginAuthResult.printDLNABody());
			Intent intent = new Intent(SocketMsgDispatcher.LOGIN_BROADCAST);
			intent.putExtra("Params", loginAuthResult);
			mContext.sendBroadcast(intent);
		}
	}
}
