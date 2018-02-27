package com.example.hg4.jiangnankezhan;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.callback.AVFriendshipCallback;
import com.example.hg4.jiangnankezhan.Adapter.FollowerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FollowerActivity extends AppCompatActivity {
    private List<AVUser> followerlist=new ArrayList<>();
    private  RecyclerView fl;
    private ImageView back;
    private ImageView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        fl=(RecyclerView)findViewById(R.id.followerlist);
        back=(ImageView)findViewById(R.id.back);
        search=(ImageView)findViewById(R.id.follower_sech);
        final String userid=getIntent().getStringExtra("aimuser");
        AVFriendshipQuery query = AVUser.friendshipQuery(userid, AVUser.class);
        query.include("follower");
        query.getInBackground(new AVFriendshipCallback() {
            @Override
            public void done(AVFriendship friendship, AVException e) {
                if(e==null){
                    SharedPreferences.Editor editor = getSharedPreferences
                            (AVUser.getCurrentUser().getObjectId()+"ifme",MODE_PRIVATE).edit();
                   if(userid.equals(AVUser.getCurrentUser().getObjectId().toString())) {
                      /* search.setVisibility(View.VISIBLE);*/
                       editor.putBoolean("ifme",true);
                       editor.apply();
                   }else{
                       editor.putBoolean("ifme",false);
                       editor.apply();
                   }
                    List<AVUser> followers = friendship.getFollowers(); //获取粉丝
                    followerlist=followers;
                    GridLayoutManager layoutManager = new GridLayoutManager(FollowerActivity.this, 3);
                    fl.setLayoutManager(layoutManager);
                    FollowerAdapter adapter=new FollowerAdapter(followerlist);
                    fl.setAdapter(adapter);
                }else{
                    e.printStackTrace();
                }
            }
        });
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
    }
}
