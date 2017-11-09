package com.example.hg4.jiangnankezhan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.CommentActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2017/10/4.
 */

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> mCommentList=new ArrayList<>();

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
		int maxTalkGroup;
		public ViewHolder(View view){
			super(view);
			cardView=(CardView)view;
			head=(ImageView)view.findViewById(R.id.comment_head);
			username=(TextView)view.findViewById(R.id.comment_username);
			date=(TextView)view.findViewById(R.id.comment_date);
			comment=(TextView)view.findViewById(R.id.mycomment_content);
			like=(Button)view.findViewById(R.id.comment_like_button);
			comment_comment=(Button)view.findViewById(R.id.comment_comment_button);
			likeCount=(TextView)view.findViewById(R.id.comment_like_count);
			commentCount=(TextView)view.findViewById(R.id.comment_comment_count);
			likeText=(TextView)view.findViewById(R.id.comment_liketext);
			commentText=(TextView)view.findViewById(R.id.comment_commenttext);
			imageList=(LinearLayout)view.findViewById(R.id.comment_image_list);
			commentList=(LinearLayout)view.findViewById(R.id.comment_comment_list);
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
						viewHolder.AvComment.increment("likeCount");
						viewHolder.AvComment.saveInBackground();
						Integer likecount=viewHolder.AvComment.getInt("likeCount");
						viewHolder.likeCount.setText(likecount.toString());
						viewHolder.like.setClickable(false);
					}
			}
		});
		viewHolder.comment_comment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//评论
				if(viewHolder.AvComment!=null){
					Intent intent=new Intent(mContext, CommentActivity.class);
					intent.putExtra("from",2);
					intent.putExtra("cmt",viewHolder.AvComment.toString());
					((Activity)mContext).startActivityForResult(intent,1);
				}
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final CommentAdapter.ViewHolder holder, final int position) {
			AVObject comment=mCommentList.get(position);
			holder.AvComment=comment;
			holder.head.setImageResource(R.drawable.def_ava_round);
			AVObject user=comment.getAVUser("from");
			if(user.getAVFile("head")!=null){
				AVFile file=user.getAVFile("head");
				if(file!=null&&file.getUrl()!=null){
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
				}
			}
			if(!user.getString("nickname").equals("（请填写）"))
				holder.username.setText(user.getString("nickname"));
			holder.date.setText(TimeUtils.dateToString(comment.getUpdatedAt()));
			holder.comment.setText(comment.getString("content"));
			Integer intLikeCount=comment.getInt("likeCount");
			Integer intCommentCount=comment.getInt("commentCount");
			holder.likeCount.setText(intLikeCount.toString());
			holder.commentCount.setText(intCommentCount.toString());
			holder.cardView.setTag(position);
			AVQuery<AVObject> imgQuery=new AVQuery<>("cscmt_imagelist");
			imgQuery.whereEqualTo("from",comment);
			imgQuery.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(e==null&&list.size()!=0){
					for(AVObject image:list){
						AVFile pic=image.getAVFile("image");
						if(pic!=null){
							final String url=pic.getUrl();
							ImageView imageView=new ImageView(mContext);
							imageView.setTag(R.id.tag_img,url);
							LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(200,200);
							layoutParams.setMargins(45,10,0,0);
							imageView.setLayoutParams(layoutParams);
							imageView.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Utilty.imgDetailDialog(mContext,url);
								}
							});
							if ((int)holder.cardView.getTag()==position&&imageView.getTag(R.id.tag_img) != null && imageView.getTag(R.id.tag_img).equals(url)){
								Glide.with(mContext).load(url).centerCrop().placeholder(R.drawable.placeholder).into(imageView);
								holder.imageList.addView(imageView);
							}

						}

					}
				}
					else {
						Log.e("test",((Integer)list.size()).toString());
					}
				}
			});
			AVQuery<AVObject> cmtQuery=new AVQuery<>("cscmt_commentlist");
			cmtQuery.whereEqualTo("to",comment);
			cmtQuery.addAscendingOrder("talkGroup");
			cmtQuery.include("from");
			cmtQuery.include("targetUser");
			cmtQuery.findInBackground(new FindCallback<AVObject>() {
				@Override
				public void done(List<AVObject> list, AVException e) {
					if(e==null&&list!=null){
						if(list.size()!=0){
							holder.commentCount.setText(((Integer)list.size()).toString());
							if(list.get(list.size()-1).getInt("talkGroup")!=0)
								holder.maxTalkGroup=list.get(list.size()-1).getInt("talkGroup");
							ArrayList<String> data=new ArrayList<String>();
							for(AVObject cmt:list){
								String content=cmt.getString("content");
								AVUser cmter=cmt.getAVUser("from");
								String fromName=cmter.getString("nickname");
								String toName="";
								if(cmt.getAVObject("targetUser")!=null)
									toName=cmt.getAVObject("targetUser").getString("nickname");
								if(cmter.getString("nickname").equals("（请填写）")){
									fromName="匿名用户";
								}
								if(toName.equals("（请填写）")){
									toName="匿名用户";
								}
								TextView textView=new TextView(mContext);
								SpannableString spanString=new SpannableString(fromName+"回复"+toName+":"+content);
								ForegroundColorSpan colorSpan1=new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorRed));
								ForegroundColorSpan colorSpan2=new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorRed));
								spanString.setSpan(colorSpan1,0,fromName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
								spanString.setSpan(colorSpan2,fromName.length()+2,fromName.length()+2+toName.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
								textView.setText(spanString);
								textView.setMaxEms(22);
								LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
								layoutParams.setMargins(51,0,0,18);
								textView.setLayoutParams(layoutParams);
								holder.commentList.addView(textView);
							}
						}

					}
					else {
						Log.e("test",((Integer)list.size()).toString());
					}
				}
			});
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		holder.imageList.removeAllViews();
		holder.commentList.removeAllViews();
	}

	@Override
	public int getItemCount() {
		return mCommentList.size();
	}
}
