package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.CommentActivity;
import com.example.hg4.jiangnankezhan.CosContentActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.RequirementsActivity;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;

import java.util.List;

/**
 * Created by HG4 on 2017/11/4.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {
	private List<AVObject> mReplyList;
	private Context mContext;
	public static class ViewHolder extends RecyclerView.ViewHolder{
		ConstraintLayout holderView;
		TextView fromUserName;
		TextView content;
		TextView date;
		TextView from;
		TextView reply;
		ImageView head;
		ImageView contentImg;
		//父评论对象
		AVObject commentObject;
		//子评论用户
		AVObject fromUser;
		AVObject replyItem;
		public ViewHolder(View view){
			super(view);
			holderView=(ConstraintLayout)view.findViewById(R.id.holder);
			holderView=(ConstraintLayout) view;
			fromUserName=(TextView)view.findViewById(R.id.fromUser);
			content=(TextView)view.findViewById(R.id.content);
			date=(TextView)view.findViewById(R.id.date);
			from=(TextView)view.findViewById(R.id.from);
			head=(ImageView) view.findViewById(R.id.circleImageView);
			contentImg=(ImageView)view.findViewById(R.id.content_img);
			reply=(TextView)view.findViewById(R.id.reply);
		}
	}
	public ReplyAdapter(List<AVObject> replyList){
		mReplyList=replyList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.reply_item,parent,false);
		final ViewHolder viewHolder=new ViewHolder(view);
		viewHolder.holderView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转
				int type=viewHolder.commentObject.getInt("type");
				if(type==3){
					Intent intent=new Intent(mContext, CosContentActivity.class);
					intent.putExtra("courseName",viewHolder.commentObject.getString("courseName"));
					intent.putExtra("teacher",viewHolder.commentObject.getString("teacher"));
					mContext.startActivity(intent);
				}
				else {
					Intent intent=new Intent(mContext, RequirementsActivity.class);
					intent.putExtra("courseName",viewHolder.commentObject.getString("courseName"));
					intent.putExtra("teacher",viewHolder.commentObject.getString("teacher"));
					intent.putExtra("type",viewHolder.commentObject.getInt("type"));
					mContext.startActivity(intent);
				}
			}
		});
		viewHolder.reply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//打开评论
				if(viewHolder.replyItem!=null){
					Intent intent=new Intent(mContext, CommentActivity.class);
					intent.putExtra("from",4);
					intent.putExtra("reply",viewHolder.replyItem.toString());
					mContext.startActivity(intent);
				}
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
			AVObject replyItem=mReplyList.get(position);
			holder.head.setImageResource(R.drawable.def_ava_round);
			holder.contentImg.setImageResource(0);
			holder.replyItem=replyItem;
			holder.commentObject=replyItem.getAVObject("to");
			holder.fromUser=replyItem.getAVObject("from");
			holder.content.setText(replyItem.getString("content"));
			if(!holder.fromUser.getString("nickname").equals("（请填写）"))
				holder.fromUserName.setText(holder.fromUser.getString("nickname"));
			AVFile file=holder.fromUser.getAVFile("head");

			if(file!=null){
				holder.head.setTag(R.id.tag_img,file.getUrl());
				Log.e("test1",holder.head.getTag(R.id.tag_img)+" "+file.getUrl());
				if((holder.head.getTag(R.id.tag_img))!=null&&(holder.head.getTag(R.id.tag_img)).equals(file.getUrl()))
					Glide.with(mContext).load(file.getUrl()).into(holder.head);
			}
			holder.from.setText(holder.commentObject.getString("courseName"));
			holder.date.setText(TimeUtils.dateToString(replyItem.getCreatedAt()));
			AVQuery<AVObject> imgQuery=new AVQuery<>("cscmt_imagelist");
			imgQuery.whereEqualTo("from",holder.commentObject);
			imgQuery.getFirstInBackground(new GetCallback<AVObject>() {
				@Override
				public void done(AVObject avObject, AVException e) {
					if(avObject!=null){
						holder.contentImg.setTag(R.id.tag_img,avObject.getAVFile("image").getUrl());
						Log.e("test2",holder.contentImg.getTag(R.id.tag_img)+" "+avObject.getAVFile("image").getUrl());
						if((holder.contentImg.getTag(R.id.tag_img))!=null&&(holder.contentImg.getTag(R.id.tag_img)).equals(avObject.getAVFile("image").getUrl()))
							Glide.with(mContext).load(avObject.getAVFile("image").getUrl()).into(holder.contentImg);
					}
				}
			});
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		super.onViewRecycled(holder);
	}

	@Override
	public int getItemCount() {
		return mReplyList.size();
	}
}
