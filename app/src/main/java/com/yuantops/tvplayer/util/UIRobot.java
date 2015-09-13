package com.yuantops.tvplayer.util;

import android.os.Bundle;
import com.yuantops.tvplayer.ui.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * 处理与UI相关的工具类，包括Activity跳转、显示Dialog、显示Toast
 * @author admin (Email: yuan.tops@gmail.com)
 * @date 2015-1-7
 */
public class UIRobot {
	public static final String TAG = UIRobot.class.getSimpleName();

	public static void gotoMainPage(Context context, String url) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        intent.putExtras(bundle);
//		intent.putExtra("url", url);
        context.startActivity(intent);
		((Activity)context).finish();
    }

	/**
	 * 显示提示信息
	 * @param mContext
	 * @param string
	 */
	public static void showToast(Context mContext, String string) {
		if(!StringUtils.isEmpty(string)) {
			Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
		}
	} 
}
