package com.yuantops.tvplayer.ui;

import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.bean.Video;
import com.yuantops.tvplayer.player.VideoPlayer;
import com.yuantops.tvplayer.player.VideoPlayer_native;
import com.yuantops.tvplayer.player.VideoPlayer_vitamio;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Activity for playing on-demand/live video streams
 * 播放点播/直播流的Activity
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Mar 12, 2015 
 */
public class VideoPlayActivity extends Activity{
	private static final String TAG = VideoPlayActivity.class.getSimpleName();	
	private static final byte   STANDARD = 1, HIGH = 2, SUPER = 3; //Device resolution for deciding video type
	
	private int    viHeight, viWidth;               //Video Player size: height, width
	private int    initTime, curTime, totTime;      //Video started at, current at, will end at	
	private Video  video;                           //Video item
	
	private SurfaceView surView;                    //View for displaying video content; 显示视频内容的组件
	private SeekBar     seekBar;                    //SeekBar
	private TextView    curTimeView, totTimeView;   //current moment, total time TextView under SeekBar	
	private ImageButton playImgBtn;                 //play/pause button	
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate()...");
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);                   // 不显示标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, // 全屏
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置横屏播放
		this.setContentView(R.layout.activity_videoplay);
		
		//Bind to Service
		Intent bindIntent = new Intent();
		bindIntent.setAction("com.yuantops.TopsTVPlayer.SERVICE"); //Action is defined in AndroidManifest.xml
		getApplicationContext().bindService(bindIntent, conn, Service.BIND_AUTO_CREATE);
		
		initViewComponents();
		addViewListeners();
		
		//Retrieve video from intent
		Intent rcvIntent = getIntent();
		video   = (Video) rcvIntent.getSerializableExtra("video");	
				
		String viUrl;
		if (video.getType().equals(Video.LIVE_BROADCAST_TYPE)) {
			viUrl    = video.getBroadcastUrl();
			viPlayer = new VideoPlayer_vitamio(surView, viUrl, this);
		} else {
			switch (getDevResolution()) {
			case HIGH:
				viUrl = video.getHighDefiUrl();
				break;
			case STANDARD:
				viUrl = video.getStandardDefiUrl();
				break;
			case SUPER:
				viUrl = video.getSuperDefiUrl();
				break;
			default:
				viUrl = video.getStandardDefiUrl();
				break;
			}
			viPlayer = new VideoPlayer_native(surView, seekBar, viUrl, curTimeView, totTimeView, 0, this);
		}
	}
	
	/**
	 * Get references of view components
	 */
	private void initViewComponents() {
		surView     = (SurfaceView) this.findViewById(R.id.main_surface_view);
		seekBar     = (SeekBar)     this.findViewById(R.id.sb_video_player);
		curTimeView = (TextView)    this.findViewById(R.id.tv_video_player_time);
		totTimeView = (TextView)    this.findViewById(R.id.tv_video_player_length);
		playImgBtn  = (ImageButton) this.findViewById(R.id.ib_play_pause);
		
		//Set focus, background image for play button
		playImgBtn.requestFocus();
		playImgBtn.setImageResource(R.drawable.pause);
		
		//Get screen size and thus deciding device resolution 
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		viHeight = dm.heightPixels;
		viWidth  = dm.widthPixels;
	}
	
	/**
	 * Add listeners to views
	 */
	private void addViewListeners() {
		seekBar.setOnSeekBarChangeListener(seekBarListener);
		playImgBtn.setOnClickListener(clickListener);
		playImgBtn.setOnFocusChangeListener(viewFocusChangeListener);
	}
	
	/**
	 * When moving seeking bar, update progress.
	 */
	private OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
		int progress;
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress0, boolean fromUser) {
			progress = (int) (progress0 * viPlayer.getDuration()
					/ seekBar.getMax());
		}
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
		}
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			viPlayer.seekTo(progress);
			//Log.v(TAG, "onStopTrackingTouch() called");
		}		
	};
	
	/**
	 * when clicking pause/play button, flip button's background image
	 */
	private OnClickListener clickListener  = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ib_play_pause:
				if (!viPlayer.isPlaying()) {
					viPlayer.play();
					playImgBtn.setImageResource(R.drawable.pause);					
				} else {
					viPlayer.pause();
					playImgBtn.setImageResource(R.drawable.play);
				}
				break;
			default:
				break;
			}
		}		
	};
	
	/**
	 * when focus of view  changes;
	 */
	private OnFocusChangeListener viewFocusChangeListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setBackgroundColor(Color.GRAY);
				Log.v(TAG + " >focus changed", "has focus");
			} else {
				v.setBackgroundColor(Color.TRANSPARENT);
				Log.v(TAG + " >focus changed", "lose focus");
			}
		}
	};
	
	/**
	 * Return device resolution type according to screen size
	 * @return
	 */
	private byte getDevResolution() {
		if (viWidth <= 480 || viHeight <= 650) {
			return STANDARD;
		} else if (viWidth > 480 && viWidth <= 1024
				|| viHeight > 800 && viHeight <= 1024) {
			return HIGH;
		} else {
			return SUPER;
		}
	}
	
}
