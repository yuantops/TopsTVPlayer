package com.yuantops.tvplayer.ui;

import com.actionbarsherlock.app.SherlockFragment;
import com.yuantops.tvplayer.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DefaultFragment extends SherlockFragment {
	private static final String TAG = DefaultFragment.class.getSimpleName();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		
		Log.v(TAG, "onCreate()");
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.activity_main, container, false);
		return v;
	}
}
