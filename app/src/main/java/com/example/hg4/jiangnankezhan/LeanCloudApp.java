package com.example.hg4.jiangnankezhan;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by HG4 on 2017/8/20.
 */

public class LeanCloudApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		AVOSCloud.initialize(this,"0B9els8IjWUmWWRsF2pvUlXn-gzGzoHsz", "WLop4joASgeTn9pl3KEDECGA");
		AVOSCloud.setDebugLogEnabled(true);
	}
}
