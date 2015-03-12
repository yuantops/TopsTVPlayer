package com.yuantops.tvplayer.ui;

import com.yuantops.tvplayer.bean.Video;
import com.yuantops.tvplayer.player.VideoPlayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Activity for playing on-demand/live video streams
 * 播放点播/直播流的Activity
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Mar 12, 2015 
 */
public class VideoPlayActivity extends Activity{
	private static final String TAG = VideoPlayActivity.class.getSimpleName();
	
	private int viHeight, viWidth;                  //Video Player size: height, width
	private int initTime, curTime, totTime;         //Video started at, current at, will end at	
	private Video  video;                           //Video item
	
	private SurfaceView surView;                    //View for displaying video content; 显示视频内容的组件
	private SeekBar     seekBar;                    //SeekBar
	private TextView    curTimeView, totTimeView;   //current moment, total time under SeekBar	
	private ImageButton playPauseImgBtn;            //	
	private ImageView   QRCodeImgV;                 //	
	private VideoPlayer viPlayer;                   //
	
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			Log.v(TAG, "Service connected");
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Log.v(TAG, "Service disconnected");
		}		
	};
	
	public void onCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
}
