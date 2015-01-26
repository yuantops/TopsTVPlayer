package com.yuantops.tvplayer.ui;

import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.app.SherlockFragment;
import com.yuantops.tvplayer.AppContext;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.api.HttpClientAPI;
import com.yuantops.tvplayer.bean.NetworkConstants;
import com.yuantops.tvplayer.bean.VideoList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DefaultFragment extends SherlockFragment {
	private static final String TAG = DefaultFragment.class.getSimpleName();	

	private AppContext appContext = null;
	private static final int LIST_SIZE = 5;//列表大小
	
	private VideoList mostPlayedList = new VideoList();//播放次数最多
	private VideoList highestRatingList = new VideoList();//评分最高
	private VideoList newestList = new VideoList();//最新上映
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		appContext = (AppContext) getActivity().getApplication();
		
		Log.v(TAG, "onCreate()");		
		
		//新建进程，1）从服务器抓取列表信息，并2）根据获取的字符串初始化列表
		new Thread(){
			@Override
			public void run() {
				Map<String, String> mostPlayedParams = new HashMap<String, String> ();
				Map<String, String> highestParams = new HashMap<String, String> ();
				Map<String, String> newestParams = new HashMap<String, String> ();
				
				mostPlayedParams.put("SIZE", String.valueOf(LIST_SIZE));
				mostPlayedParams.put("CATEOGORY", "mostPlayed");
				highestParams.put("SIZE", String.valueOf(LIST_SIZE));
				highestParams.put("CATEOGORY", "highestRating");
				newestParams.put("SIZE", String.valueOf(LIST_SIZE));
				newestParams.put("CATEOGORY", "newest");
				
				String servletAppBase = HttpClientAPI.getRootUrl(appContext.getWebServerIP());
				String servletUrl = servletAppBase + NetworkConstants.HTTP_URL_DELIMITER + NetworkConstants.GET_VIDEO_LIST_SERVLET;
				String webserverResp1 = HttpClientAPI.httpGet("http://124.16.138.41:8080/FlingHttpServer/RecommendListServlet?action=MOST_PLAYEDams");
				String webserverResp2 = HttpClientAPI.httpGet("http://124.16.138.41:8080/FlingHttpServer/RecommendListServlet?action=MOST_PLAYEDams");
				String webserverResp3 = HttpClientAPI.httpGet("http://124.16.138.41:8080/FlingHttpServer/RecommendListServlet?action=MOST_PLAYEDams");
				//String webserverResp1 = HttpClientAPI.httpGet(HttpClientAPI.makeURL(servletUrl, mostPlayedParams));
				//String webserverResp2 = HttpClientAPI.httpGet(HttpClientAPI.makeURL(servletUrl, highestParams));
				//String webserverResp3 = HttpClientAPI.httpGet(HttpClientAPI.makeURL(servletUrl, newestParams));
				
				Log.v(TAG, "webserverResp1\n" + webserverResp1 + "\n");
				Log.v(TAG, "webserverResp2\n" + webserverResp2 + "\n");
				Log.v(TAG, "webserverResp3\n" + webserverResp3 + "\n");
				
				mostPlayedList.populateListFromString(webserverResp1);
				highestRatingList.populateListFromString(webserverResp2);
				newestList.populateListFromString(webserverResp3);		
				
				Log.v(TAG, "mostPlayedList size:\n" + mostPlayedList.getSize() + "\n");
				Log.v(TAG, "webserverResp2\n" + highestRatingList.getSize() + "\n");
				Log.v(TAG, "webserverResp3\n" + newestList.getSize() + "\n");
			}
			
		}.start();
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.activity_main, container, false);
		return v;
	}
}
