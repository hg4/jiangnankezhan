package com.example.hg4.jiangnankezhan.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.avos.avoscloud.AVUser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by HG4 on 2017/9/6.
 */

public class PerferencesUtils {
	/**
	 * 保存是否第一次进入应用的状态值
	 */
	public static void saveState(Context context, String id,boolean isFirstLogin) {
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();
		editor.putBoolean("first login", isFirstLogin);
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
	public static void saveBitmapToSharedPreferences(Bitmap bitmap,String id, Context context){
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
		byte[] byteArray=byteArrayOutputStream.toByteArray();
		String imageString=new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
		SharedPreferences sharedPreferences=context.getSharedPreferences(id+"userdata", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString("image", imageString);
		editor.commit();
	}
	public static Bitmap getBitmapFromSharedPreferences(String id,Context context){
		SharedPreferences sharedPreferences=context.getSharedPreferences(id+"userdata", Context.MODE_PRIVATE);
		String imageString=sharedPreferences.getString("image", "");
		byte[] byteArray=Base64.decode(imageString, Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
		Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
		return bitmap;
	}
}
