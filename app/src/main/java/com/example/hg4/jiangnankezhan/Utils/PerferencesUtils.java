package com.example.hg4.jiangnankezhan.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVUser;

/**
 * Created by HG4 on 2017/9/6.
 */

public class PerferencesUtils {
	/**
	 * 保存是否第一次进入应用的状态值
	 */
	public static void saveState(Context context, String id,boolean isFirstOpen) {
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();
		editor.putBoolean("first login", isFirstOpen);
		editor.commit();
	}

	/**
	 * 获取是否第一次进入应用的状态值
	 */
	public static boolean getState(Context context,String id) {
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		return share.getBoolean("first login", true);
	}

	//根据用户id创建私人preferences并保存信息
	public static void saveUserStringData(Context context,String id,String key,String value){
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=share.edit();
		editor.putString(key,value);
		editor.apply();
	}

	public static String getUserStringData(Context context,String id,String key){
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		String value=share.getString(key,"");
		return value;
	}

}
