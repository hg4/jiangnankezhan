package com.example.hg4.jiangnankezhan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.avos.avoscloud.AVObject;
import com.example.hg4.jiangnankezhan.Adapter.ReplyAdapter;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class ReplyActivity extends BaseActivity {
	private List<AVObject> list=new ArrayList<>();
	private List<AVObject> replyList=new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar=getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.back_icon);
		initView();
	}
	private void initView(){
		ReplyAdapter replyAdapter=new ReplyAdapter(list);
		RecyclerFragment fragment=RecyclerFragment.newInstance(replyAdapter,list,new Bundle());
		FragmentManager fManager=getSupportFragmentManager();
		FragmentTransaction fTransaction = fManager.beginTransaction();
		fTransaction.add(R.id.fg_layout,fragment);
		fTransaction.commit();
	/*	if(getIntent().getStringArrayListExtra("reply")!=null){
			List<String> stringlist=getIntent().getStringArrayListExtra("reply");
			for(String str:stringlist){
				try{
					AVObject avObject=AVObject.parseAVObject(str);
					if(avObject!=null)
						replyList.add(avObject);
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
		}*/
		List<String> stringlist=PerferencesUtils.getDataList(this,id,"replylist");
		for(String str:stringlist) {
			try {
				AVObject avObject = AVObject.parseAVObject(str);
				if (avObject != null)
					replyList.add(avObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fragment.commentList=replyList;
		fragment.loadMoreComment();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home: //对用户按home icon的处理，本例只需关闭activity，就可返回上一activity，即主activity。
				finish();
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
