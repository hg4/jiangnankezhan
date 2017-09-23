package com.example.hg4.jiangnankezhan.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hg4.jiangnankezhan.Model.Info;
import com.example.hg4.jiangnankezhan.R;

import java.util.List;

/**
 * Created by HG4 on 2017/8/29.
 */

public class BaseInfoAdapter extends RecyclerView.Adapter<BaseInfoAdapter.ViewHolder> {
	private List<Info> infoList;
	static class ViewHolder extends RecyclerView.ViewHolder{
		TextView baseKey;
		TextView baseValue;
		ImageButton edit;

		public ViewHolder(View view){
			super(view);
			baseKey=(TextView)view.findViewById(R.id.info_key);
			baseValue=(TextView)view.findViewById(R.id.info_value);
			edit=(ImageButton)view.findViewById(R.id.info_edit);
		}
	}
	public BaseInfoAdapter(List<Info> infoList){this.infoList=infoList;}
	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.baseinfo_item,parent,false);
		ViewHolder viewHolder=new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {

			Info info = infoList.get(position);
			holder.baseKey.setText(info.getKey());
			holder.baseValue.setText(info.getValue());
		if (mOnItemClickLitener != null) {
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos=holder.getLayoutPosition();
					mOnItemClickLitener.onItemClick(holder.itemView,pos);
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return infoList.size();
	}
}
