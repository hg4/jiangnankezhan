package com.example.hg4.jiangnankezhan.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.LoadingDialog;
import com.example.hg4.jiangnankezhan.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by HG4 on 2017/8/30.
 */

public class Utilty {
	private static HttpURLConnection urlConnection;
	public  static boolean nameIsValid(String name){
		return true;
	}
	public static boolean phoneIsValid(String phone){return true;}
	public static boolean emailIsValid(String email){return true;}
	 /* 把Bitmap转Byte
     */
	public static byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public  static LoadingDialog createDiaglog(Context context,String message){
		LoadingDialog dialog=new LoadingDialog(context);
		dialog.setMessage(message).show();
		return dialog;
	}
	public static void dismissDiaglog(Dialog dialog,int delay){
		try{
			Thread.sleep(delay);
			dialog.dismiss();
		}
		catch (Exception e1){
			e1.printStackTrace();
		}
	}
	public static android.support.v7.app.AlertDialog imgDetailDialog(final Context mContext, final String url){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
		final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext,R.style.Dialog_Fullscreen).create();
		ImageView img = (ImageView)imgEntryView.findViewById(R.id.large_image);
		img.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				View contentView = LayoutInflater.from(mContext).inflate(R.layout.popwindow_img, null);
				final PopupWindow mPopWindow = new PopupWindow(contentView,
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
				mPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
				mPopWindow.setContentView(contentView);
				mPopWindow.setAnimationStyle(R.style.contextMenuAnim);
				//设置各个控件的点击响应
				TextView save = (TextView)contentView.findViewById(R.id.pop_save);
				TextView cancel = (TextView)contentView.findViewById(R.id.pop_cancel);
				save.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						getBitmap(mContext,url);

						mPopWindow.dismiss();
					}
				});
				cancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mPopWindow.dismiss();
					}
				});
				//显示PopupWindow
				mPopWindow.showAtLocation(imgEntryView,Gravity.BOTTOM,0,0);
				return true;
			}
		});
		Glide.with(imgEntryView.getContext()).load(url).placeholder(R.drawable.placeholder).into(img);
		dialog.setView(imgEntryView);
		dialog.show();
		return dialog;
	}
	public static android.support.v7.app.AlertDialog leadDialog(final Context mContext,int drawableId){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null); // 加载自定义的布局文件
		final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(mContext,R.style.Dialog_Fullscreen).create();
		ImageView img = (ImageView)imgEntryView.findViewById(R.id.large_image);

		img.setScaleType(ImageView.ScaleType.CENTER_CROP);
		img.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dialog.dismiss();
				return true;
			}
		});
		img.setImageResource(drawableId);
		dialog.setView(imgEntryView);
		dialog.show();
		return dialog;
	}
	public static void getBitmap(final Context context, final String imageUrl) {
		AsyncTask asyncTask=new AsyncTask() {
			@Override
			protected Object doInBackground(Object[] params) {
				try {
					URL url1 = new URL(imageUrl);
					urlConnection = (HttpURLConnection) url1.openConnection();
					urlConnection.setReadTimeout(5000);
					urlConnection.setConnectTimeout(5000);
					urlConnection.setRequestMethod("GET");
					if (urlConnection.getResponseCode() == 200) {
						InputStream inputStream = urlConnection.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						//将图片保存到本地
						savePic2Phone(context, bitmap);
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					urlConnection.disconnect();
				}
				return 0;
			}
		};
		asyncTask.execute();
	}

	private static void savePic2Phone(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(), "jiangnankezhan");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		Log.e("test","ok");
	}

	public static void showNetworkFail(Context context,ViewGroup holder){
		ImageView error=new ImageView(context);
		 error.setImageResource(R.drawable.net_error);
		 error.setScaleType(ImageView.ScaleType.CENTER_CROP);
		 error.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		 holder.removeAllViews();
		 holder.addView(error);
	}
}
