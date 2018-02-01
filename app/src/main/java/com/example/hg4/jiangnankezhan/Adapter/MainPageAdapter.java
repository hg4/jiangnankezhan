package com.example.hg4.jiangnankezhan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.CommentActivity;
import com.example.hg4.jiangnankezhan.MainPageActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2018/1/30.
 */

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> mCommentList=new ArrayList<>();

	static class ViewHolder extends RecyclerView.ViewHolder{
		CardView cardView;
		ImageView head;
		TextView action;
		TextView aim;
		TextView date;
		TextView content;
		TextView likeCount;
		TextView commentCount;
		TextView downCount;
		TextView coinCount;
		TextView link;
		ImageView like;
		ImageView comment;
		ImageView coin;
		ImageView down;
		AVObject AvComment;
		public ViewHolder(View view){
			super(view);
			cardView=(CardView)view;
			head=(ImageView)view.findViewById(R.id.head);
			date=(TextView)view.findViewById(R.id.date);
			content=(TextView)view.findViewById(R.id.content);
			like=(ImageView) view.findViewById(R.id.like);
			likeCount=(TextView)view.findViewById(R.id.likecount);
			comment=(ImageView)view.findViewById(R.id.comment);
			commentCount=(TextView)view.findViewById(R.id.comment_count);
			coin=(ImageView)view.findViewById(R.id.coin);
			down=(ImageView)view.findViewById(R.id.down);
			downCount=(TextView)view.findViewById(R.id.downcount);
			coinCount=(TextView)view.findViewById(R.id.coincount);
			action=(TextView)view.findViewById(R.id.action);
			aim=(TextView)view.findViewById(R.id.aimName);
			link=(TextView)view.findViewById(R.id.link);
		}
	}

	public MainPageAdapter(List<AVObject> commentList){
		mCommentList=commentList;
	}

	@Override
	public MainPageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.mainpage_item,parent,false);
		final ViewHolder viewHolder=new ViewHolder(view);
		viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final MainPageAdapter.ViewHolder holder, final int position) {
		int type=0;
		AVObject comment=mCommentList.get(position);
		holder.AvComment=comment;
		holder.head.setImageResource(R.drawable.def_ava_round);
		if(comment.getAVFile("resource")!=null)
			type=1;
		switch (type){
			case 0:
				holder.action.setText("评价了");
				AVObject user=comment.getAVUser("from");
				if(user.getAVFile("head")!=null){
				AVFile file=user.getAVFile("head");
				if(file!=null&&file.getUrl()!=null){
					Glide.with(mContext).load(file.getUrl()).into(holder.head);
				}
				holder.date.setText(TimeUtils.dateToString(comment.getUpdatedAt()));
					holder.content.setText(comment.getString("content"));
					Integer intLikeCount=comment.getInt("likeCount");
					Integer intCommentCount=comment.getInt("commentCount");
					holder.likeCount.setText(intLikeCount.toString());
					holder.commentCount.setText(intCommentCount.toString());
					holder.aim.setText(comment.getString("courseName"));

			}
				break;
			case 1:
				holder.action.setText("上传资料到");
				holder.down.setVisibility(View.VISIBLE);
				holder.downCount.setVisibility(View.VISIBLE);
				holder.coin.setVisibility(View.VISIBLE);
				holder.coinCount.setVisibility(View.VISIBLE);
				holder.link.setVisibility(View.VISIBLE);
				holder.content.setText(comment.getString("Introduce"));
				holder.date.setText(TimeUtils.dateToString(comment.getUpdatedAt()));
				holder.downCount.setText(""+comment.getInt("download"));
				holder.coinCount.setText(""+comment.getInt("points")+"积分");
				holder.likeCount.setText(""+comment.getInt("likeCount"));
				holder.commentCount.setText(""+comment.getInt("commentCount"));
				holder.aim.setText(comment.getString("Course"));
				holder.link.setText(comment.getString("Title"));

				break;
		}
	}


	@Override
	public int getItemCount() {
		return mCommentList.size();
	}
}
