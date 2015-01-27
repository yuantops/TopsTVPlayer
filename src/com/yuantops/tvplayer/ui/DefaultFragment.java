package com.yuantops.tvplayer.ui;

import java.util.HashMap;
import java.util.Map;

import com.actionbarsherlock.app.SherlockFragment;
import com.yuantops.tvplayer.AppContext;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.adapter.GridViewAdapterDetail;
import com.yuantops.tvplayer.api.HttpClientAPI;
import com.yuantops.tvplayer.bean.NetworkConstants;
import com.yuantops.tvplayer.bean.Video;
import com.yuantops.tvplayer.bean.VideoList;
import com.yuantops.tvplayer.util.UIRobot;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class DefaultFragment extends SherlockFragment implements OnItemClickListener {
	private static final String TAG = DefaultFragment.class.getSimpleName();	

	private AppContext appContext = null;
	private static final int LIST_SIZE = 5;//列表大小
	
	private VideoList mostPlayedList = new VideoList();//播放次数最多
	private VideoList highestRatingList = new VideoList();//评分最高
	private VideoList newestList = new VideoList();//最新上映
	
	private GridViewAdapterDetail mostPlayedAdapter = null;
	private GridViewAdapterDetail highestRatingAdapter = null;
	private GridViewAdapterDetail newestAdapter = null;
	
	private GridViewNew tvgallery;// 评分最高
	private GridViewNew filmgallery;// 最新
	private GridViewNew mostplayedgallery;// 播放最多
	private TextView tvTitle;// “综合推荐”
	private TextView movieTitle;// “最新节目”
	private TextView mostPlayedTitle;// “最热节目”
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		appContext = (AppContext) getActivity().getApplication();
		
		mostPlayedAdapter = new GridViewAdapterDetail(getActivity(), mostPlayedList);
		highestRatingAdapter = new GridViewAdapterDetail(getActivity(), highestRatingList);
		newestAdapter =	new GridViewAdapterDetail(getActivity(), newestList);
		
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
				String webserverResp1 = HttpClientAPI.httpGet("http://124.16.138.41:8080/FlingHttpServer/RecommendListServlet?action=MOST_PLAYED");
				String webserverResp2 = HttpClientAPI.httpGet("http://124.16.138.41:8080/FlingHttpServer/RecommendListServlet?action=MOST_PLAYED");
				String webserverResp3 = HttpClientAPI.httpGet("http://124.16.138.41:8080/FlingHttpServer/RecommendListServlet?action=MOST_PLAYED");
				//String webserverResp1 = HttpClientAPI.httpGet(HttpClientAPI.makeURL(servletUrl, mostPlayedParams));
				//String webserverResp2 = HttpClientAPI.httpGet(HttpClientAPI.makeURL(servletUrl, highestParams));
				//String webserverResp3 = HttpClientAPI.httpGet(HttpClientAPI.makeURL(servletUrl, newestParams));				
				
				mostPlayedList.populateListFromString(webserverResp1);
				highestRatingList.populateListFromString(webserverResp2);
				newestList.populateListFromString(webserverResp3);	
				
				mostPlayedAdapter.notifyDataSetChanged();
				highestRatingAdapter.notifyDataSetChanged();
				newestAdapter.notifyDataSetChanged();
				
				mostPlayedList.print();
				highestRatingList.print();
				newestList.print();
			}
			
		}.start();		
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_default, container, false);
		
		tvTitle = (TextView) v.findViewById(R.id.tvtext);
		tvTitle.setText("评价最高");
		tvgallery = (GridViewNew) v.findViewById(R.id.tvgallery);
		tvgallery.setSelection(1);
		tvgallery.setNumColumns(LIST_SIZE);
		tvgallery.setAdapter(highestRatingAdapter);
		tvgallery.setOnItemClickListener(this);

		movieTitle = (TextView) v.findViewById(R.id.movietext);
		movieTitle.setText("最新上映");
		filmgallery = (GridViewNew) v.findViewById(R.id.filmgallery);
		filmgallery.setSelection(1);
		filmgallery.setNumColumns(LIST_SIZE);
		filmgallery.setAdapter(newestAdapter);
		filmgallery.setOnItemClickListener(this);

		mostPlayedTitle = (TextView) v.findViewById(R.id.most_played_text);
		mostPlayedTitle.setText("最热节目");
		mostplayedgallery = (GridViewNew) v
				.findViewById(R.id.most_played_gallery);
		mostplayedgallery.setSelection(1);
		mostplayedgallery.setNumColumns(LIST_SIZE);
		mostplayedgallery.setAdapter(mostPlayedAdapter);
		mostplayedgallery.setOnItemClickListener(this);
				
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Video video = (Video) parent.getItemAtPosition(position);
		UIRobot.showVideoDetail(getActivity(), video);		
	}
}
