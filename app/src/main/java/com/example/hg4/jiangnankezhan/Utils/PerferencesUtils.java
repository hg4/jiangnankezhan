package com.example.hg4.jiangnankezhan.Utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2017/9/6.
 */

public class PerferencesUtils {

	public static void saveFirstOp(Context context,boolean isFirstOp) {
		SharedPreferences share= PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = share.edit();
		editor.putBoolean("first open", isFirstOp);
		editor.commit();
	}

	/**
	 * 获取是否第一次进入登录的状态值
	 */
	public static boolean getFirstOp(Context context) {
		SharedPreferences share=PreferenceManager.getDefaultSharedPreferences(context);
		return share.getBoolean("first open", true);
	}
	/**
	 * 保存是否第一次进入登录的状态值
	 */
	public static void saveState(Context context, String id,boolean isFirstLogin) {
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();
		editor.putBoolean("first login", isFirstLogin);
		editor.commit();
	}
	public static void saveState(Context context,String id,int viewId,boolean isShow){
		SharedPreferences share=context.getSharedPreferences(id+"userdata"+viewId,Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();
		editor.putBoolean("isShow", isShow);
		editor.commit();
	}
	public static boolean getState(Context context,String id,int viewId) {
		SharedPreferences share=context.getSharedPreferences(id+"userdata"+viewId,Context.MODE_PRIVATE);
		return share.getBoolean("isShow", true);
	}
	/**
	 * 获取是否第一次进入登录的状态值
	 */
	public static boolean getState(Context context,String id) {
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		return share.getBoolean("first login", true);
	}
	public static void savePermission(Context context, String id,boolean isFirstPermiss) {
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = share.edit();
		editor.putBoolean("first permiss", isFirstPermiss);
		editor.commit();
	}
	public static boolean getPermission(Context context,String id) {
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		return share.getBoolean("first permiss", true);
	}
	//根据用户id创建私人preferences并保存信息
	public static void saveUserStringData(Context context,String id,String key,String value){
		SharedPreferences share=context.getSharedPreferences(id+"userdata",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=share.edit();
		editor.putString(key,value);
		editor.apply();
	}
	public static void saveUserIntData(Context context,String id,String key,int value){
		SharedPreferences share=context.getSharedPreferences(id+"userdata1",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=share.edit();
		editor.putInt(key,value);
		editor.apply();
	}
	public static int getUserIntData(Context context,String id,String key){
		SharedPreferences share=context.getSharedPreferences(id+"userdata1",Context.MODE_PRIVATE);
		int value=share.getInt(key,0);
		return value;
	}
	public static void saveUserLongData(Context context,String id,String key,long value){
		SharedPreferences share=context.getSharedPreferences(id+"userdata1",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=share.edit();
		editor.putLong(key,value);
		editor.apply();
	}
	public static long getUserLongData(Context context,String id,String key){
		SharedPreferences share=context.getSharedPreferences(id+"userdata1",Context.MODE_PRIVATE);
		long value=share.getLong(key,0);
		return value;
	}
	public static  void saveDataList(Context context,String id,String key,List<String> datalist) {

		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(datalist);
		SharedPreferences share=context.getSharedPreferences(id+"userdata1",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=share.edit();
		if (null == datalist || datalist.size() <= 0){
			editor.clear();
			editor.commit();
			return;
		}

		editor.clear();
		editor.putString(key, strJson);
		editor.commit();

	}

	public static List<String> getDataList(Context context,String id,String key) {
		List<String> datalist=new ArrayList<>();
		SharedPreferences share=context.getSharedPreferences(id+"userdata1",Context.MODE_PRIVATE);
		String strJson = share.getString(key, null);
		if (null == strJson) {
			return datalist;
		}
		Gson gson = new Gson();
		datalist = gson.fromJson(strJson, new TypeToken<List<String>>() {
		}.getType());
		return datalist;

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
