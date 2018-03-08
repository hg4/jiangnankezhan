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
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.callback.AVFriendshipCallback;
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

public class HomeUserAdapter extends RecyclerView.Adapter<HomeUserAdapter.ViewHolder> {
    private List<AVObject> userlist;
    static class  ViewHolder extends RecyclerView.ViewHolder{
        AVObject user;
        ImageView head;
        TextView nickname;
        ImageView edit;

        public ViewHolder(View view){
            super(view);
            head=(ImageView)view.findViewById(R.id.head);
            nickname=(TextView)view.findViewById(R.id.nickname);
            edit=(ImageView)view.findViewById(R.id.edit);
        }
    }
    public HomeUserAdapter(List<AVObject> fl){
        userlist=fl;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.edit.setVisibility(View.VISIBLE);
       holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(parent.getContext(), MainPageActivity.class);
                intent.putExtra("user",holder.user.toString());
                parent.getContext().startActivity(intent);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.hotuseredit, null);
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
                final TextView iffollow=(TextView)view1.findViewById(R.id.iffollow);
                TextView mainpage=(TextView)view1.findViewById(R.id.mainpage);
                AVFriendshipQuery query = AVUser.friendshipQuery(holder.user.getObjectId(), AVUser.class);
                query.include("follower");
                query.getInBackground(new AVFriendshipCallback() {
                    @Override
                    public void done(AVFriendship friendship, AVException e) {
                        if(e==null){
                            List<AVUser> followers = friendship.getFollowers(); //获取粉丝
                            for(int i=0; i<followers.size(); i++){
                                if(followers.get(i).getObjectId().equals(AVUser.getCurrentUser().getObjectId())){
                                    iffollow.setText("已关注");
                                }
                            }
                        }else{
                            e.printStackTrace();
                        }
                    }
                });
                iffollow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(iffollow.getText().equals("+关注")){
                            AVUser.getCurrentUser().followInBackground(holder.user.getObjectId(), new FollowCallback() {
                                @Override
                                public void done(AVObject object, AVException e) {
                                    if (e == null) {
                                        Toast.makeText(parent.getContext(),"已关注",Toast.LENGTH_SHORT).show();
                                        iffollow.setText("已关注");
                                    } else if (e.getCode() == AVException.DUPLICATE_VALUE) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            popupWindow.dismiss();
                        }
                    }
                });
                sendmess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        Intent intent = new Intent(parent.getContext(), LCIMConversationActivity.class);
                        intent.putExtra(LCIMConstants.PEER_ID, holder.user.getObjectId());
                        parent.getContext().startActivity(intent);
                    }
                });
                mainpage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        Intent intent=new Intent(parent.getContext(), MainPageActivity.class);
                        intent.putExtra("user",holder.user.toString());
                        parent.getContext().startActivity(intent);
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,int position){
        holder.user=userlist.get(position);
        if(!holder.user.getString("nickname").equals("（请填写）"))
            holder.nickname.setText(holder.user.getString("nickname"));
        holder.head.setImageResource(R.drawable.def_ava_round);
        if(holder.user.getAVFile("head")!=null){
            AVFile file=holder.user.getAVFile("head");
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
    }
    @Override
    public int getItemCount(){
        return userlist.size();
    }
}
