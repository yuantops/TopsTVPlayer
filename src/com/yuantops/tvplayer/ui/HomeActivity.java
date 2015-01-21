package com.yuantops.tvplayer.ui;

import com.yuantops.tvplayer.AppManager;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.bean.DLNABody;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class HomeActivity extends Activity {
	
	private static final String TAG = HomeActivity.class.getSimpleName();

	public void onCreate(Bundle savedInstanceState) {		
		Log.v(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		AppManager.getInstance().addActivity(this);			
	}
}
