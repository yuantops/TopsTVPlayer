package com.yuantops.tvplayer.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.bean.Video;
import com.yuantops.tvplayer.bean.VideoList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapterDetail extends BaseAdapter{
	private static final String TAG = GridViewAdapterDetail.class.getSimpleName();
	
	private Context mContext;
	private VideoList mList;

	private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.vlc)
			.showImageForEmptyUri(R.drawable.vlc)
			.showImageOnFail(R.drawable.vlc).cacheInMemory(true)
			.cacheOnDisk(true).build();
	private ImageLoader loader = ImageLoader.getInstance();
	
	public GridViewAdapterDetail (Context context, VideoList list) {
		this.mContext = context;
		this.mList = list;
	}
	
	@Override
	public int getCount() {
		return mList.getSize();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.getVideo(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.video, null);

			vh = new ViewHolder();
			vh.videoicon = (ImageView) convertView.findViewById(R.id.videoicon);
			vh.videoname = (TextView) convertView.findViewById(R.id.videoname);
			vh.videoplaytimes = (TextView) convertView
					.findViewById(R.id.videoplaytimes);
			vh.videoreleasedate = (TextView) convertView
					.findViewById(R.id.video_release_date);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		Video video = mList.getVideo(position);
		if (video!=null) {
			vh.videoname.setText(video.getVideoName_cn());
			vh.videoplaytimes.setText("播放:" + String.valueOf(video.getPlayedTimes()));
			vh.videoreleasedate.setText("上映:" + video.getReleaseDate());
			loader.displayImage(video.getPosterUrl(), vh.videoicon, options);
		}
		
		return convertView;
	}
	
	class ViewHolder {
		ImageView videoicon;
		TextView videoname;
		TextView videoplaytimes;
		TextView videoreleasedate;
	}

}
