package com.yuantops.tvplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.adapter.ListviewAdapter;
import com.yuantops.tvplayer.util.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuan on 9/4/15.
 */
public class LiveListFragment extends SherlockFragment {
    private static final String TAG = LiveListFragment.class.getSimpleName();
    private static JSONArray liveList = new JSONArray();
    private static final String LIVE_API_SUFFIX = "/topstv/debug";
    //private static final String LIVE_API_SUFFIX = "/videos";
    private static String LiveApiUrl;
    private static ListviewAdapter movielistAdapter;

    private ListView listViewLive;
    private AdapterView.OnItemClickListener clickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LiveApiUrl == null) {
            LiveApiUrl = MainActivity.url + LIVE_API_SUFFIX;
        }
        Log.v(TAG + " >>intent from MainActivity", MainActivity.url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_vod, container, false);
        listViewLive = (ListView) v.findViewById(R.id.listview_vod);

        if (liveList == null || liveList.length() == 0 && LiveApiUrl != null) {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, LiveApiUrl, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            liveList.put((JSONObject)jsonArray.get(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    movielistAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e(TAG + " >>>response error", "  ");
                    volleyError.printStackTrace();
                }
            });
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
        }

        movielistAdapter = new ListviewAdapter(getActivity(), liveList);
        listViewLive.setAdapter(movielistAdapter);

        clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoUrl;
                try {
                    videoUrl = ((JSONObject) liveList.get(position)).getString("broadcastUrl");
                } catch (JSONException e) {
                    videoUrl = null;
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle intentArgs = new Bundle();
                intentArgs.putString("broadcastUrl", videoUrl);
                intentArgs.putString("type", "LIVE");
                intent.putExtras(intentArgs);
                getActivity().startActivity(intent);
            }
        };
        listViewLive.setOnItemClickListener(clickListener);

        return v;
    }
}
