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
public class VODListFragment extends SherlockFragment {
    private static final String TAG = VODListFragment.class.getSimpleName();
    private static JSONArray movieList = new JSONArray();
    private static final String VOD_API_SUFFIX = "/topstv/debug";
    //private static final String VOD_API_SUFFIX = "/videos";
    private static String VODApiUrl;
    private static ListviewAdapter movielistAdapter;

    private ListView listViewVOD;
    private AdapterView.OnItemClickListener clickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (VODApiUrl == null) {
            VODApiUrl = MainActivity.url + VOD_API_SUFFIX;
        }
        Log.v(TAG + " >>intent from MainActivity", MainActivity.url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_vod, container, false);
        listViewVOD = (ListView) v.findViewById(R.id.listview_vod);

        if (movieList == null || movieList.length() == 0 && VODApiUrl != null) {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, VODApiUrl, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            movieList.put((JSONObject)jsonArray.get(i));
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

        movielistAdapter = new ListviewAdapter(getActivity(), movieList);
        listViewVOD.setAdapter(movielistAdapter);

        clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String videoUrl;
                try {
                    videoUrl = ((JSONObject) movieList.get(position)).getString("standardDefiUrl");
                } catch (JSONException e) {
                    videoUrl = null;
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                Bundle intentArgs = new Bundle();
                intentArgs.putString("standardDefiUrl", videoUrl);
                intentArgs.putString("type", "VOD");
                intent.putExtras(intentArgs);
                getActivity().startActivity(intent);
            }
        };
        listViewVOD.setOnItemClickListener(clickListener);

        return v;
    }
}
