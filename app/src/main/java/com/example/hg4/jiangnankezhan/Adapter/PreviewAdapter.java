package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.R;

import java.util.List;

/**
 * Created by HG4 on 2017/10/4.
 */

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder> {
	private Context mContext;
	private List<String> mpathList;
	static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView prepic;
		public ViewHolder(View view){
			super(view);
			prepic=(ImageView)view;
			prepic.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}
	}
	public PreviewAdapter(List<String> pathList){
		mpathList=pathList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.preview_item,parent,false);
		ViewHolder viewHolder=new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		String picPath=mpathList.get(position);
		Glide.with(mContext).load(picPath).into(holder.prepic);
	}

	@Override
	public int getItemCount() {
		return mpathList.size();
	}
}
