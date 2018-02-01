package com.example.hg4.jiangnankezhan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.hg4.jiangnankezhan.Adapter.AdapterFragment;
import com.example.hg4.jiangnankezhan.Adapter.MyCmtAdapter;
import com.example.hg4.jiangnankezhan.Adapter.MyMaterialAdapter;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;

public class MyCommentActivity  extends BaseActivity implements ViewPager.OnPageChangeListener{
	private TabLayout layoutTab;
	private ViewPager viewpagerTab;
	private List<AVObject> myCmtList=new ArrayList<>();
	private List<AVObject> myMaterList=new ArrayList<>();
	private String[] stringList = new String[]{"评价","资料"};
	private List<Fragment> fragmentList=new ArrayList<>();
	private AdapterFragment adapterFragment;
	private ImageButton search;
	private RecyclerFragment recyclerFragment;
	private RecyclerFragment recyclerFragment1;
	private List<AVObject> displayList=new ArrayList<>();
	private List<AVObject> MdisplayList=new ArrayList<>();
	private MyMaterialAdapter myMaterialAdapter=new MyMaterialAdapter(MdisplayList);
	private MyCmtAdapter cmtAdapter=new MyCmtAdapter(displayList);
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_comment);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar=getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeAsUpIndicator(R.drawable.back_icon);
		initView();
		initData();
		getCommentData();
	}
	private void getCommentData(){
		final AVObject user=AVObject.createWithoutData("_User",id);
		AVQuery<AVObject> query=new AVQuery<>("Course_comment");
		query.whereEqualTo("from",user);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(list!=null){
					myCmtList.clear();
					myCmtList.addAll(list);
					recyclerFragment.commentList=myCmtList;
					recyclerFragment.loadMoreComment();

				}
			}
		});
		AVQuery<AVObject> mQuery=new AVQuery<>("Course_file");
		mQuery.whereEqualTo("owner",user);
		mQuery.include("owner");
		mQuery.orderByDescending("createdAt");
		mQuery.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(list!=null){
					myMaterList.clear();
					myMaterList.addAll(list);
					recyclerFragment1.commentList=myMaterList;
					recyclerFragment1.loadMoreComment();
					Utilty.dismissDiaglog(dialog,1000);
				}
			}
		});


	}
	private void initView() {
		layoutTab = (TabLayout) findViewById(R.id.mycmt_layoutTab);
		viewpagerTab = (ViewPager) findViewById(R.id.mycmt_viewpager);
		search=(ImageButton)findViewById(R.id.mycmt_sech);
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MyCommentActivity.this,SearchActivity.class);
				intent.putExtra("findname","Course_comment");
				intent.putExtra("adaptertype",1);
				Bundle bundle=new Bundle();
				bundle.putInt("conditiontype",1);
				bundle.putString("objectid",id);
				intent.putExtra("condition",bundle);
				startActivity(intent);
			}
		});
		dialog=Utilty.createDiaglog(this,"努力加载中...");
		Bundle bundle1=new Bundle();
		bundle1.putInt("close",1);
		recyclerFragment=RecyclerFragment.newInstance(cmtAdapter,displayList,bundle1);
		fragmentList.add(recyclerFragment);
		recyclerFragment1=RecyclerFragment.newInstance(myMaterialAdapter,MdisplayList,bundle1);
		fragmentList.add(recyclerFragment1);
		if(!Utilty.isNetworkAvailable(this)){
			Utilty.dismissDiaglog(dialog,1000);
			Toast.makeText(this,"网络连接不可用",Toast.LENGTH_SHORT).show();
		}
	}

	private void initData() {
		adapterFragment= new AdapterFragment(getSupportFragmentManager(),fragmentList,stringList);
		viewpagerTab.setAdapter(adapterFragment);
		viewpagerTab.setOffscreenPageLimit(3);
		viewpagerTab.addOnPageChangeListener(this);
//        代码中优先级高于xml
//        layoutTab.setTabMode(TabLayout.MODE_SCROLLABLE);
		layoutTab.setupWithViewPager(viewpagerTab);
		layoutTab.setTabsFromPagerAdapter(adapterFragment);
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
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
}
