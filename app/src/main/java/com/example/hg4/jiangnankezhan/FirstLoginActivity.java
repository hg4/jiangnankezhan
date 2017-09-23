package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVUser;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.gyf.barlibrary.ImmersionBar;

public class FirstLoginActivity extends AppCompatActivity {
	private Button go;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_login);
		ImmersionBar.with(this).transparentBar().init();
		go=(Button)findViewById(R.id.firstlog_button);
		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(FirstLoginActivity.this,ListGradeActivity.class),1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=0){
			switch (requestCode){
				case 1:
					startActivityForResult(new Intent(FirstLoginActivity.this,ListDiaglogActivity.class),2);
					break;
				case 2:startActivity(new Intent(FirstLoginActivity.this,MainActivity.class));
					PerferencesUtils.saveState(FirstLoginActivity.this, AVUser.getCurrentUser().getObjectId(),false);
					this.finish();
			}
		}
	}
}
