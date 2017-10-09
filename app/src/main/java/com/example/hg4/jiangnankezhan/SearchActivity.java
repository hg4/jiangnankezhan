package com.example.hg4.jiangnankezhan;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.search.AVSearchQuery;
import com.example.hg4.jiangnankezhan.Adapter.SechcsAdapter;
import com.example.hg4.jiangnankezhan.Model.Course;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements OnLoadMoreListener{
	private EditText input;
	private TextView find;
	private RecyclerView recyclerView;
	private List<AVObject> courseList=new ArrayList<>();
	private List<AVObject> displayCourseList=new ArrayList<>();
	private SechcsAdapter adapter;
	private ImageView back;
	private SwipeToLoadLayout swipeToLoadLayout;
	private int index=ORI;
	private static final int INCREMENT=1;
	private static final int ORI=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		input=(EditText)findViewById(R.id.sechcs_text);
		find=(TextView)findViewById(R.id.schcs_find);
		back=(ImageView)findViewById(R.id.sechcs_back);
		swipeToLoadLayout=(SwipeToLoadLayout)findViewById(R.id.swipeToLoadLayout);
		swipeToLoadLayout.setOnLoadMoreListener(this);
		recyclerView=(RecyclerView)findViewById(R.id.swipe_target);
		LinearLayoutManager layoutManager=new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		find.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String findText=input.getText().toString();
				if(!"".equals(findText)){
					AVSearchQuery<AVObject> avSearchQuery=new AVSearchQuery<AVObject>(findText);
					avSearchQuery.setClassName("Course");
					avSearchQuery.setLimit(10);
					avSearchQuery.findInBackgroud(new FindCallback<AVObject>() {
						@Override
						public void done(List<AVObject> list, AVException e) {
							if(e==null){
								if(courseList.size()!=0){
									courseList.clear();
									index=ORI;
								}
								if(list.size()!=0){
									Log.e("test",list.toString());
									courseList=list;
									loadMoreCourse();
								}
							}
							else e.printStackTrace();

						}
					});
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
	private void loadMoreCourse(){
		if(courseList.size()>index){
			displayCourseList=courseList.subList(0,index);
			index=index+INCREMENT;
		}
		else {
			index=courseList.size();
			displayCourseList=courseList.subList(0,index);
			swipeToLoadLayout.setLoadMoreEnabled(false);
		}
		adapter=new SechcsAdapter(displayCourseList);
		recyclerView.setAdapter(adapter);
	}
	@Override
	public void onLoadMore() {
		swipeToLoadLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setLoadingMore(false);
				loadMoreCourse();
			}
		},1000);
	}
}
