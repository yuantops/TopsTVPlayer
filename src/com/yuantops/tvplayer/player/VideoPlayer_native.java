package com.yuantops.tvplayer.player;

import java.util.Timer;
import java.util.TimerTask;

import com.yuantops.tvplayer.util.StringUtils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Videoplayer based on native MediaPlayer provided by official Android library;
 * Used for playing on-demand video stream in HTTP protocol;
 * Do not support real-time live video stream in RTSP protocol
 * 基于Andorid官方提供的原生MediaPlayer插件;
 * 用以播放HTTP格式的点播流;
 * 不支持RTSP格式的实时视频直播流
 * @author yuan (Email: yuan.tops@gmail.com)
 * @date Mar 9, 2015 
 */
public class VideoPlayer_native implements VideoPlayer{
	private static final String TAG = VideoPlayer_native.class.getSimpleName();	
	private static final int CURR_TIME_VALUE = 8;
	
	private Context       mContext;
	private SurfaceHolder surHolder;
	private SeekBar       seekBar;
	private MediaPlayer   mePlayer;
	private TextView      vpCurrentTime, vpTotalTime;//currentTime, total video time length
	private int           vpWidth, vpHeight;
	
	private String   vUrl;
	private int      vpBeginTime;//seekbar position when initializing
	private boolean  flag  = true;
	private Timer    timer = new Timer();
	
	private TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mePlayer == null) {
				return;
			} else
				try {
					if (mePlayer.isPlaying()) {
						int position = (int) mePlayer.getCurrentPosition();
						int max = seekBar.getMax();
						if (position != 0) {
							Message msg = mHandler.obtainMessage(
									CURR_TIME_VALUE, position, max);
							mHandler.sendMessage(msg);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}		
	};
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CURR_TIME_VALUE:
				int position = msg.arg1;
				int max = msg.arg2;
				int duration = (int) mePlayer.getDuration();
				vpCurrentTime.setText(StringUtils.millisToString(position));
				vpTotalTime.setText(StringUtils.millisToString(duration));
				if (duration > 0) {
					int pos = max * position / duration;
					seekBar.setProgress(pos);
				}
				if (vpBeginTime != 0 && flag) {
					mePlayer.seekTo(vpBeginTime);
					seekBar.setProgress(max * vpBeginTime / duration);
					flag = false;
				}
				break;
			case 1:
				break;
			}
		};
	};
	
	private SurfaceHolder.Callback surCallbackListener = new SurfaceHolder.Callback() {		
		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
		}		
		@Override
		public void surfaceCreated(SurfaceHolder arg0) {
			mePlayer = new MediaPlayer();
			mePlayer.setDisplay(surHolder);
			mePlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mePlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
			mePlayer.setOnPreparedListener(onPreparedListener);
			
			try {
				mePlayer.reset();
				mePlayer.setDataSource(vUrl);
				mePlayer.prepareAsync();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
			mePlayer.setDisplay(surHolder);
		}
	};
	
	private OnBufferingUpdateListener onBufferingUpdateListener = new OnBufferingUpdateListener() {
		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			seekBar.setSecondaryProgress(percent);
		}
	};
	
	private OnPreparedListener onPreparedListener = new OnPreparedListener() {
		@Override
		public void onPrepared(MediaPlayer mp) {
			vpHeight = mp.getVideoHeight();
			vpWidth = mp.getVideoWidth();
			if (vpHeight != 0 && vpWidth != 0) {
				mp.start();// 播放视频
			}
			Log.v("mediaPlayer", "onPrepared");
		}
	};
	
	/**
	 * @param surView
	 * @param seekBar
	 * @param vUrl
	 * @param vpCurrentTime
	 * @param vpTotalTime
	 * @param vpBeginTime
	 * @param context
	 */
	public VideoPlayer_native(SurfaceView surView, SeekBar seekBar, String vUrl, 
			TextView vpCurrentTime, TextView vpTotalTime, 
			int vpBeginTime, Context context) {

		Log.v(TAG, "initialize");
		this.mContext = context;
		this.seekBar  = seekBar;
		this.vUrl     = vUrl;
		this.vpCurrentTime = vpCurrentTime;
		this.vpTotalTime   = vpTotalTime;
		this.vpBeginTime   = vpBeginTime;
		
		surHolder = surView.getHolder();
		surHolder.addCallback(surCallbackListener);
		surHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		timer.schedule(mTimerTask, 0, 1000);
	}
	
	@Override
	public void play() {
		mePlayer.start();		
	}

	@Override
	public void pause() {
		mePlayer.pause();
	}

	@Override
	public void stop() {
		if (mePlayer != null) {
			mePlayer.stop();
			mePlayer.release();
			mTimerTask.cancel();
			mePlayer = null;
		}
	}

	@Override
	public boolean isPlaying() {
		boolean flag;
		if (mePlayer == null) {
			flag = false;
		} else {
			flag = mePlayer.isPlaying();
		}
		return flag;
	}
	
	public int getCurrentPosition() {
		if (mePlayer == null) {
			return 0;
		} else {
			return (int) mePlayer.getCurrentPosition();
		}
	}
	
	public int getDuration() {
		return mePlayer.getDuration();
	}

	public void seekTo(int progress) {
		mePlayer.seekTo(progress);
		//Log.v(TAG, "seekTo() " + progress);
	}
}
