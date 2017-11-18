package com.example.hg4.jiangnankezhan.Adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
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
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.CommentActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
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
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.material_rely,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,CommentActivity.class);
                intent.putExtra("to_comment",holder.to_comment.toString());
                intent.putExtra("to_User",holder.to_User.toString());
                intent.putExtra("from",6);
                ((Activity) mContext).startActivityForResult(intent,1);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position){
        holder.cmtObject=matercomlist.get(position);
        holder.from_User=holder.cmtObject.getAVObject("from_User");
        holder.to_User=holder.cmtObject.getAVObject("to_User");
        holder.to_comment=holder.cmtObject.getAVObject("to_comment");

        holder.head.setImageResource(R.drawable.def_ava_round);
        if(holder.from_User.getAVFile("head")!=null){
            AVFile file=holder.from_User.getAVFile("head");
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
        if(!holder.from_User.getString("nickname").equals("（请填写）"))
            holder.content.setText(holder.from_User.getString("nickname") +"@"+holder.to_User.getString("nickname")+holder.cmtObject.getString("content"));
        holder.date.setText(TimeUtils.dateToString(holder.cmtObject.getUpdatedAt()));
        AVQuery<AVObject> imgQuery=new AVQuery<>("cscmt_imagelist");
        imgQuery.whereEqualTo("from_material",holder.cmtObject);
        imgQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e==null&&list.size()!=0){
                    for(AVObject image:list){
                        AVFile pic=image.getAVFile("image");
                        if(pic!=null){
                            final String url=pic.getUrl();
                            ImageView imageView=new ImageView(mContext);
                            imageView.setTag(R.id.tag_img,url);
                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(200,200);
                            layoutParams.setMargins(45,10,0,0);
                            imageView.setLayoutParams(layoutParams);
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Utilty.imgDetailDialog(mContext,url);
                                }
                            });
                            Glide.with(mContext).load(url).centerCrop().placeholder(R.drawable.placeholder).into(imageView);
                                holder.imageList.addView(imageView);
                            }

                    }
                }
                else {

                }
            }
        });
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
