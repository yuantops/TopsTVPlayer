package com.yuantops.tvplayer.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 节目列表
 * @author admin (Email: yuan.tops@gmail.com)
 * @date 2015-1-7
 */
public class VideoList implements Serializable{
	private static final String TAG = VideoList.class.getSimpleName();
	private ArrayList<Video> list;
	
	public VideoList(){
		list = new ArrayList<Video>();
	}
	
	public int getSize() {
		return list.size();
	}
	
	public Video getVideo(int position) {
		return list.get(position);
	}
	
	/**
	 * 从json格式的字符串中填充VideoList
	 * @param jsonString
	 */
	public void populateListFromString(String jsonString){
		JSONArray jsonArray = null;		
		JSONObject jsonnode = null;
		
		try {
			jsonnode = new JSONObject(jsonString);
			jsonArray = jsonnode.getJSONArray("videoinfo");
			if(jsonArray == null || jsonArray.length() == 0){
				return;
			}
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);

				Video v = new Video();
				v.setVideoName_en(jsonObject.getString("englishname"));
				v.setVideoName_cn(jsonObject.getString("name"));
				v.setCategory(jsonObject.getString("category"));
				v.setIntroduction(jsonObject.getString("introduction"));
				v.setPlayedTimes(jsonObject.getInt("playtimes"));
				v.setReleaseDate(jsonObject.getString("releasedate"));
				v.setRuntime(jsonObject.getString("duringtime"));
				v.setType(jsonObject.getString("type"));
				
				v.setPosterUrl(jsonObject.getString("image_url"));
				v.setHighDefiUrl(jsonObject.getString("high_definition_url"));
				v.setStandardDefiUrl(jsonObject.getString("standard_definition_url"));
				v.setSuperDefiUrl(jsonObject.getString("super_definition_url"));
				//v.setBroadcastUrl(jsonObject.getString("broadcast_url"));
				
				list.add(v);
				
			}
		} catch (JSONException e) {
			Log.d(TAG, "+++Failed to create json array since json error.+++");
			e.printStackTrace();
		}			
	}
	
	public void print() {
		Iterator ite = list.iterator();
		Video item = null;
		while(ite.hasNext()) {
			item = (Video) ite.next();
			Log.v(TAG, "video name: " + item.getVideoName_cn());
		}
	}
}
