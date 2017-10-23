package com.example.hg4.jiangnankezhan;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.avos.avoscloud.AVObject;
import com.example.hg4.jiangnankezhan.Adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
	protected RecyclerView commentView;
	protected SwipeToLoadLayout swipeToLoadLayout;
	//继承对象获取完数据后将数据list赋给commentlist并调用loadmorecomment（）
	protected List<AVObject> commentList=new ArrayList<>();
	protected Bundle mArguments;
	//继承后adapter一定要幅值,adapter.notify要有效，必须其自身和数据源的list指向的对象没有改变，故后面两个不能是protected，若为protected继承后产生新的对象和父类不指向同一个
	private RecyclerView.Adapter adapter;
	private List<AVObject> displayCmtList;
	protected int index=ORI;
	protected static final int INCREMENT=4;
	protected static final int ORI=4;
	public RecyclerFragment() {
		// Required empty public constructor
	}
	public static RecyclerFragment newInstance(RecyclerView.Adapter adapter,List<AVObject> displayCmtList,Bundle bundle) {
		RecyclerFragment fragment = new RecyclerFragment();
		fragment.adapter=adapter;
		//初始化为内容为空即可，需和adapter中的list为同一对象
		fragment.displayCmtList=displayCmtList;
		//设置是否去掉上拉刷新（close=1）和下拉加载（close=2）
		fragment.mArguments=bundle;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_comment, container, false);
		commentView=(RecyclerView)view.findViewById(R.id.swipe_target);
		swipeToLoadLayout=(SwipeToLoadLayout) view.findViewById(R.id.cmt_swipeLayout);
		swipeToLoadLayout.setOnLoadMoreListener(this);
		swipeToLoadLayout.setOnRefreshListener(this);
		setswipeType();
		LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
		commentView.setLayoutManager(layoutManager);
		commentView.setAdapter(adapter);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	private void setswipeType(){
		int close=0;
		if(mArguments!=null)
			close=mArguments.getInt("close",0);
		switch (close){
			case 1:swipeToLoadLayout.setRefreshEnabled(false);
				break;
			case 2:swipeToLoadLayout.setLoadMoreEnabled(false);
				break;
			default:
				break;
		}
	}

	protected void loadMoreComment(){
		if(commentList.size()>index){
			displayCmtList.clear();
			displayCmtList.addAll(commentList.subList(0,index));
			index=index+INCREMENT;
		}
		else {
			displayCmtList.clear();
			index=commentList.size();
			displayCmtList.addAll(commentList.subList(0,index));
			swipeToLoadLayout.setLoadMoreEnabled(false);
		}
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onRefresh() {
		index=ORI;
		swipeToLoadLayout.setLoadMoreEnabled(true);
		swipeToLoadLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeToLoadLayout.setRefreshing(false);
				loadMoreComment();
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
	public void setArguments(Bundle args) {
		mArguments=args;
	}
	public void setAdapter(RecyclerView.Adapter adapter){
		this.adapter=adapter;
	}
	public void setRecyclerAdapter(RecyclerView.Adapter adapter){
		commentView.setAdapter(adapter);
	}
	public void setDisplayCmtList(List<AVObject> list){
		this.displayCmtList=list;
	}

}
