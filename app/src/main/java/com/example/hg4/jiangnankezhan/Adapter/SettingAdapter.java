package com.example.hg4.jiangnankezhan.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Setting;

import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by Administrator on 2017/9/5.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {
    private List<Setting> settingList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView item;
        ImageButton select;

        public ViewHolder(View view){
            super(view);
            item=(TextView)view.findViewById(R.id.item);
            select=(ImageButton)view.findViewById(R.id.select);
        }
    }
    public SettingAdapter(List<Setting> settingList){
        this.settingList=settingList;}
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Setting setting = settingList.get(position);
        holder.item.setText(setting.getItem());
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
        return settingList.size();
    }
}
