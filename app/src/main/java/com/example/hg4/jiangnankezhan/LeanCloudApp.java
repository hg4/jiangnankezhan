package com.example.hg4.jiangnankezhan;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.im.v2.AVIMClient;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import cn.leancloud.chatkit.LCChatKit;

/**
 * Created by HG4 on 2017/8/20.
 */

public class LeanCloudApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		LitePalApplication.initialize(getApplicationContext());
		Connector.getDatabase();
		AVOSCloud.initialize(this,"0B9els8IjWUmWWRsF2pvUlXn-gzGzoHsz", "WLop4joASgeTn9pl3KEDECGA");
		AVOSCloud.setDebugLogEnabled(true);
		LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
		LCChatKit.getInstance().init(getApplicationContext(), "0B9els8IjWUmWWRsF2pvUlXn-gzGzoHsz","WLop4joASgeTn9pl3KEDECGA");
		AVIMClient.setAutoOpen(true);
		PushService.setDefaultPushCallback(this,MainActivity.class);
		PushService.setAutoWakeUp(true);
	}
}
