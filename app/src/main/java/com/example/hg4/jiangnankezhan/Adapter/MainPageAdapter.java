package com.example.hg4.jiangnankezhan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.CommentActivity;
import com.example.hg4.jiangnankezhan.CosContentActivity;
import com.example.hg4.jiangnankezhan.DownloadActivity;
import com.example.hg4.jiangnankezhan.MainPageActivity;
import com.example.hg4.jiangnankezhan.MaterialActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.RequirementsActivity;
import com.example.hg4.jiangnankezhan.UploadActivity;
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
	private int type=0;

	static class ViewHolder extends RecyclerView.ViewHolder{
		int type=0;
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
	public MainPageAdapter(List<AVObject> commentList,int type){
		mCommentList=commentList;
		this.type=type;
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
				switch (viewHolder.type){
					case 0:{
						Intent intent=new Intent(mContext, RequirementsActivity.class);
						intent.putExtra("teacher",viewHolder.AvComment.getString("teacher"));
						intent.putExtra("courseName",viewHolder.AvComment.getString("courseName"));
						mContext.startActivity(intent);
						break;
					}
					case 1:{
						Intent intent=new Intent(mContext, DownloadActivity.class);
						intent.putExtra("courseName",viewHolder.AvComment.getString("Course"));
						intent.putExtra("content",viewHolder.link.getText().toString());
						mContext.startActivity(intent);
						break;
					}
					case -1:{
						Intent intent=new Intent(mContext, CosContentActivity.class);
						intent.putExtra("courseName",viewHolder.AvComment.getString("courseName"));
						mContext.startActivity(intent);
						break;
					}
				}
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final MainPageAdapter.ViewHolder holder, final int position) {
		AVObject comment = mCommentList.get(position);
		holder.AvComment = comment;
		holder.head.setImageResource(R.drawable.def_ava_round);
		if (comment.getAVFile("resource") != null)
			holder.type = 1;
		switch (holder.type) {
			case 0:
				AVObject user = comment.getAVUser("from");
				if(type==1){
					String fromName=user.getString("nickname");
					SpannableString spannableString=new SpannableString(fromName+"评价了");
					ForegroundColorSpan colorSpan1=new ForegroundColorSpan(mContext.getResources().getColor(R.color.black));
					AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(36);
					spannableString.setSpan(sizeSpan,fromName.length(),fromName.length()+3,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
					spannableString.setSpan(colorSpan1,fromName.length(),fromName.length()+3,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
					holder.action.setText(spannableString);
			}
				else holder.action.setText("评价了");
				if (user.getAVFile("head") != null) {
					AVFile file = user.getAVFile("head");
					if (file != null && file.getUrl() != null) {
						Glide.with(mContext).load(file.getUrl()).into(holder.head);
					}
					if(comment.getInt("type")==3){
						holder.type=-1;
					}
					holder.date.setText(TimeUtils.dateToString(comment.getUpdatedAt()));
					holder.content.setText(comment.getString("content"));
					Integer intLikeCount = comment.getInt("likeCount");
					Integer intCommentCount = comment.getInt("commentCount");
					holder.likeCount.setText(intLikeCount.toString());
					holder.commentCount.setText(intCommentCount.toString());
					holder.aim.setText(comment.getString("courseName"));

				}
				break;
			case 1:
				holder.down.setVisibility(View.VISIBLE);
				holder.downCount.setVisibility(View.VISIBLE);
				holder.coin.setVisibility(View.VISIBLE);
				holder.coinCount.setVisibility(View.VISIBLE);
				holder.link.setVisibility(View.VISIBLE);
				AVObject owner = comment.getAVObject("owner");
				if(type==1) {
				String fromName=owner.getString("nickname");
				SpannableString spannableString=new SpannableString(fromName+"上传资料到");
				ForegroundColorSpan colorSpan1=new ForegroundColorSpan(mContext.getResources().getColor(R.color.black));
				AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(36);
				spannableString.setSpan(sizeSpan,fromName.length(),fromName.length()+5,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
				spannableString.setSpan(colorSpan1,fromName.length(),fromName.length()+5,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
					holder.action.setText(spannableString);
			}
			else holder.action.setText("上传资料到");
				if (owner.getAVFile("head") != null) {
					AVFile file = owner.getAVFile("head");
					if (file != null && file.getUrl() != null) {
						Glide.with(mContext).load(file.getUrl()).into(holder.head);
					}
					holder.content.setText(comment.getString("Introduce"));
					holder.date.setText(TimeUtils.dateToString(comment.getUpdatedAt()));
					holder.downCount.setText("" + comment.getInt("download"));
					holder.coinCount.setText("" + comment.getInt("points") + "积分");
					holder.likeCount.setText("" + comment.getInt("likeCount"));
					holder.commentCount.setText("" + comment.getInt("commentCount"));
					holder.aim.setText(comment.getString("Course"));
					holder.link.setText(comment.getString("Title"));
					break;
				}
		}
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		super.onViewRecycled(holder);
		holder.type=0;
		holder.down.setVisibility(View.GONE);
		holder.downCount.setVisibility(View.GONE);
		holder.coin.setVisibility(View.GONE);
		holder.coinCount.setVisibility(View.GONE);
		holder.link.setVisibility(View.GONE);
	}

	@Override
	public int getItemCount() {
		return mCommentList.size();
	}
}
