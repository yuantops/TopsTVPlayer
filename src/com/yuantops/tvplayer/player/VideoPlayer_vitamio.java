package com.yuantops.tvplayer.player;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Videoplayer based on thrid-party library Vitamio;
 * Used for playing real-time live video stream in RTSP protocol;
 * Also support on-demand video stream in HTTP protocol
 * 基于第三方的MediaPlayer插件 Vitamio;
 * 用以播放RTSP格式的实时视频直播流;
 * 同時支持HTTP格式的点播流
 * @author yuan (Email: yuan.tops@gmail.com)
 * @date Mar 9, 2015 
 */
public class VideoPlayer_vitamio implements VideoPlayer,
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnVideoSizeChangedListener, SurfaceHolder.Callback {
	private static final String TAG = VideoPlayer.class.getSimpleName();
	private int mVideoWidth;
	private int mVideoHeight;
	private Context mContext;// VideoPlayerVitamio所处的上下文
	private SurfaceHolder surfaceHolder;//
	private MediaPlayer mediaPlayer;// 组件：MediaPlayer
	private String videoUrl;// 视频流URL
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;

	public VideoPlayer_vitamio(SurfaceView surfaceView, String videoUrl,
			Context context) {
		Log.v(TAG, "initialize");
		this.surfaceHolder = surfaceView.getHolder();
		this.videoUrl = videoUrl;
		this.mContext = context;

		this.surfaceHolder.addCallback(this);
		this.surfaceHolder.setFormat(PixelFormat.RGBX_8888);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		Log.v(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {

		doCleanUp();

		mediaPlayer = new MediaPlayer(mContext);
		try {
			Log.v(TAG + " >>videoUrl", videoUrl);
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnVideoSizeChangedListener(this);
			// this.mContext.getSystemService(arg0)setVolumeControlStream(AudioManager.STREAM_MUSIC);
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		Log.v(TAG, "surfaceDestroyed called");

	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
					+ ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "onCompletion called");
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		Log.d(TAG, "onBufferingUpdate called");
	}

	private void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	private void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
		mediaPlayer.start();
	}

	public void play() {
		mediaPlayer.start();
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public void stop() {
		if (mediaPlayer != null) {
			// mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public boolean isPlaying() {
		if (mediaPlayer == null) {
			return false;
		} else {
			return mediaPlayer.isPlaying();
		}
	}

	public int getCurrentPosition() {
		Log.v(TAG, "set returned postion 50");
		return 50;
	}
	
	public int getDuration() {
		return 0;
	}

	public void seekTo(int progress) {
		return;
	}
}
