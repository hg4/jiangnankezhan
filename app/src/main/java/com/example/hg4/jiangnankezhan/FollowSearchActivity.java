package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.search.AVSearchQuery;
import com.example.hg4.jiangnankezhan.Adapter.MainPageAdapter;
import com.example.hg4.jiangnankezhan.Adapter.MaterialSchAdapter;
import com.example.hg4.jiangnankezhan.Adapter.MyCmtAdapter;
import com.example.hg4.jiangnankezhan.Adapter.SechcsAdapter;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;


public class FollowSearchActivity extends BaseActivity {
	private EditText input;
	private TextView find;
	private ImageView back;
	private LoadingDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		input=(EditText)findViewById(R.id.sechcs_text);
		find=(TextView)findViewById(R.id.schcs_find);
		back=(ImageView)findViewById(R.id.sechcs_back);
        input.setHint("请输入手机号");
		find.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (Utilty.isNetworkAvailable(FollowSearchActivity.this)){
					String findText=input.getText().toString();
					if(!"".equals(findText)){
						dialog=Utilty.createDiaglog(FollowSearchActivity.this,"努力加载中...");
						AVQuery<AVObject> query = new AVQuery<>("_User");
						query.whereEqualTo("username",findText);
						query.getFirstInBackground(new GetCallback<AVObject>() {
							@Override
							public void done(AVObject avObject, AVException e) {
								{
									if (e == null) {
										if(avObject!=null){
											finish();
											Utilty.dismissDiaglog(dialog,500);
											Intent intent = new Intent(FollowSearchActivity.this, MainPageActivity.class);
											intent.putExtra("user",avObject.toString());
											startActivity(intent);
										}else{
											Utilty.dismissDiaglog(dialog,500);
											Toast.makeText(FollowSearchActivity.this,"此用户不存在",Toast.LENGTH_SHORT).show();
										}

									} else {
										e.printStackTrace();
										Utilty.dismissDiaglog(dialog,500);
										Toast.makeText(FollowSearchActivity.this,"此用户不存在",Toast.LENGTH_SHORT).show();
									}
								}
							}
						});
					}
				}else{
					Toast.makeText(FollowSearchActivity.this, "当前网络不可用", Toast.LENGTH_SHORT).show();
				}
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FollowSearchActivity.this.finish();
			}
		});
	}
}
