package com.example.hg4.jiangnankezhan;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.gyf.barlibrary.ImmersionBar;
import java.util.Timer;
import java.util.TimerTask;

public class OpenActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open);
		ImmersionBar.with(this).transparentBar().init();
		Timer timer = new Timer();//实例化Timer类
		timer.schedule(new TimerTask() {
			public void run() {
				startActivity(new Intent(OpenActivity.this, LoginActivity.class));
				this.cancel();
				OpenActivity.this.finish();
			}
		}, 2000);//五百毫秒
	}
}
