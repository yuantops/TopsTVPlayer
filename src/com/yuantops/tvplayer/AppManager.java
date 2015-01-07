package com.yuantops.tvplayer;

import android.app.Activity;
import android.content.Context;
import java.util.Stack;

/**
 * AppActivity的全局管理工具，单例模式
 * @author admin (Email: yuan.tops@gmail.com)
 * @date 2015-1-7
 */
public class AppManager {
	private static AppManager instance;
	private Stack<Activity> activityStack;
	
	private AppManager(){
		activityStack = new Stack<Activity>();
	}
	
	/**
	 * 单例模式
	 * @return 唯一的AppManager Instance
	 */
	public static AppManager getInstance(){
		if(instance == null){
			instance = new AppManager();
		}
		return instance;
	}
	
	/**
	 * 压Activity入栈
	 * @param activity
	 */
	public void addActivity(Activity activity){
		activityStack.add(activity);
	}
	
	
	/**
	 * 获取当前栈顶的Activity
	 * @return
	 */
	public Activity currentActivity(){
		return activityStack.lastElement();
	}
	
	/**
	 * 销毁栈顶的Activity
	 */
	public void finishActivity(){
		Activity lastActivity = activityStack.lastElement();
		activityStack.remove(lastActivity);
		lastActivity.finish();
		lastActivity=null;
	}
	
	/**
	 * 销毁传入的Activity
	 * @param activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		};
	}
	
	/**
	 * 销毁栈内所有Activity
	 */
	public void finishAllActivity(){
		for (int i = 0, size = activityStack.size(); i < size; i++){
            if (null != activityStack.get(i)){
            	activityStack.get(i).finish();
            }
	    }
		activityStack.clear();
	}
	
	/**
	 * 退出程序
	 * @param mContext
	 */
	public void ExitApp(Context mContext){
		try {
			finishAllActivity();			
			System.exit(0);
		} catch (Exception e) {	}
	}
}
