package com.yuantops.tvplayer.ui;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yuantops.tvplayer.AppManager;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.bean.Video;
import com.yuantops.tvplayer.util.UIRobot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoDisplayActivity extends Activity {
	private static final String TAG = VideoDisplayActivity.class.getSimpleName();
	
	ImageView dis_videopicture;
	Button dis_playbutton;
	TextView dis_videoname , dis_playtimes ,dis_releasedate, dis_duringtime , dis_category , dis_introduction;

	/*private DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.vlc)
			.showImageForEmptyUri(R.drawable.vlc)
			.showImageOnFail(R.drawable.vlc).cacheInMemory(true)
			.cacheOnDisk(true).build();*/
	private ImageLoader loader = ImageLoader.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_videodisplay);
		AppManager.getInstance().addActivity(this);
		
		Intent intent = this.getIntent();
		final Video item = (Video) intent.getSerializableExtra("video");
				
		dis_videopicture = (ImageView) findViewById(R.id.dis_videopicture);
		dis_playbutton = (Button) findViewById(R.id.dis_playbutton);
		dis_videoname = (TextView) findViewById(R.id.dis_videoname);
		dis_playtimes = (TextView) findViewById(R.id.dis_playtimes);
		dis_releasedate = (TextView) findViewById(R.id.dis_releasedate);
		dis_duringtime = (TextView) findViewById(R.id.dis_duringtime);
		dis_category = (TextView) findViewById(R.id.dis_category);
		dis_introduction = (TextView) findViewById(R.id.dis_introduction);

		dis_videoname.setText(item.getVideoName_cn());
		//dis_playtimes.setText(String.valueOf(playtimes));
		dis_releasedate.setText(item.getReleaseDate());
		dis_duringtime.setText(item.getRuntime());
		dis_category.setText(item.getCategory());
		dis_introduction.setText(item.getIntroduction());		

		loader.init(ImageLoaderConfiguration.createDefault(this));
		loader.displayImage(item.getPosterUrl(), dis_videopicture);
		
		dis_playbutton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.v(TAG + " >video info", item.toString());
				//UIRobot.enterChatroom(VideoDisplayActivity.this);
				UIRobot.openVideoPlayer(VideoDisplayActivity.this, item);
			}			
		});
	}
}
