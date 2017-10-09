package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.hg4.jiangnankezhan.CosDetailsActivity;
import com.example.hg4.jiangnankezhan.R;

import java.util.List;

/**
 * Created by HG4 on 2017/10/2.
 */

public class SechcsAdapter extends RecyclerView.Adapter<SechcsAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> mCourseList;

	static class ViewHolder extends RecyclerView.ViewHolder{
		CardView cardView;
		TextView courseName;
		TextView classroom;
		TextView time;
		String date;
		TextView teacher;
		ImageButton detail;
		ImageButton add;

		public ViewHolder(View view){
			super(view);
			cardView=(CardView)view;
			courseName=(TextView)view.findViewById(R.id.csitem_coursename);
			classroom=(TextView)view.findViewById(R.id.csitem_classroom);
			time=(TextView)view.findViewById(R.id.csitem_time);
			teacher=(TextView)view.findViewById(R.id.csitem_teacher);
			detail=(ImageButton)view.findViewById(R.id.csitem_detail);
			add=(ImageButton)view.findViewById(R.id.csitem_add);
		}
	}

	public SechcsAdapter(List<AVObject> mCourseList){
		this.mCourseList=mCourseList;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		AVObject course=mCourseList.get(position);
		holder.courseName.setText(course.getString("courseName"));
		holder.classroom.setText(course.getString("classroom"));
		holder.time.setText(course.getString("weekLength")+" "+course.getString("duration"));
		holder.teacher.setText(course.getString("teacher"));
		holder.date=course.getString("date");

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.sechcs_item,parent,false);
		final ViewHolder viewHolder=new ViewHolder(view);
		viewHolder.detail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//查看课程详情
				Intent intent=new Intent(mContext, CosDetailsActivity.class);
				intent.putExtra("name",viewHolder.courseName.getText().toString());
				intent.putExtra("date",viewHolder.date);
				intent.putExtra("teacher",viewHolder.teacher.getText().toString());
				mContext.startActivity(intent);
			}
		});
		viewHolder.add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//添加到课表
			}
		});
		return viewHolder;
	}

	@Override
	public int getItemCount() {
		return mCourseList.size();
	}
}
