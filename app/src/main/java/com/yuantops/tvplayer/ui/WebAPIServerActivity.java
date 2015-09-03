package com.yuantops.tvplayer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.yuantops.tvplayer.R;
import com.yuantops.tvplayer.util.StringUtils;
import com.yuantops.tvplayer.util.UIRobot;

/**
 * Created by yuan on 9/3/15.
 */
public class WebAPIServerActivity extends Activity implements OnClickListener {
    private static final String TAG = WebAPIServerActivity.class.getName();

    private EditText webServerIPEdtx;
    private Button   enterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_server);
        webServerIPEdtx = (EditText) findViewById(R.id.web_api_server_ip);
        enterBtn        = (Button) findViewById(R.id.web_server_enter);
        enterBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        String edtxContent = webServerIPEdtx.getText().toString();
        if (StringUtils.isEmpty(edtxContent)) {
            return;
        }
        Log.d(TAG, "Valid url, jump to MainPage");
        UIRobot.gotoMainPage(this);
    }
}
