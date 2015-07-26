package com.yuantops.tvplayer.player;

/**
 * Interface for VideoPlayer component, defining universal methods like play, pause and stop
 * @author yuan (Email: yuan.tops@gmail.com) *
 * @date Mar 9, 2015 
 */
public interface VideoPlayer {
	void play();
	void pause();
	void stop();
	boolean isPlaying(); 
	int getDuration();
	void seekTo(int progress);
}
