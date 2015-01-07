package com.yuantops.tvplayer.bean;

import java.io.Serializable;
import java.util.ArrayList;

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
				v.setHighDefiUrl(jsonObject.getString("highdefinition_url"));
				v.setStandardDefiUrl(jsonObject.getString("standarddefinition_url"));
				v.setSuperDefiUrl(jsonObject.getString("superdefinition_url"));
				v.setBroadcastUrl(jsonObject.getString("broadcast_url"));
				
				list.add(v);
				
			}
		} catch (JSONException e) {
			Log.d(TAG, "+++Failed to create json array since json error.+++");
			e.printStackTrace();
		}
		
		
	}
}
