package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVFriendshipQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.callback.AVFriendshipCallback;
import com.example.hg4.jiangnankezhan.Adapter.FolloweeAdapter;

import java.util.ArrayList;
import java.util.List;

public class FolloweeActivity extends AppCompatActivity {
    private List<AVUser> followeelist = new ArrayList<>();
    private RecyclerView fl;
    private ImageView back;
    private AVUser aimuser;
    private ImageView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followee);
        fl = (RecyclerView) findViewById(R.id.followeelist);
        back = (ImageView) findViewById(R.id.back);
        add=(ImageView)findViewById(R.id.add);
        String userid = getIntent().getStringExtra("aimuser");
        AVFriendshipQuery query = AVUser.friendshipQuery(userid, AVUser.class);
        query.include("followee");
        query.getInBackground(new AVFriendshipCallback() {
            @Override
            public void done(AVFriendship friendship, AVException e) {
                if (e == null) {
                    aimuser = friendship.getUser();
                    SharedPreferences.Editor editor = getSharedPreferences
                            (AVUser.getCurrentUser().getObjectId() + "ifme", MODE_PRIVATE).edit();
                    if(aimuser.getObjectId().toString().equals(AVUser.getCurrentUser().getObjectId().toString())){
                        add.setVisibility(View.VISIBLE);
                        editor.putBoolean("ifme",true);
                        editor.apply();
                    }else{
                        editor.putBoolean("ifme",false);
                        editor.apply();
                    }
                    List<AVUser> followees = friendship.getFollowees();
                    followeelist = followees;
                    GridLayoutManager layoutManager = new GridLayoutManager(FolloweeActivity.this, 3);
                    fl.setLayoutManager(layoutManager);
                    FolloweeAdapter adapter = new FolloweeAdapter(followeelist);
                    fl.setAdapter(adapter);
                } else {
                    e.printStackTrace();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FolloweeActivity.this,FollowSearchActivity.class);
                startActivity(intent);
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
