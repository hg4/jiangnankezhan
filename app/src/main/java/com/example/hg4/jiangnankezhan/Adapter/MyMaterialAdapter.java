package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.MainPageActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.PopupWindowUtil;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

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
		ImageButton editPop;
		AVObject file;
		AVObject owner;
		int position;
		public ViewHolder(View view){
			super(view);
			head=(ImageView)view.findViewById(R.id.head);
			username=(TextView)view.findViewById(R.id.owner);
			materialName=(TextView)view.findViewById(R.id.materialname);
			date=(TextView)view.findViewById(R.id.date);
			content=(TextView)view.findViewById(R.id.content);
			likeCount=(TextView)view.findViewById(R.id.likenumber);
			editPop=(ImageButton)view.findViewById(R.id.mycomment_pop);
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
		final ViewHolder viewHolder=new ViewHolder(view);
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
								AVQuery<AVObject> query=new AVQuery<AVObject>("Course_file");
								query.whereEqualTo("objectId",viewHolder.file.getObjectId());
								query.deleteAllInBackground(new DeleteCallback() {
									@Override
									public void done(AVException e) {
										if(e==null){
											Log.e("delete","ok");
											dataList.remove(viewHolder.position);
											MyMaterialAdapter.this.notifyDataSetChanged();
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
		AVObject fileObject=dataList.get(position);
		holder.position=position;
		if(fileObject!=null){
			holder.file=fileObject;
			holder.owner=fileObject.getAVObject("owner");
			Glide.with(mContext).load(holder.owner.getAVFile("head").getUrl()).into(holder.head);
			holder.username.setText(holder.owner.getString("nickname"));
			holder.content.setText(fileObject.getString("Introduce"));
			holder.materialName.setText(fileObject.getString("Title"));
			holder.date.setText(TimeUtils.dateToString(fileObject.getCreatedAt()));
			holder.likeCount.setText(fileObject.getInt("likeCount")+"");
		}
	}

	@Override
	public int getItemCount() {
		return dataList.size();
	}
}
