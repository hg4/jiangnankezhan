package com.example.hg4.jiangnankezhan;

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
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.search.AVSearchQuery;
import com.example.hg4.jiangnankezhan.Adapter.MaterialSchAdapter;
import com.example.hg4.jiangnankezhan.Adapter.MyCmtAdapter;
import com.example.hg4.jiangnankezhan.Adapter.SechcsAdapter;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity implements OnLoadMoreListener{
	private EditText input;
	private TextView find;
	private RecyclerView recyclerView;
	private List<AVObject> searchList=new ArrayList<>();
	private List<AVObject> displaySearchList=new ArrayList<>();
	private ImageView back;
	private SwipeToLoadLayout swipeToLoadLayout;
	private LoadingDialog dialog;
	private String className;
	private Bundle condition;
	private RecyclerView.Adapter adapter;
	private int adapterType;
	private int conditionType;
	private int index=ORI;
	private static final int INCREMENT=3;
	private static final int ORI=4;
	private static final int CONDITION_CSFILE=2;
	private static final int CONDITION_MYCMT=1;
	private static final int ADAPTER_CS=0;
	private static final int ADAPTER_MYCMT=1;
	private static final int ADAPTER_CSFILE=2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		className=getIntent().getStringExtra("findname");
		adapterType=getIntent().getIntExtra("adaptertype",0);
		if(getIntent().getBundleExtra("condition")!=null){
			//若有查询条件 设置
			condition=getIntent().getBundleExtra("condition");
			conditionType=condition.getInt("conditiontype");
		}
		input=(EditText)findViewById(R.id.sechcs_text);
		find=(TextView)findViewById(R.id.schcs_find);
		back=(ImageView)findViewById(R.id.sechcs_back);
		swipeToLoadLayout=(SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
		swipeToLoadLayout.setOnLoadMoreListener(this);
		recyclerView=(RecyclerView)findViewById(R.id.swipe_target);
		LinearLayoutManager layoutManager=new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		setRecyclerAdapter();
		find.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (Utilty.isNetworkAvailable(SearchActivity.this)){
					String findText=input.getText().toString();
					if(!"".equals(findText)){
						dialog=Utilty.createDiaglog(SearchActivity.this,"努力加载中...");
						AVSearchQuery<AVObject> avSearchQuery=new AVSearchQuery<AVObject>();
						avSearchQuery.setClassName(className);
						avSearchQuery.setQueryString(findText);
						avSearchQuery.findInBackgroud(new FindCallback<AVObject>() {
							@Override
							public void done(List<AVObject> list, AVException e) {
								if(e==null){
									if(searchList.size()!=0){
										searchList.clear();
										index=ORI;
									}
									if(list.size()!=0){
										Log.e("test",list.toString());
										//传递来的查询条件,必须为objectId;
										if(condition!=null){
											subData(list);
										}
										else {
											searchList=list;
										}

										loadMoreData();
										Utilty.dismissDiaglog(dialog,1000);
									}
									else {
										Utilty.dismissDiaglog(dialog,500);
									}
								}
								else {
									Utilty.dismissDiaglog(dialog,500);
								}
							}
						});
					}
				}else{
					Toast.makeText(SearchActivity.this, "当前网络不可用", Toast.LENGTH_SHORT).show();
				}
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.this.finish();
			}
		});
	}
	private void loadMoreData(){
		if(searchList.size()>index){
			displaySearchList.clear();
			displaySearchList.addAll(searchList.subList(0,index));
			index=index+INCREMENT;
		}
		else {
			index=searchList.size();
			displaySearchList.clear();
			displaySearchList.addAll(searchList.subList(0,index));
			swipeToLoadLayout.setLoadMoreEnabled(false);
		}
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onLoadMore() {
		swipeToLoadLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setLoadingMore(false);
				loadMoreData();
			}
		},1000);
	}
	private void setRecyclerAdapter(){
		switch (adapterType){
			case ADAPTER_CS:
				adapter=new SechcsAdapter(displaySearchList);
				recyclerView.setAdapter(adapter);
				break;
			case ADAPTER_MYCMT:
				adapter=new MyCmtAdapter(displaySearchList);
				recyclerView.setAdapter(adapter);
				break;
			case ADAPTER_CSFILE:
				adapter=new MaterialSchAdapter(displaySearchList);
				recyclerView.setAdapter(adapter);
				break;
			default:
				break;
		}
	}
	private void subData(List<AVObject> list){
		List<AVObject> sublist=new ArrayList<>();
		switch (conditionType){
			case CONDITION_MYCMT:
				String id=condition.getString("objectid");
				for(AVObject data:list){
					AVObject from=data.getAVObject("from");
					if(id.equals(from.getObjectId())){
						sublist.add(data);
					}
				}
				searchList=sublist;
				break;
			case CONDITION_CSFILE:
				for(AVObject data:list){
					String courseName=data.getString("Course");
					String getname=condition.getString("courseName");
					if(getname!=null){
						if(getname.equals(courseName)){
							sublist.add(data);
						}
					}
				}
				searchList=sublist;
				break;
		}
	}

}
