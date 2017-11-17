package com.example.hg4.jiangnankezhan.Adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetDataCallback;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class UniChildCmtAdapter extends RecyclerView.Adapter<UniChildCmtAdapter.ViewHolder>{

    private List<AVObject> matercomlist=new ArrayList<>();
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        AVObject cmtObject;
        AVObject to_User;
        AVObject from_User;
        AVObject to_comment;
        TextView content;
        ImageView head;
        TextView date;
        TextView reply;
        LinearLayout imageList;
        public ViewHolder(View view){
            super(view);
            head=(ImageView)view.findViewById(R.id.head);
            date=(TextView)view.findViewById(R.id.time);
            content=(TextView)view.findViewById(R.id.content);
            reply=(TextView)view.findViewById(R.id.reply);
            imageList=(LinearLayout)view.findViewById(R.id.imagelist);
        }
    }

    public UniChildCmtAdapter(List<AVObject> List){
        matercomlist=List;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.material_rely,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转评论界面
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder,int position){
        AVObject comment=matercomlist.get(position);
        if(comment.getAVUser("from_User").getAVFile("head")!=null){
            AVFile file=comment.getAVUser("from_User").getAVFile("head");
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
        holder.date.setText(comment.getUpdatedAt().toString());
        if(!comment.getAVUser("from").getString("nickname").equals("（请填写）")) {
            holder.content.setText(comment.getAVUser("from").getString("nickname") +":"+ comment.getString("content"));
        }


    }


    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.imageList.removeAllViews();
    }
    @Override
    public int getItemCount() {
        return  matercomlist.size();
    }

}
