package com.yuantops.tvplayer.ui;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.yuantops.tvplayer.AppContext;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.adapter.GridViewAdapterDetail;
import com.yuantops.tvplayer.api.HttpClientAPI;
import com.yuantops.tvplayer.bean.NetworkConstants;
import com.yuantops.tvplayer.bean.Video;
import com.yuantops.tvplayer.bean.VideoList;
import com.yuantops.tvplayer.util.UIRobot;

public class MovieFragment extends SherlockFragment implements OnItemClickListener {
	private static final String TAG = MovieFragment.class.getSimpleName();
	
	private AppContext appContext = null;
	
	private VideoList movieList = new VideoList();
	private GridViewAdapterDetail movieAdapter = null;
	
	private GridViewNew movieGridView;
	private Handler myHandler = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getActivity().getApplication();
		
		movieAdapter = new GridViewAdapterDetail(getActivity(), movieList);	
		
		myHandler = new Handler() {
			@Override  
            public void handleMessage(Message msg) {  
                super.handleMessage(msg);  
                if(msg.what == 1)  {
                	movieAdapter.notifyDataSetChanged();
                }
           }
		};
		
		new Thread() {
			@Override
			public void run() {
				Map<String, String> movieListParams = new HashMap<String, String>();
				movieListParams.put("CATEGORY", "movie");
				
				String servletAppBase = HttpClientAPI.getRootUrl(appContext.getWebServerIP());
				String servletUrl = servletAppBase + NetworkConstants.HTTP_URL_DELIMITER + NetworkConstants.GET_VIDEO_LIST_SERVLET;
				String webserverResp1 = HttpClientAPI.httpGet("http://124.16.138.41:8080/FlingHttpServer/RecommendListServlet?action=MOST_PLAYED");
				
				//String webserverResp1 = HttpClientAPI.httpGet(HttpClientAPI.makeURL(servletUrl, movieListParams));
				movieList.populateListFromString(webserverResp1);
				
				Message dataLoadedMsg = new Message();
				dataLoadedMsg.what = 1;
				myHandler.sendMessage(dataLoadedMsg);
			}
		}.start();
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_movie, container, false);
		movieGridView = (GridViewNew) v.findViewById(R.id.moviegridView);
		movieGridView.setAdapter(movieAdapter);
		movieGridView.setSelection(1);
		movieGridView.setOnItemClickListener(this);
		return v;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Video video = (Video) parent.getItemAtPosition(position);
		UIRobot.showVideoDetail(getActivity(), video);	
	}
}
