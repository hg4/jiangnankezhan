package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CosContentActivity extends BaseActivity implements View.OnClickListener{
	private String courseName;
	private String teacher;
	private Intent intent;
	private Button addcmt;
	CommentFragment commentFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cos_content);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar=getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.back_icon);
		setTitle("课程内容");
		setTitleColor(R.color.colorDarkbule);
		addcmt=(Button)findViewById(R.id.addcmt_comment);
		addcmt.setOnClickListener(this);
		intent=getIntent();
		courseName=intent.getStringExtra("courseName");
		teacher=intent.getStringExtra("teacher");
		commentFragment=createFragment(3);
		//将新建的fragment添加到activity的layout中
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.cos_content_layout,commentFragment);
		transaction.commit();
	}
	private CommentFragment createFragment(int type){
		CommentFragment fragment=new CommentFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("type",type);
		bundle.putString("courseName",courseName);
		bundle.putString("teacher",teacher);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.addcmt_comment:
				Intent intent=new Intent(CosContentActivity.this,CommentActivity.class);
				intent.putExtra("type",3);
				intent.putExtra("courseName",courseName);
				intent.putExtra("teacher",teacher);
				intent.putExtra("from",3);
				startActivityForResult(intent,1);
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home: //对用户按home icon的处理，本例只需关闭activity，就可返回上一activity，即主activity。
				finish();
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=0){
			switch (requestCode){
				case 1:
					commentFragment.autoRefresh();
					break;
			}
		}
	}

}
