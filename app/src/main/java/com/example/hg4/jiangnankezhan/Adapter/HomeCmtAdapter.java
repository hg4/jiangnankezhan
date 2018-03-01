package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.CosContentActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.RequirementsActivity;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2018/2/27.
 */

public class HomeCmtAdapter extends RecyclerView.Adapter<HomeCmtAdapter.ViewHolder> {
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
		ImageView like;
		ImageView comment;
		ImageView prepic;
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
			action=(TextView)view.findViewById(R.id.action);
			aim=(TextView)view.findViewById(R.id.aimName);
			prepic=(ImageView)view.findViewById(R.id.prepic);
		}
	}

	public HomeCmtAdapter(List<AVObject> commentList){
		mCommentList=commentList;
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.hotcmt_item,parent,false);
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
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		AVObject comment = mCommentList.get(position);
		holder.AvComment = comment;
		holder.head.setImageResource(R.drawable.def_ava_round);
		AVObject user = comment.getAVUser("from");
		String fromName=user.getString("nickname");
		SpannableString spannableString=new SpannableString(fromName+"评价了");
		ForegroundColorSpan colorSpan1=new ForegroundColorSpan(mContext.getResources().getColor(R.color.black));
		AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(36);
		spannableString.setSpan(sizeSpan,fromName.length(),fromName.length()+3,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(colorSpan1,fromName.length(),fromName.length()+3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		holder.action.setText(spannableString);
		if (user.getAVFile("head") != null) {
			AVFile file = user.getAVFile("head");
			if (file != null && file.getUrl() != null) {
				Glide.with(mContext).load(file.getUrl()).into(holder.head);
			}
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

	@Override
	public void onViewRecycled(ViewHolder holder) {
		super.onViewRecycled(holder);
	}

	@Override
	public int getItemCount() {
		return mCommentList.size();
	}
}

