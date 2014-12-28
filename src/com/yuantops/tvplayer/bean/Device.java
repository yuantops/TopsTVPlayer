package com.yuantops.tvplayer.bean;

import android.content.Context;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/** 
 * Android设备信息实体
 *
 * @author yuan (yuan.tops@gmail.com)
 * @created Dec 28, 2014 6:51:53 PM
 */
public class Device {
	private String Manufacturer = null;//制造商
	private String Model = null;//设备名称
	
	private int ScreenWidth = 0;//屏幕宽
	private int ScreenHeight = 0;//屏幕长
	private String IP = null;//IP地址
	
	private static Device instance;
	
	private Device(){
		this.Manufacturer = Build.MANUFACTURER;
		this.Model = Build.MODEL;
	}
	
	private Device(Context mContext){
		this.Manufacturer = Build.MANUFACTURER;
		this.Model = Build.MODEL;
		
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		this.ScreenWidth = display.getWidth();
		this.ScreenHeight = display.getHeight();
	}
	
	public static Device getInstance(){
		if(instance == null){
			instance = new Device();
		}
		return instance;
	}
	
	public static void setScreenSize(Context mContext){
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if(instance == null){
			return;
		}
		instance.ScreenWidth = display.getWidth();
		instance.ScreenHeight = display.getHeight();
	}
	
}
