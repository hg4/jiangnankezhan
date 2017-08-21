package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gyf.barlibrary.ImmersionBar;

public class RegisterOKActivity extends AppCompatActivity {
	private Button enter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_ok);
		ImmersionBar.with(this).transparentBar().init();
		enter=(Button)findViewById(R.id.enter);
		enter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegisterOKActivity.this,MainActivity.class));
				RegisterOKActivity.this.finish();
			}
		});
	}
}
