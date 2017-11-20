package com.example.hg4.jiangnankezhan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.hg4.jiangnankezhan.DownloadActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.SettingActivity;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;

import static com.example.hg4.jiangnankezhan.R.id.comment;
import static com.example.hg4.jiangnankezhan.R.id.holder;

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
		UniChildCmtAdapter adapter;
		List<AVObject> childlist=new ArrayList<>();
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
	public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.material_comment_item,parent,false);
		final ViewHolder holder=new ViewHolder(view);
		LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
		holder.childCmtList.setLayoutManager(layoutManager);
		holder.adapter=new UniChildCmtAdapter(holder.childlist);
		holder.childCmtList.setAdapter(holder.adapter);
		holder.reply.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,CommentActivity.class);
				intent.putExtra("to_comment",holder.cmtObject.toString());
				intent.putExtra("to_User",holder.fromUser.toString());
				intent.putExtra("from",6);
				((Activity) mContext).startActivityForResult(intent,2);
			}
		});
		return holder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		holder.cmtObject=commentList.get(position);
		holder.toMaterial=holder.cmtObject.getAVObject("material");
		holder.fromUser=holder.cmtObject.getAVObject("from_User");
		holder.toUser=holder.cmtObject.getAVObject("to_User");
			AVQuery<AVObject> query = new AVQuery<>("Material_comment");
		    query.include("to_comment");
		    query.include("to_User");
		    query.include("from_User");
			query.whereEqualTo("to_comment",holder.cmtObject);
			query.findInBackground(new FindCallback<AVObject>() {
				@Override
				public void done(List<AVObject> list, AVException e){
					{
						if (e == null) {
							if(list.size()!=0){
								holder.childlist.clear();
								holder.childlist.addAll(list);

								holder.adapter.notifyDataSetChanged();

							}
						} else {
							e.printStackTrace();
						}
					}
				}
			});


		holder.head.setImageResource(R.drawable.def_ava_round);
		AVObject user=holder.cmtObject.getAVUser("from_User");
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
		if(!user.getString("nickname").equals("（请填写）")) {
			String str=user.getString("nickname")+ ":" + holder.cmtObject.getString("content");
			SpannableStringBuilder style;	//定义一个SpannableStringBuilder对象
			style = new SpannableStringBuilder(str);
			int start = str.indexOf(user.getString("nickname"));  //单词第一次出现的索引
			int end = start + user.getString("nickname").length();
			style.setSpan(new ForegroundColorSpan(Color.parseColor("#616161")), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.content.setText(str);
			holder.content.setText(style);
        }
        holder.date.setText(TimeUtils.dateToHMSString(holder.cmtObject.getCreatedAt()));
		AVQuery<AVObject> imgQuery=new AVQuery<>("cscmt_imagelist");
		imgQuery.whereEqualTo("from_material",holder.cmtObject);
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
								Glide.with(mContext).load(url).centerCrop().placeholder(R.drawable.placeholder).into(imageView);
								holder.imageList.addView(imageView);
							}

					}
				}
				else {

				}
			}
		});
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
