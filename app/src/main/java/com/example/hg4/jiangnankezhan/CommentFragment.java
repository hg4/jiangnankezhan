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
import static com.example.hg4.jiangnankezhan.R.id.list;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends RecyclerFragment {
	private int type;
	private String courseName;
	private String teacher;
	private RecyclerView swipe_target;
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
		List<AVObject> displayCmtList=new ArrayList<>();
		setDisplayCmtList(displayCmtList);
		setAdapter(new CommentAdapter(displayCmtList));
		setConfig();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onRefresh() {
		setConfig();
		super.onRefresh();
	}

	private void setConfig(){
		if(mArguments!=null){
			type=mArguments.getInt("type",0);
			courseName=mArguments.getString("courseName");
			teacher=mArguments.getString("teacher");
			switch (type){
				case 0:
					AVQuery<AVObject> query=new AVQuery<>("Course_comment");
					query.orderByDescending("createdAt");
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
								}else{
									swipeToLoadLayout.setBackgroundResource(R.drawable.nocmt);
								}
							}
						}
					});
					break;
				case 1:
					AVQuery<AVObject> query1=new AVQuery<>("Course_comment");
					query1.orderByDescending("createdAt");
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
								}else{
									commentView.setBackgroundResource(R.drawable.nocom2);
								}
							}
						}
					});
					break;
				case 2:
					AVQuery<AVObject> query2=new AVQuery<>("Course_comment");
					query2.orderByDescending("createdAt");
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
								}else{
									commentView.setBackgroundResource(R.drawable.nocom3);
								}
							}
						}
					});
					break;
				case 3:
					AVQuery<AVObject> query3=new AVQuery<>("Course_comment");
					query3.orderByDescending("createdAt");
					query3.whereEqualTo("courseName",courseName);
					query3.whereEqualTo("type",type);
					query3.include("from");
					query3.findInBackground(new FindCallback<AVObject>() {
						@Override
						public void done(List<AVObject> list, AVException e) {
							if(e==null){
								if(commentList.size()!=0){
									commentList.clear();
								}
								if(list.size()!=0){
									commentList=list;
									loadMoreComment();
								}else{
									commentView.setBackgroundResource(R.drawable.nocom4);
								}
							}
						}
					});
			}
		}
	}


}
