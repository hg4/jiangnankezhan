package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by HG4 on 2017/11/13.
 */

public class MyMaterialAdapter extends RecyclerView.Adapter<MyMaterialAdapter.ViewHolder> {
	private Context mContext;
	private List<AVObject> dataList;
	public static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView head;
		TextView username;
		TextView materialName;
		TextView date;
		TextView content;
		TextView likeCount;
		AVObject file;
		AVObject owner;
		public ViewHolder(View view){
			super(view);
			head=(ImageView)view.findViewById(R.id.head);
			username=(TextView)view.findViewById(R.id.owner);
			materialName=(TextView)view.findViewById(R.id.materialname);
			date=(TextView)view.findViewById(R.id.time);
			content=(TextView)view.findViewById(R.id.content);
			likeCount=(TextView)view.findViewById(R.id.likenumber);
		}
	}
	public MyMaterialAdapter(List<AVObject> mList){
		dataList=mList;
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.mymater_item,parent,false);
		ViewHolder viewHolder=new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		AVObject fileObject=dataList.get(position);
		if(fileObject!=null){
			holder.file=fileObject;
			holder.owner=fileObject.getAVObject("owner");
			Glide.with(mContext).load(holder.owner.getAVFile("head").getUrl()).into(holder.head);
			holder.username.setText(holder.owner.getString("nickname"));
			holder.content.setText(fileObject.getString("Introduce"));
			holder.materialName.setText(fileObject.getString("Title"));
			holder.date.setText(TimeUtils.dateToString(fileObject.getUpdatedAt()));
			holder.likeCount.setText(fileObject.getInt("likeCount")+"");
		}
	}

	@Override
	public int getItemCount() {
		return dataList.size();
	}
}
