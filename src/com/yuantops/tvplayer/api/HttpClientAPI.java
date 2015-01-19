package com.yuantops.tvplayer.api;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.yuantops.tvplayer.bean.NetworkConstants;


/**
 * 与服务器进行交互的Http工具类
 * @author admin (Email: yuan.tops@gmail.com)
 * @date 2015-1-6
 */
public class HttpClientAPI {
	private static final String TAG = HttpClientAPI.class.getSimpleName();
	
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;
	
	/**
	 * @param url_base URL
	 * @param params 需要传入的参数对
	 * @return 完整的URL
	 */
	public static String makeURL(String url_base, Map<String, String> params){
		StringBuilder url = new StringBuilder(url_base);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
		}

		return url.toString().replace("?&", "?");
	}
	
		
	/**
	 * @param url 需要访问的URL
	 * @return 服务器返回的字符串
	 */
	public static String httpGet(String url){		
		HttpParams HttpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(HttpParameters, TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(HttpParameters, TIMEOUT_SOCKET);
		
		DefaultHttpClient httpClient = null;
		HttpPost httpPost = null;
		HttpResponse httpResp = null;
		
		String httpRespString = "";
		int time = 0;
				
		do{
			try{
				Log.v(TAG, "here1");
				httpClient = new DefaultHttpClient(HttpParameters);
				Log.v(TAG, "here2");
				Log.v(TAG, "postUrl:   "+url);				
				httpPost = new HttpPost(url);
				Log.v(TAG, "here3");
				httpResp = httpClient.execute(httpPost);
				Log.v(TAG, "here4");
				httpRespString = EntityUtils.toString(httpResp.getEntity());
				Log.v(TAG, "here5");
				Log.v(TAG, httpRespString);
				break;
			}catch(SocketTimeoutException e){
				Log.v(TAG, "here6");
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
			} finally {
				Log.v(TAG, "here7");
				// 释放连接
				if(httpResp.getEntity() != null){
					try {
						httpResp.getEntity().consumeContent();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				httpClient = null;
			}
		}while(time<RETRY_TIME);
		
		return httpRespString;
	}
	
	/**
	 * 用户登录认证
	 * @param serverip 服务器IP
	 * @param account 帐号
	 * @param CyptedPwd 加密后的密码
	 * @return 认证失败："0"; 认证成功：数据库中登录记录号
	 */
	public static String loginAuth(String serverip, String account, String CyptedPwd){
		String baseURL = "http://" + serverip + ":"+ NetworkConstants.WEB_SERVER_PORT  + NetworkConstants.HTTP_URL_DELIMITER + NetworkConstants.WEB_SERVER_APP_NAME  + NetworkConstants.HTTP_URL_DELIMITER + NetworkConstants.LOGIN_SERVLET;
		
		Map<String, String> loginParams = new HashMap<String, String>();
		loginParams.put("Account", account);
		loginParams.put("Password", CyptedPwd);
		
		return httpGet(makeURL(baseURL, loginParams));
	}
	
	/**
	 * 用户注销
	 * @param serverip 服务器IP
	 * @param loginId 登录成功后返回的登录记录ID
	 */
	public static void logout(String serverip, String loginId){
		String baseURL = "http://" + serverip + ":"+ NetworkConstants.WEB_SERVER_PORT  + NetworkConstants.HTTP_URL_DELIMITER + NetworkConstants.LOGOUT_SERVLET;
		
		Map<String, String> logoutParams = new HashMap<String, String>();
		logoutParams.put("LoginRecord", loginId);
		
		httpGet(makeURL(baseURL, logoutParams));
	}
	
}
