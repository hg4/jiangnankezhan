package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.hg4.jiangnankezhan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2017/11/16.
 */

public class UniCmtAdapter extends RecyclerView.Adapter<UniCmtAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> commentList=new ArrayList<>();//material_comment对象list集合
	public static class ViewHolder extends RecyclerView.ViewHolder{
		AVObject cmtObject;//对应material_comment对象
		AVObject toMaterial;//对应material列
		AVObject fromUser;//对应from列
		AVObject toUser;//对应toUser列
		TextView content;
		ImageView head;
		TextView date;
		TextView reply;
		RecyclerView childCmtList;
		LinearLayout imageList;
		public ViewHolder(View view){
			super(view);
			head=(ImageView)view.findViewById(R.id.head);
			date=(TextView)view.findViewById(R.id.time);
			content=(TextView)view.findViewById(R.id.content);
			reply=(TextView)view.findViewById(R.id.reply);
			childCmtList=(RecyclerView)view.findViewById(R.id.relylist);
			imageList=(LinearLayout)view.findViewById(R.id.imagelist);
		}
	}
	public UniCmtAdapter(List list){
		commentList=list;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.material_comment_item,parent,false);
		ViewHolder holder=new ViewHolder(view);
		holder.reply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//点击跳转commentactivity
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.head.setImageResource(R.drawable.def_ava_round);//防止错位
		//怎么bind数据自己写
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		super.onViewRecycled(holder);
		holder.imageList.removeAllViews();
	}

	@Override
	public int getItemCount() {
		return commentList.size();
	}
}
