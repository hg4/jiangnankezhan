package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
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
		LinearLayout imageList;
		LinearLayout commentList;
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
			imageList=(LinearLayout)view.findViewById(R.id.comment_image_list);

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
					if(e==null&&bytes!=null){
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
			AVQuery<AVObject> imgQuery=new AVQuery<>("cscmt_imagelist");
			imgQuery.whereEqualTo("from",comment);
			imgQuery.findInBackground(new FindCallback<AVObject>() {
				@Override
				public void done(List<AVObject> list, AVException e) {
					if(e==null&&list.size()!=0){
						for(AVObject image:list){
							AVFile file=image.getAVFile("image");
							String url=file.getUrl();
							ImageView imageView=new ImageView(mContext);
							LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(200,200);
							layoutParams.setMargins(40,10,0,0);
							imageView.setLayoutParams(layoutParams);
							imageView.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

								}
							});
							Glide.with(mContext).load(url).centerCrop().into(imageView);
							holder.imageList.addView(imageView);
						}
					}
					else {
						Log.e("test",((Integer)list.size()).toString());
					}
				}
			});
		/*	AVQuery<AVObject> query=new AVQuery<>("course_comment");
			query.whereEqualTo("objectId",comment.getObjectId());
			AVQuery<AVObject> imgQuery=new AVQuery<>("cscmt_imagelist");
			imgQuery.whereMatchesQuery("from",query);
			imgQuery.findInBackground(new FindCallback<AVObject>() {
				@Override
				public void done(List<AVObject> list, AVException e) {
					if(e==null&&list.size()!=0){
						for(AVObject image:list){
							AVFile pic=image.getAVFile("image");
							pic.getDataInBackground(new GetDataCallback() {
								@Override
								public void done(byte[] bytes, AVException e) {
									if(e==null&&bytes!=null){
										ImageView imageView=new ImageView(mContext);
										imageView.setLayoutParams(new LinearLayout.LayoutParams(50,50));
										Glide.with(mContext).load(bytes).centerCrop().into(imageView);
										holder.imageList.addView(imageView);
									}
								}
							});
						}
					}
					else {
						e.printStackTrace();
						Log.e("test",((Integer)list.size()).toString());
					}
				}
			}); */
	}

	@Override
	public int getItemCount() {
		return mCommentList.size();
	}
}
