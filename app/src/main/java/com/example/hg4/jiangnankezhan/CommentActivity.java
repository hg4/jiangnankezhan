package com.example.hg4.jiangnankezhan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.Adapter.PreviewAdapter;
import com.example.hg4.jiangnankezhan.Utils.GlideLoader;
import com.example.hg4.jiangnankezhan.Utils.Utilty;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private ImageView back;
    private EditText content;
    private TextView textnum;
    private RecyclerView preview;
    private ImageView addpic;
    private Button subCmt;
    private int maxNum = 100;
    private int type;
    private int from;
    private Intent intent;
    private String courseName;
    private String teacher;
    private ConstraintLayout pic_cmt;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> path = new ArrayList<>();
    private static final int FROM_REQUEST=1;
    private static final int FROM_COMMENT=2;
    private static final int FROM_CONTENT=3;
    public static final int REQUEST_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        back=(ImageView)findViewById(R.id.back);
        preview=(RecyclerView)findViewById(R.id.addcmt_picpreview);
        pic_cmt=(ConstraintLayout)findViewById(R.id.pic_cmt);
        LinearLayoutManager layoutManager=new LinearLayoutManager(CommentActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        preview.setLayoutManager(layoutManager);
        adapter=new PreviewAdapter(path);
        preview.setAdapter(adapter);
        addpic=(ImageView)findViewById(R.id.addcmt_addpic);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				setResult(0);
                CommentActivity.this.finish();
            }
        });
        content=(EditText)findViewById(R.id.textcomment);
        textnum=(TextView)findViewById(R.id.textnum);
        subCmt=(Button)findViewById(R.id.addcmt_comment);
        intent=getIntent();
        type=intent.getIntExtra("type",0);
        courseName=intent.getStringExtra("courseName");
        teacher=intent.getStringExtra("teacher");
        from=intent.getIntExtra("from",0);
        if(from==FROM_COMMENT){
            preview.setVisibility(View.INVISIBLE);
            addpic.setVisibility(View.INVISIBLE);
            pic_cmt.setVisibility(View.INVISIBLE);
        }
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String S = String.valueOf(maxNum-s.length());
                textnum.setText(S);
            }

        });
        addpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				if(ContextCompat.checkSelfPermission(CommentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
					ActivityCompat.requestPermissions(CommentActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
				}
                if(ContextCompat.checkSelfPermission(CommentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
					ImageConfig imageConfig=new ImageConfig.Builder(new GlideLoader())
							.mutiSelectMaxSize(3)
							.pathList(path)
							.requestCode(REQUEST_CODE)
							.build();
					ImageSelector.open(CommentActivity.this,imageConfig);
				}

            }
        });
        subCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from==FROM_COMMENT){
                    if(!"".equals(content.getText().toString())){
                        AVObject comment=new AVObject("cscmt_commentlist");
                        comment.put("from", AVUser.getCurrentUser());
                        comment.put("content",content.getText());
                        String cmtid=intent.getStringExtra("cmtid");
                        if(cmtid!=null){
                            AVObject toComment=AVObject.createWithoutData("Course_comment",cmtid);
                            comment.put("to",toComment);
                            comment.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e!=null){
                                        Toast.makeText(CommentActivity.this,"发表失败",Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    else {
                                        Toast.makeText(CommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                        setResult(1);
                                        CommentActivity.this.finish();
                                    }
                                }
                            });
                        }

                }
                }
                if(from==FROM_REQUEST){
                    if(!"".equals(content.getText().toString())){
                        AVObject comment=new AVObject("Course_comment");
                        comment.put("from", AVUser.getCurrentUser());
                        comment.put("type",type);
                        if(type==0){
                            comment.put("courseName",courseName);
                        }
                        else {
                            comment.put("courseName",courseName);
                            comment.put("teacher",teacher);
                        }
                        comment.put("content",content.getText());
                        comment.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e!=null){
                                    Toast.makeText(CommentActivity.this,"发表失败",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                else {
                                    Toast.makeText(CommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                    setResult(1);
                                    CommentActivity.this.finish();

                                }
                            }
                        });
                        if(path.size()!=0){
                            int index=adapter.getItemCount();
                            for(int i=0;i<index;i++){
                                preview.getChildAt(i);
                                AVObject imageFile=new AVObject("cscmt_imagelist");
                                imageFile.put("from",comment);
                                Bitmap bitmap=BitmapFactory.decodeFile(path.get(i));
                                byte[] bytes=Utilty.Bitmap2Bytes(bitmap);
                                AVFile file=new AVFile("image",bytes);
                                imageFile.put("image",file);
                                imageFile.saveInBackground();
                            }
                        }
                    }
                }
                if(from==FROM_CONTENT){
                    if(!"".equals(content.getText().toString())){
                        AVObject comment=new AVObject("Course_comment");
                        comment.put("from", AVUser.getCurrentUser());
                        comment.put("type",type);
                        if(type==3){
                            comment.put("courseName",courseName);
                        }
                        comment.put("content",content.getText());
                        comment.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e!=null){
                                    Toast.makeText(CommentActivity.this,"发表失败",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                else {
                                    Toast.makeText(CommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                    setResult(1);
                                    CommentActivity.this.finish();

                                }
                            }
                        });
                        if(path.size()!=0){
                            int index=adapter.getItemCount();
                            for(int i=0;i<index;i++){
                                preview.getChildAt(i);
                                AVObject imageFile=new AVObject("cscmt_imagelist");
                                imageFile.put("from",comment);
                                Bitmap bitmap=BitmapFactory.decodeFile(path.get(i));
                                byte[] bytes=Utilty.Bitmap2Bytes(bitmap);
                                AVFile file=new AVFile("image",bytes);
                                imageFile.put("image",file);
                                imageFile.saveInBackground();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }
            path.clear();
            path.addAll(pathList);

            adapter.notifyDataSetChanged();

        }
    }
}
