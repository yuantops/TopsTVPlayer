package com.yuantops.tvplayer;

import com.yuantops.tvplayer.api.HttpClientAPI;
import com.yuantops.tvplayer.util.StringUtils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

/** 
 * 全局上下文：保存调用全局配置，调用网络访问api
 *
 * @author yuan (yuan.tops@gmail.com)
 * @created Dec 28, 2014 2:10:53 PM
 */
public class AppContext extends Application{

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03; 
	
	public static final String ENCRYPT_KEY = "toBeOrNotToBe";
	
	private boolean isLogin = false;
	private String loginAccount = null;
	private String loginRecordId = null;
	
	private String IPAddress = null;
	private String deviceType = Build.MODEL;
	
	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	/**
	 * 检查是否已经接入网络
	 */
	public boolean isNetworkConnected(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}
	
	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	
	
	/**
	 * 初始化本机IP地址
	 */
	public void initIPAddress(){
		WifiManager wifiMan = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		int ipAddress = wifiInf.getIpAddress();
		String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
		
		if(!StringUtils.isEmpty(ip)){
			this.IPAddress = ip;
		}else{
			this.IPAddress = "0.0.0.0";
		}
	}
		
	/**
	 * 获取本机IP地址
	 * @return IP
	 */
	public String getLocalIP(){
		return this.IPAddress;
	}
	
	/**
	 * 获取机器型号
	 * @return device type
	 */
	public String getDeviceType() {
		return this.deviceType;
	}
	
	/**
	 * 获取登录标志
	 */
	public boolean isLogin(){
		return this.isLogin;
	}
	
	/**
	 * 获得登录用户名
	 */
	public String getLoginAccount(){
		return this.loginAccount;
	}
	
	/**
	 * 获得登录在数据库中的记录号
	 */
	public String getloginRecordId(){
		return this.loginRecordId;
	}
	
	/**
	 * 注销此次登录
	 * 在服务器上注销登录；还原登录标记
	 */
	public void logout(){
		if(StringUtils.isEmpty(this.loginAccount) || StringUtils.isEmpty(this.loginAccount)){
			return;
		}
		//TODO 在服务器上注销此次登录
		HttpClientAPI.logout(this.loginRecordId);
		
		this.isLogin = false;
		this.loginAccount = null;
		this.loginRecordId = null;
	}
		
	/**
	 * 用户名和密码认证
	 * @param name 用户名
	 * @param pwd 密码（加密后的）
	 * @return 1）认证失败：“0” 2）认证成功：数据库中登录记录号
	 */
	public String loginAuthenticate(String account, String pwd){
		//TODO 
		return HttpClientAPI.loginAuth(account, pwd);
	}
	
	/**
	 * 设置登录信息
	 * @param account 用户名
	 * @param recordId 数据库中登录记录号
	 */
	public void setLoginInfo(String account, String recordId){
		this.isLogin = true;
		this.loginAccount = account;
		this.loginRecordId = recordId;
	}
	
	/**
	 * 记住用户名和密码，保存到磁盘
	 * @param isRememberMe 
	 * @param account 用户名
	 * @param pwd 密码（加密后）
	 */
	public void saveLoginInfo(boolean isRememberMe, String account, String pwd){
		if(isRememberMe){
			SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
			sharedPreferences.edit().putString("account", account);
			sharedPreferences.edit().putString("password", pwd);
			sharedPreferences.edit().putString("isRememberMe", "true");
			sharedPreferences.edit().commit();
		}else{
			clearLoginInfo();
		}
	}
	
	/**
	 * 保存登录参数
	 * @param key
	 * @param value
	 */
	public void setLoginInfoParams(String key, String value){
		SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(key, value);
		sharedPreferences.edit().commit();
	}
	
	/**
	 * 获取保存的登录参数
	 * @param key 
	 * @return value
	 */
	public String getLoginInfoParams(String key){
		SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, "");
	}
	
	/**
	 * 清除保存在本地的登录参数:
	 */
	public void clearLoginInfo(){
		SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
		sharedPreferences.edit().putString("account", "");
		sharedPreferences.edit().putString("password", "");
		sharedPreferences.edit().putString("isRememberMe", "");
		sharedPreferences.edit().commit();
	}
	
}
