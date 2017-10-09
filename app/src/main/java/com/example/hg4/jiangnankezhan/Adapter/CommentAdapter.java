package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.PersonInfoActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.example.hg4.jiangnankezhan.Utils.RegexUtil;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by HG4 on 2017/10/4.
 */

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> mCommentList=new ArrayList<>();
	private Integer intLikeCount;
	private Integer intCommentCount;
	static class ViewHolder extends RecyclerView.ViewHolder{
		CardView cardView;
		ImageView head;
		TextView username;
		TextView date;
		TextView comment;
		TextView likeCount;
		TextView commentCount;
		TextView likeText;
		TextView commentText;
		Button like;
		Button comment_comment;
		AVObject AvComment;
		public ViewHolder(View view){
			super(view);
			cardView=(CardView)view;
			head=(ImageView)view.findViewById(R.id.comment_head);
			username=(TextView)view.findViewById(R.id.comment_username);
			date=(TextView)view.findViewById(R.id.comment_date);
			comment=(TextView)view.findViewById(R.id.comment_content);
			like=(Button)view.findViewById(R.id.comment_like_button);
			comment_comment=(Button)view.findViewById(R.id.comment_comment_button);
			likeCount=(TextView)view.findViewById(R.id.comment_like_count);
			commentCount=(TextView)view.findViewById(R.id.comment_comment_count);
			likeText=(TextView)view.findViewById(R.id.comment_liketext);
			commentText=(TextView)view.findViewById(R.id.comment_commenttext);
		}
	}

	public CommentAdapter(List<AVObject> commentList){
		mCommentList=commentList;
	}

	@Override
	public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.comment_fragment_item,parent,false);
		final ViewHolder viewHolder=new ViewHolder(view);
		viewHolder.like.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//点赞
					if(viewHolder.AvComment!=null){
						intLikeCount++;
						viewHolder.AvComment.put("likeCount",intLikeCount);
						viewHolder.AvComment.saveInBackground(new SaveCallback() {
							@Override
							public void done(AVException e) {
								viewHolder.likeCount.setText(intLikeCount.toString());
								viewHolder.like.setClickable(false);
							}
						});
					}
			}
		});
		viewHolder.comment_comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//评论
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final CommentAdapter.ViewHolder holder, int position) {
			AVObject comment=mCommentList.get(position);
			holder.AvComment=comment;
			AVObject user=comment.getAVUser("from");
			AVFile file=user.getAVFile("head");
			file.getDataInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] bytes, AVException e) {
					if(e==null){
						Bitmap head= Utilty.Bytes2Bimap(bytes);
						holder.head.setImageBitmap(head);
					}
					else e.printStackTrace();
				}
			});
			holder.username.setText(user.getString("nickname"));
			holder.date.setText(TimeUtils.dateToString(comment.getUpdatedAt()));
			holder.comment.setText(comment.getString("content"));
			intLikeCount=comment.getInt("likeCount");
			intCommentCount=comment.getInt("commentCount");
			holder.likeCount.setText(intLikeCount.toString());
			holder.commentCount.setText(intCommentCount.toString());



	}

	@Override
	public int getItemCount() {
		return mCommentList.size();
	}
}
