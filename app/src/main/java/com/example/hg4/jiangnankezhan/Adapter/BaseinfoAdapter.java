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
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.example.hg4.jiangnankezhan.CosContentActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.RequirementsActivity;
import com.example.hg4.jiangnankezhan.Utils.PopupWindowUtil;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HG4 on 2018/2/2.
 */


public class BaseinfoAdapter extends RecyclerView.Adapter<BaseinfoAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> mDataList=new ArrayList<>();
	static class ViewHolder extends RecyclerView.ViewHolder{
		private TextView sex;
		private TextView grade;
		private TextView college;
		private TextView education;
		private TextView major;

		private AVObject user;
		public ViewHolder(View view){
			super(view);
			sex=(TextView)view.findViewById(R.id.sex);
			grade=(TextView)view.findViewById(R.id.grade);
			college=(TextView)view.findViewById(R.id.college);
			education=(TextView)view.findViewById(R.id.education);
			major=(TextView)view.findViewById(R.id.major);
		}
	}

	public BaseinfoAdapter(List<AVObject> list){
		mDataList=list;
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.fragment_viewright,parent,false);
		final ViewHolder viewHolder=new ViewHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.user=mDataList.get(position);
		holder.sex.setText(holder.user.getString("sex"));
		holder.grade.setText(holder.user.getString("grade"));
		holder.college.setText(holder.user.getString("college"));
		holder.education.setText(holder.user.getString("education"));
		holder.major.setText(holder.user.getString("major"));
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}
}
