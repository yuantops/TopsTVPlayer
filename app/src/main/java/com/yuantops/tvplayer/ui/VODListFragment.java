package com.yuantops.tvplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.adapter.GridViewAdapterDetail;
import com.yuantops.tvplayer.adapter.ListviewAdapter;
import com.yuantops.tvplayer.bean.Video;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by yuan on 9/4/15.
 */
public class VODListFragment extends SherlockFragment {
    private static final String TAG = VODListFragment.class.getSimpleName();
    private static JSONArray movieList;
    private static ListviewAdapter movielistAdapter;

    private ListView listViewVOD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 5; i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("videoNameCn", "hello");
                obj.put("genre", "comedy");
                obj.put("releaseDate", "2012");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(obj);
        }
        movieList = jsonArray;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_vod, container, false);
        listViewVOD = (ListView) v.findViewById(R.id.listview_vod);

        movielistAdapter = new ListviewAdapter(getActivity(), movieList);
        listViewVOD.setAdapter(movielistAdapter);
        Log.v(TAG + " >>intent from MainActivity", MainActivity.url);
        return v;
    }
}
