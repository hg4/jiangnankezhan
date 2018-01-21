package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.RequirementsActivity;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2018/1/19.
 */

public class SimilarCmtAdapter extends RecyclerView.Adapter<SimilarCmtAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> mCommentList=new ArrayList<>();

	static class ViewHolder extends RecyclerView.ViewHolder{
		CardView cardView;
		TextView from;
		TextView type;
		ImageView head;
		TextView nickname;
		TextView date;
		TextView content;
		ImageButton toSee;
		AVObject cmtObject;
		public ViewHolder(View view){
			super(view);
			cardView=(CardView)view;
			from=(TextView)view.findViewById(R.id.from);
			type=(TextView)view.findViewById(R.id.type);
			head=(ImageView)view.findViewById(R.id.head);
			nickname=(TextView)view.findViewById(R.id.nickname);
			date=(TextView)view.findViewById(R.id.date);
			content=(TextView)view.findViewById(R.id.content);
			toSee=(ImageButton)view.findViewById(R.id.tosee);
		}
	}
	public  SimilarCmtAdapter(List<AVObject> mCommentList){
		this.mCommentList=mCommentList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.similar_comment_item,parent,false);
		final ViewHolder viewHolder=new ViewHolder(view);
		viewHolder.toSee.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext, RequirementsActivity.class);
				String courseName=viewHolder.cmtObject.getString("courseName");
				String teacher=viewHolder.cmtObject.getString("teacher");
				intent.putExtra("courseName",courseName);
				intent.putExtra("teacher",teacher);
				mContext.startActivity(intent);
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		AVObject similarComment=mCommentList.get(position);
		holder.cmtObject=similarComment;
		String from="来自"+similarComment.getString("courseName");
		SpannableString spFrom=new SpannableString(from);
		int start=2;
		int end=start+similarComment.getString("courseName").length();
		AbsoluteSizeSpan sizeSpan=new AbsoluteSizeSpan(60);
		ForegroundColorSpan colorSpan1=new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorRed));
		ForegroundColorSpan colorSpan2=new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorDarkbule));
		spFrom.setSpan(colorSpan1,start,end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		spFrom.setSpan(sizeSpan,start,end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		holder.from.setText(spFrom);//来自什么的评价
		String cmtType;
		int type=similarComment.getInt("type");
		if(type==1){
			cmtType="对教师风格的评价";
			SpannableString spType=new SpannableString(cmtType);
			spType.setSpan(colorSpan2,1,5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			spType.setSpan(sizeSpan,1,5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			holder.type.setText(spType);
		}
		if(type==2){
			cmtType="对点名频率的评价";
			SpannableString spType=new SpannableString(cmtType);
			spType.setSpan(colorSpan2,1,5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			spType.setSpan(sizeSpan,1,5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
			holder.type.setText(spType);
		}
		AVObject user=similarComment.getAVObject("from");
		if(user.getAVFile("head")!=null){
			AVFile file=user.getAVFile("head");
			if(file!=null&&file.getUrl()!=null){
				Glide.with(mContext).load(file.getUrl()).into(holder.head);
			}
		}
		if(!user.getString("nickname").equals("（请填写）"))
			holder.nickname.setText(user.getString("nickname"));
		holder.date.setText(TimeUtils.dateToString(similarComment.getUpdatedAt()));
		holder.content.setText(similarComment.getString("content"));
	}

	@Override
	public int getItemCount() {
		return mCommentList.size();
	}
}
