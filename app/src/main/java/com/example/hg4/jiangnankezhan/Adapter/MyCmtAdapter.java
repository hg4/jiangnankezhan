package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.LogUtil;
import com.example.hg4.jiangnankezhan.CosContentActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.RequirementsActivity;
import com.example.hg4.jiangnankezhan.Utils.PopupWindowUtil;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2017/10/19.
 */

public class MyCmtAdapter extends RecyclerView.Adapter<MyCmtAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> mCommentList=new ArrayList<>();
	static class ViewHolder extends RecyclerView.ViewHolder{
		TextView dataName;
		TextView dataDate;
		TextView dataContent;
		ImageButton editPop;
		AVObject cmtData;
		int position;
		ConstraintLayout layout_holder;
		public ViewHolder(View view){
			super(view);
			layout_holder=(ConstraintLayout)view;
			dataName=(TextView) view.findViewById(R.id.mycomment_dataname);
			dataDate=(TextView) view.findViewById(R.id.mycomment_date);
			dataContent=(TextView) view.findViewById(R.id.mycomment_content);
			editPop=(ImageButton)view.findViewById(R.id.mycomment_pop);
		}
	}

	public MyCmtAdapter(List<AVObject> list){
		mCommentList=list;
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.mycomment_item,parent,false);
		final ViewHolder viewHolder=new ViewHolder(view);
		viewHolder.layout_holder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到具体评论
				int type=viewHolder.cmtData.getInt("type");
				if(type==3){
					Intent intent=new Intent(mContext, CosContentActivity.class);
					intent.putExtra("courseName",viewHolder.cmtData.getString("courseName"));
					intent.putExtra("teacher",viewHolder.cmtData.getString("teacher"));
					mContext.startActivity(intent);
				}
				else {
					Intent intent=new Intent(mContext, RequirementsActivity.class);
					intent.putExtra("courseName",viewHolder.cmtData.getString("courseName"));
					intent.putExtra("teacher",viewHolder.cmtData.getString("teacher"));
					intent.putExtra("type",viewHolder.cmtData.getInt("type"));
					mContext.startActivity(intent);
				}

			}
		});
		viewHolder.editPop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> data=new ArrayList<>();
				data.add("删除");
				final PopupWindowUtil popupWindow=new PopupWindowUtil(mContext,data);
				AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						switch (position){
							case 0:
								viewHolder.cmtData.deleteInBackground(new DeleteCallback() {
									@Override
									public void done(AVException e) {
										if(e==null){
											Log.e("delete","ok");
											mCommentList.remove(viewHolder.position);
											MyCmtAdapter.this.notifyDataSetChanged();
											popupWindow.dismiss();
										}
									}
								});
						}
					}
				};
				popupWindow.setItemClickListener(itemClickListener);
				popupWindow.setOff(-100,10);
				popupWindow.show(viewHolder.editPop,2);
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		AVObject myComment=mCommentList.get(position);
		holder.cmtData=myComment;
		holder.position=position;
		holder.dataName.setText(myComment.getString("courseName"));
		holder.dataDate.setText(TimeUtils.dateToHMSString(myComment.getCreatedAt()));
		holder.dataContent.setText(myComment.getString("content"));
	}

	@Override
	public int getItemCount() {
		return mCommentList.size();
	}
}
