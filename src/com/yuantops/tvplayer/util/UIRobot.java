package com.yuantops.tvplayer.util;

import com.yuantops.tvplayer.bean.Video;

import android.content.Context;
import android.content.Intent;

/**
 * 处理与UI相关的工具类，包括Activity跳转、显示Dialog、显示Toast
 * @author admin (Email: yuan.tops@gmail.com)
 * @date 2015-1-7
 */
public class UIRobot {
	public static final String TAG = UIRobot.class.getSimpleName();
	
	/**
	 * 跳转到影片详情介绍页
	 * @param mContext
	 * @param video
	 */
	public static void showVideoDetail(Context mContext, Video video){
		if(video == null){
			return;
		}
		Intent intent = new Intent(mContext, VideoDisplayActivity.class);
		intent.putExtra("video", video);
		mContext.startActivity(intent);
	}
	
	public static void openVideoPlayer(Context mContext, Video video){
		if(video == null){
			return;
		}
		Intent intent = null;
		if(video.getType().equals(Video.LIVE_BROADCAST_TYPE)){
			intent = new Intent(mContext, VideoPlayActivity_Vitamio.class);
		}else if(video.getType().equals(Video.VIDEO_ON_DEMAND_TYPE)){
			intent = new Intent(mContext, VideoPlayActivity.class);
		}
		intent.putExtra("video", video);
		mContext.startActivity(intent);
		
		if(mContext.)
	}
}
