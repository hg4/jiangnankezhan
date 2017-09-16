package com.example.hg4.jiangnankezhan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by HG4 on 2017/8/20.
 */

public class BaseActivity extends AppCompatActivity {
	protected String id= AVUser.getCurrentUser().getObjectId();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImmersionBar.with(this).transparentStatusBar().init();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
	}
}
