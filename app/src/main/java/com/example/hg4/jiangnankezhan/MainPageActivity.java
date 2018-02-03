package com.example.hg4.jiangnankezhan;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.Adapter.AdapterFragment;
import com.example.hg4.jiangnankezhan.Adapter.BaseinfoAdapter;
import com.example.hg4.jiangnankezhan.Adapter.MainPageAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainPageActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
	private String[] stringList = new String[]{"动态","关于TA"};
	private List<Fragment> fragmentList=new ArrayList<>();
	private List<AVObject> myCmtList=new ArrayList<>();
	private AdapterFragment adapterFragment;
	private RecyclerFragment recyclerFragment;
	private ViewPager viewpagerTab;
	private TabLayout layoutTab;
	private List<AVObject> displayList=new ArrayList<>();
	private MainPageAdapter mainPageAdapter=new MainPageAdapter(displayList);
	private AVObject aimUser;
	private ImageView head;
	private TextView aimUserName;
	private TextView aimUserCollege;
	private RecyclerFragment baseinfoFragment;
	private List<AVObject> baseDisplayList=new ArrayList<>();
	private BaseinfoAdapter baseinfoAdapter=new BaseinfoAdapter(baseDisplayList);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);
		Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar=getSupportActionBar();
		if(actionBar!=null)
			actionBar.setDisplayHomeAsUpEnabled(true);
		CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
		head=(ImageView)findViewById(R.id.info_imageView);
		aimUserName=(TextView)findViewById(R.id.info_nickname);
		aimUserCollege=(TextView)findViewById(R.id.info_college);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		if(getIntent().getStringExtra("user")!=null){
			try{
				aimUser=AVObject.parseAVObject(getIntent().getStringExtra("user"));
				baseDisplayList.add(aimUser);
				aimUserName.setText(aimUser.getString("nickname"));
				aimUserCollege.setText(aimUser.getString("college"));
				if (aimUser.getAVFile("head") != null) {
					AVFile file = aimUser.getAVFile("head");
					if (file != null && file.getUrl() != null) {
						Glide.with(this).load(file.getUrl()).into(head);
					}
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}

		viewpagerTab=(ViewPager) findViewById(R.id.pageviewpager);
		layoutTab=(TabLayout) findViewById(R.id.tablayout);
		Bundle bundle1=new Bundle();
		bundle1.putInt("close",1);
		Bundle bundle2=new Bundle();
		bundle2.putInt("close",3);
		recyclerFragment=RecyclerFragment.newInstance(mainPageAdapter,displayList,bundle1);
		baseinfoFragment=RecyclerFragment.newInstance(baseinfoAdapter,baseDisplayList,bundle2);
		fragmentList.add(recyclerFragment);
		fragmentList.add(baseinfoFragment);
		initData();
		getCommentData();
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
	private void getCommentData(){
		AVQuery<AVObject> avQuery1=new AVQuery<>("Course_comment");
		avQuery1.whereEqualTo("from",aimUser);
		avQuery1.include("from");
		avQuery1.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(list!=null){
					myCmtList.clear();
					myCmtList.addAll(list);
				}
				AVQuery<AVObject> avQuery2=new AVQuery<>("Course_file");
				avQuery2.whereEqualTo("owner",aimUser);
				avQuery2.include("owner");
				avQuery2.findInBackground(new FindCallback<AVObject>() {
					@Override
					public void done(List<AVObject> list, AVException e) {
						if(list!=null){
							myCmtList.addAll(list);
							Collections.sort(myCmtList, new Comparator<AVObject>() {
								@Override
								public int compare(AVObject o1, AVObject o2) {
									if(o1.getCreatedAt().getTime()>o2.getCreatedAt().getTime())
										return -1;
									else return 1;
								}
							});
							recyclerFragment.commentList=myCmtList;
							recyclerFragment.loadMoreComment();
						}
					}
				});
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case android.R.id.home:
				finish();
				return true;
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
