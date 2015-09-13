package com.yuantops.tvplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yuantops.tvplayer.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yuan on 9/5/15.
 */
public class ListviewAdapter extends BaseAdapter {
    private static final String TAG = ListviewAdapter.class.getSimpleName();

    private Context mCtx;
    private JSONArray mJsonArr;

    public ListviewAdapter(Context context, JSONArray jsonArray) {
        mCtx = context;
        mJsonArr = jsonArray;
    }

    public int getCount() {
        return mJsonArr.length();
    }

    @Override
    public Object getItem(int position) {
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) mJsonArr.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_video, null);

            vh = new ViewHolder();
            vh.txtVwName  = (TextView) convertView.findViewById(R.id.video_name_view);
            vh.txtVwGenre = (TextView) convertView.findViewById(R.id.video_genre);
            vh.txtVwDate  = (TextView) convertView.findViewById(R.id.video_date);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonItem = (JSONObject) getItem(position);
        if (jsonItem != null) {
            try {
                vh.txtVwName.setText((String) jsonItem.get("videoNameCn"));
                vh.txtVwGenre.setText((String) jsonItem.get("genre"));
                vh.txtVwDate.setText((String) jsonItem.get("releaseDate"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView txtVwName;
        TextView txtVwGenre;
        TextView txtVwDate;
    }
}
