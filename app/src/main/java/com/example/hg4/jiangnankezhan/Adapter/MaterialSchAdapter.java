package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.hg4.jiangnankezhan.DownloadActivity;
import com.example.hg4.jiangnankezhan.R;

import java.util.List;

/**
 * Created by HG4 on 2017/10/27.
 */

public class MaterialSchAdapter extends RecyclerView.Adapter<MaterialSchAdapter.ViewHolder>{
	private List<AVObject> mMaterialList;
	private Context mContext;
	public static class ViewHolder extends RecyclerView.ViewHolder{
		View holderView;
		TextView text;
		public ViewHolder(View view){
			super(view);
			holderView=view;
			text=(TextView) view.findViewById(R.id.item_text);
		}
	}
	public MaterialSchAdapter(List<AVObject> MaterialList){
		mMaterialList=MaterialList;
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(mContext==null){
			mContext=parent.getContext();
		}
		View view= LayoutInflater.from(mContext).inflate(R.layout.material_item,parent,false);
		final ViewHolder viewHolder=new ViewHolder(view);
		viewHolder.holderView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,DownloadActivity.class);
				intent.putExtra("content",viewHolder.text.getText().toString());
				mContext.startActivity(intent);
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		AVObject material=mMaterialList.get(position);
		String title=material.getString("Title");
		holder.text.setText(title);
	}

	@Override
	public int getItemCount() {
		return mMaterialList.size();
	}
}
