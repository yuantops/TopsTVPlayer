package com.yuantops.tvplayer.bean;

import java.io.Serializable;

/**
 * 视频/节目的实体类
 * @author yuan (Email: yuan.tops@gmail.com) 
 * @date Dec 30, 2014 
 */
public class Video implements Serializable{
	public static final String VIDEO_ON_DEMAND_TYPE = "VOD";
	public static final String LIVE_BROADCAST_TYPE = "live";
	
	private String videoName_en;//英文名
	private String videoName_cn;//中文名
	private String introduction;//剧情介绍
	private String releaseDate;//上映日期
	private String runtime;//时长
	private String[] category;//类别
	private String type;//直播/点播
	private String playedTimes;//播放次数
	
	private String posterUrl;//海报图片链接
	private String highDefiUrl;//高清链接
	private String standardDefiUrl;//标清链接
	private String superDefiUrl;//超清链接
	private String broadcastUrl;//直播链接
	
	public Video(String videoName_cn, String videoName_en, String intro, String releaseDate, String runtime, String[] category, String type, String playedTimes, String posterUrl, String highDefiUrl, String standardDefiUrl, String superDefiUrl, String broadcastUrl){
		this.videoName_cn = videoName_cn;
		this.videoName_en = videoName_en;
		this.introduction = intro;
		this.releaseDate = releaseDate;
		this.runtime = runtime;
		this.category = category;
		this.type = type;
		this.playedTimes = playedTimes;
		this.posterUrl = posterUrl;
		this.highDefiUrl = highDefiUrl;
		this.standardDefiUrl = standardDefiUrl;
		this.superDefiUrl = superDefiUrl;
		this.broadcastUrl = broadcastUrl;
	}

	public String getVideoName_en() {
		return videoName_en;
	}

	public void setVideoName_en(String videoName_en) {
		this.videoName_en = videoName_en;
	}

	public String getVideoName_cn() {
		return videoName_cn;
	}

	public void setVideoName_cn(String videoName_cn) {
		this.videoName_cn = videoName_cn;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String[] category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlayedTimes() {
		return playedTimes;
	}

	public void setPlayedTimes(String playedTimes) {
		this.playedTimes = playedTimes;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getHighDefiUrl() {
		return highDefiUrl;
	}

	public void setHighDefiUrl(String highDefiUrl) {
		this.highDefiUrl = highDefiUrl;
	}

	public String getStandardDefiUrl() {
		return standardDefiUrl;
	}

	public void setStandardDefiUrl(String standardDefiUrl) {
		this.standardDefiUrl = standardDefiUrl;
	}

	public String getSuperDefiUrl() {
		return superDefiUrl;
	}

	public void setSuperDefiUrl(String superDefiUrl) {
		this.superDefiUrl = superDefiUrl;
	}

	public String getBroadcastUrl() {
		return broadcastUrl;
	}

	public void setBroadcastUrl(String broadcastUrl) {
		this.broadcastUrl = broadcastUrl;
	}	

}
