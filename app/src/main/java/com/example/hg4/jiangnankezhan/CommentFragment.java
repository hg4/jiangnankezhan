package com.example.hg4.jiangnankezhan;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.Adapter.CommentAdapter;
import com.example.hg4.jiangnankezhan.Adapter.SechcsAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.hg4.jiangnankezhan.R.id.holder;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment  implements OnRefreshListener, OnLoadMoreListener {
	private RecyclerView commentView;
	private SwipeToLoadLayout swipeToLoadLayout;
	private List<AVObject> commentList=new ArrayList<>();
	private List<AVObject> displayCourseList=new ArrayList<>();
	private List<AVObject> imageList=new ArrayList<>();
	private Bundle mArguments;
	private int type;
	private CommentAdapter adapter;
	private String courseName;
	private String teacher;
	private int index=ORI;
	private static final int INCREMENT=4;
	private static final int ORI=4;
	public CommentFragment() {
		// Required empty public constructor

	}

	@Override
	public void setArguments(Bundle args) {
		mArguments=args;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view=inflater.inflate(R.layout.fragment_comment, container, false);
		commentView=(RecyclerView)view.findViewById(R.id.swipe_target);
		swipeToLoadLayout=(SwipeToLoadLayout) view.findViewById(R.id.cmt_swipeLayout);
		swipeToLoadLayout.setOnLoadMoreListener(this);
		swipeToLoadLayout.setOnRefreshListener(this);
		LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
		commentView.setLayoutManager(layoutManager);
		setConfig();
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	private void setConfig(){
		if(mArguments!=null){
			type=mArguments.getInt("type",0);
			courseName=mArguments.getString("courseName");
			teacher=mArguments.getString("teacher");
			switch (type){
				case 0:
					AVQuery<AVObject> query=new AVQuery<>("Course_comment");
					query.orderByDescending("updatedAt");
					query.whereEqualTo("courseName",courseName);
					query.whereEqualTo("type",type);
					query.include("from");
					query.findInBackground(new FindCallback<AVObject>() {
						@Override
						public void done(List<AVObject> list, AVException e) {
							if(e==null){
								if(commentList.size()!=0){
									commentList.clear();
									index=ORI;
								}
								if(list.size()!=0){
									commentList=list;
									loadMoreComment();
								}
							}
						}
					});
					break;
				case 1:
					swipeToLoadLayout.setBackgroundColor(getResources().getColor(R.color.colorBlue));
					AVQuery<AVObject> query1=new AVQuery<>("Course_comment");
					query1.orderByDescending("updatedAt");
					query1.whereEqualTo("courseName",courseName);
					query1.whereEqualTo("teacher",teacher);
					query1.whereEqualTo("type",type);
					query1.include("from");
					query1.findInBackground(new FindCallback<AVObject>() {
						@Override
						public void done(List<AVObject> list, AVException e) {
							if(e==null){
								if(commentList.size()!=0){
									commentList.clear();
									index=ORI;
								}
								if(list.size()!=0){
									commentList=list;
									loadMoreComment();
								}
							}
						}
					});
					break;
				case 2:
					swipeToLoadLayout.setBackgroundColor(getResources().getColor(R.color.colorBrown));
					AVQuery<AVObject> query2=new AVQuery<>("Course_comment");
					query2.orderByDescending("updatedAt");
					query2.whereEqualTo("courseName",courseName);
					query2.whereEqualTo("teacher",teacher);
					query2.whereEqualTo("type",type);
					query2.include("from");
					query2.findInBackground(new FindCallback<AVObject>() {
						@Override
						public void done(List<AVObject> list, AVException e) {
							if(e==null){
								if(commentList.size()!=0){
									commentList.clear();
								}
								if(list.size()!=0){
									commentList=list;
									loadMoreComment();
								}
							}
						}
					});
					break;
			}
		}
	}
	private void loadMoreComment(){
		if(commentList.size()>index){
			displayCourseList=commentList.subList(0,index);
			index=index+INCREMENT;
		}
		else {
			index=commentList.size();
			displayCourseList=commentList.subList(0,index);
			swipeToLoadLayout.setLoadMoreEnabled(false);
		}
		adapter=new CommentAdapter(displayCourseList);
		commentView.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
		index=ORI;
		swipeToLoadLayout.setLoadMoreEnabled(true);
		swipeToLoadLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setRefreshing(false);
				setConfig();
			}
		},1000);
	}

	@Override
	public void onLoadMore() {
		swipeToLoadLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setLoadingMore(false);
				loadMoreComment();
			}
		},1000);
	}

	public void autoRefresh() {
		swipeToLoadLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setRefreshing(true);
			}
		});
	}


}
