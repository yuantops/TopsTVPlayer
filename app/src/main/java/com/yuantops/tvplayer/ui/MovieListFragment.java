package com.yuantops.tvplayer.ui;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragment;
import com.yuantops.tvplayer.bean.Video;

import java.util.List;

/**
 * Created by yuan on 9/4/15.
 */
public class MovieListFragment extends SherlockFragment {
    private static String MOVIE_LIST_API;
    private static List<Video> movieList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
