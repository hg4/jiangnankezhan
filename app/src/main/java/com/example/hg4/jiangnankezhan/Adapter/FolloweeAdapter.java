package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.GetDataCallback;
import com.example.hg4.jiangnankezhan.MainPageActivity;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.List;

import cn.leancloud.chatkit.activity.LCIMConversationActivity;
import cn.leancloud.chatkit.utils.LCIMConstants;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2018/2/11.
 */

public class FolloweeAdapter extends RecyclerView.Adapter<FolloweeAdapter.ViewHolder> {
    private List<AVUser> followeelist;
    static class  ViewHolder extends RecyclerView.ViewHolder{
        AVObject followee;
        ImageView head;
        TextView nickname;
        ImageView edit;
        SharedPreferences pref;

        public ViewHolder(View view){
            super(view);
            head=(ImageView)view.findViewById(R.id.head);
            nickname=(TextView)view.findViewById(R.id.nickname);
            edit=(ImageView)view.findViewById(R.id.edit);
        }
    }
    public FolloweeAdapter(List<AVUser> fl){
        followeelist=fl;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.pref=parent.getContext().getSharedPreferences
                (AVUser.getCurrentUser().getObjectId()+"ifme",MODE_PRIVATE);
        Boolean ifme=holder.pref.getBoolean("ifme",false);
        if(ifme==true){
            holder.edit.setVisibility(View.VISIBLE);
        }
        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(parent.getContext(), MainPageActivity.class);
                intent.putExtra("user",holder.followee.toString());
                parent.getContext().startActivity(intent);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.followeeedit, null);
                final PopupWindow popupWindow = new PopupWindow(parent.getContext());
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(view1);
                popupWindow.setOutsideTouchable(false);
                popupWindow.setFocusable(true);
                int xpos=view.getMeasuredWidth();
                popupWindow.showAsDropDown(holder.edit,xpos, 0);
                TextView sendmess=(TextView)view1.findViewById(R.id.sendmess);
                TextView cancelfl=(TextView)view1.findViewById(R.id.cancelfl);
                sendmess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        Intent intent = new Intent(parent.getContext(), LCIMConversationActivity.class);
                        intent.putExtra(LCIMConstants.PEER_ID, holder.followee.getObjectId());
                        parent.getContext().startActivity(intent);
                    }
                });
                cancelfl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        AVUser.getCurrentUser().unfollowInBackground(holder.followee.getObjectId(), new FollowCallback() {
                            @Override
                            public void done(AVObject object, AVException e) {
                                if (e == null) {
                                    followeelist.remove(holder.followee);
                                    FolloweeAdapter.this.notifyDataSetChanged();
                                    Toast.makeText(parent.getContext(),"已取消关注",Toast.LENGTH_SHORT).show();
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,int position){
        holder.followee=followeelist.get(position);
        if(!holder.followee.getString("nickname").equals("（请填写）"))
            holder.nickname.setText(holder.followee.getString("nickname").toString());

        if(holder.followee.getAVFile("head")!=null){
            AVFile file=holder.followee.getAVFile("head");
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
        }else{
            holder.head.setImageResource(R.drawable.def_ava_round);
        }
    }
    @Override
    public int getItemCount(){
        return followeelist.size();
    }
}
