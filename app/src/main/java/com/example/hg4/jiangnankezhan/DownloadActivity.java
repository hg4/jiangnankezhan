package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.example.hg4.jiangnankezhan.Adapter.MaterialCommentAdapter;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.util.ArrayList;
import java.util.List;

import static com.example.hg4.jiangnankezhan.R.id.teacher;

public class DownloadActivity extends BaseActivity {
    private TextView materialname;
    private TextView owner;
    private TextView date;
    private TextView content;
    private ImageView head;
    private ImageView back;
    private Button download;
    private ImageView like;
    private ImageView comment;
    private TextView likenumber;
    private TextView commentnumber;
    private List<AVObject> matercomlist=new ArrayList<>();
    private RecyclerView commentlist;
    private MaterialCommentAdapter adapter;
    private AVObject user;
    private String courseName;
    private String teacher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        materialname = (TextView) findViewById(R.id.materialname);
        back = (ImageView) findViewById(R.id.back);
        owner = (TextView) findViewById(R.id.owner);
        date = (TextView) findViewById(R.id.time);
        content = (TextView) findViewById(R.id.content);
        head = (ImageView) findViewById(R.id.head);
        download = (Button) findViewById(R.id.download);
        like=(ImageView)findViewById(R.id.like);
        likenumber=(TextView)findViewById(R.id.likenumber);
        comment=(ImageView)findViewById(R.id.comment);
        commentnumber=(TextView)findViewById(R.id.commentnumber);

        commentlist=(RecyclerView)findViewById(R.id.commentlist);
        adapter=new MaterialCommentAdapter(matercomlist);
        commentlist.setAdapter(adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        commentlist.setLayoutManager(layoutManager);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like.setImageResource(R.drawable.comment_like);
                int newlikenumber=Integer.parseInt(likenumber.getText().toString())+1;
                likenumber.setText(String.valueOf(newlikenumber));
                AVQuery<AVObject> query = new AVQuery<>("Course_file");
                query.whereEqualTo("Title", getIntent().getStringExtra("content"));
                query.getFirstInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        {
                            if (e == null) {
                                avObject.put("likeCount",Integer.parseInt(likenumber.getText().toString()));
                                avObject.saveInBackground();
                                like.setClickable(false);
                            } else {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DownloadActivity.this,CommentActivity.class);
                intent.putExtra("materialName",getIntent().getStringExtra("content"));
                intent.putExtra("from",5);
                startActivityForResult(intent,1);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadActivity.this.finish();
            }
        });
        materialname.setText(getIntent().getStringExtra("content"));
        AVQuery<AVObject> query = new AVQuery<>("Course_file");
        query.whereEqualTo("Title", getIntent().getStringExtra("content"));
        query.include("owner");
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                {
                    if (e == null) {
                        if(!avObject.getString("Introduce").equals("")){
                            content.setText(avObject.getString("Introduce"));
                        }
                        date.setText(TimeUtils.dateToString(avObject.getCreatedAt()));
                        AVObject user = avObject.getAVObject("owner");
                        owner.setText(user.getString("nickname"));
                        AVFile file = user.getAVFile("head");
                        likenumber.setText(avObject.getNumber("likeCount").toString());
                        commentnumber.setText(avObject.getNumber("commentCount").toString());
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                if (e == null && bytes != null) {
                                    Bitmap head1 = Utilty.Bytes2Bimap(bytes);
                                    head.setImageBitmap(head1);
                                } else e.printStackTrace();
                            }
                        });
                    } else {
                        e.printStackTrace();
                        Toast.makeText(DownloadActivity.this, "查找失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null){
                    if(user.getBoolean("permissionD")){
                        AVQuery<AVObject> query = new AVQuery<>("Course_file");
                        query.whereEqualTo("Title", getIntent().getStringExtra("content"));
                        query.getFirstInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avObject, AVException e) {
                                {
                                    if (e == null) {
                                        Uri uri = Uri.parse(avObject.getAVFile("resource").getUrl());
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    } else {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
                    }
                    else {
                        final FrameLayout downLimit=(FrameLayout) getLayoutInflater().inflate(R.layout.limit_dialog, null);
                        AlertDialog.Builder limitBuilder=new AlertDialog.Builder(DownloadActivity.this);
                        limitBuilder.setView(downLimit);
                        final AlertDialog limitDialog=limitBuilder.create();
                        limitDialog.show();
                        Button jump=(Button)downLimit.findViewById(R.id.jump);
                        jump.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(DownloadActivity.this,RequirementsActivity.class);
                                intent.putExtra("teacher",teacher);
                                intent.putExtra("courseName",courseName);
                                limitDialog.dismiss();
                                startActivity(intent);
                            }
                        });
                        Button cancel=(Button)downLimit.findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                limitDialog.dismiss();
                            }
                        });
                    }
                }

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=0){
            switch (requestCode){
                case 1:
                    break;
            }
        }
    }
}
