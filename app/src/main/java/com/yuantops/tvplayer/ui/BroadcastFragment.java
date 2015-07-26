package com.yuantops.tvplayer.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
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

public class BroadcastFragment extends SherlockFragment implements OnItemClickListener {
	private static final String TAG = BroadcastFragment.class.getSimpleName();
	
	private AppContext appContext = null;	
	private VideoList broadcastList = new VideoList();
	private GridViewAdapterDetail broadcastAdapter = null;
	
	private GridViewNew broadcastGridView;
	private Handler myHandler = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate()");
		appContext = (AppContext) getActivity().getApplication();
		
		broadcastAdapter = new GridViewAdapterDetail(getActivity(), broadcastList);	
		
		myHandler = new Handler() {
			@Override  
            public void handleMessage(Message msg) {  
                super.handleMessage(msg);  
                if(msg.what == 1)  {
                	broadcastAdapter.notifyDataSetChanged();
                }
           }
		};
		
		new Thread() {
			@Override
			public void run() {
				Map<String, String> broadcastListParams = new HashMap<String, String>();
				broadcastListParams.put("CATEGORY", "broadcast");
				
				String servletAppBase = HttpClientAPI.getRootUrl(appContext.getWebServerIP());
				String servletUrl = servletAppBase + NetworkConstants.HTTP_URL_DELIMITER + NetworkConstants.GET_VIDEO_LIST_SERVLET;
				String webserverResp1 = HttpClientAPI.httpGet("http://124.16.138.41:8080/FlingHttpServer/RecommendListServlet?action=MOST_PLAYED");
				
				//String webserverResp1 = HttpClientAPI.httpGet(HttpClientAPI.makeURL(servletUrl, broadcastListParams));
				broadcastList.populateListFromString(webserverResp1);
				Message dataLoadedMsg = new Message();
				dataLoadedMsg.what = 1;
				myHandler.sendMessage(dataLoadedMsg);
				//broadcastAdapter.notifyDataSetChanged();
			}
		}.start();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_broadcast, container, false);		
		broadcastGridView = (GridViewNew) v.findViewById(R.id.broadcastGridView);
		broadcastGridView.setAdapter(broadcastAdapter);
		broadcastGridView.setSelection(1);
		broadcastGridView.setOnItemClickListener(this);
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Video video = (Video) parent.getItemAtPosition(position);
		UIRobot.showVideoDetail(getActivity(), video);	
	}
}
