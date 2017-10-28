package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

public class DownloadActivity extends BaseActivity {
    private TextView materialname;
    private TextView owner;
    private TextView date;
    private TextView content;
    private ImageView head;
    private ImageView back;
    private Button download;
    private ImageView like;
    private TextView likenumber;

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
                        content.setText(avObject.getString("Introduce"));
                        date.setText(TimeUtils.dateToString(avObject.getCreatedAt()));
                        AVObject user = avObject.getAVObject("owner");
                        owner.setText(user.getString("nickname"));
                        AVFile file = user.getAVFile("head");
                        likenumber.setText(avObject.getNumber("likeCount").toString());
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
        });

    }
}
