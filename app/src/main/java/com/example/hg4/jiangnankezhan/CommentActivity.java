package com.example.hg4.jiangnankezhan;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.example.hg4.jiangnankezhan.Adapter.PreviewAdapter;
import com.example.hg4.jiangnankezhan.Utils.GlideLoader;
import com.example.hg4.jiangnankezhan.Utils.RegexUtil;
import com.example.hg4.jiangnankezhan.Utils.Utilty;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity {
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
    private static final int FROM_REPLY=4;
    private static final int FROM_MATERIAL=5;
    private static final int FROM_MATERIALCHD=6;
    public static final int REQUEST_CODE = 1000;
    private Dialog dialog;
    private AVObject user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        back=(ImageView)findViewById(R.id.back);
        preview=(RecyclerView)findViewById(R.id.addcmt_picpreview);
        pic_cmt=(ConstraintLayout)findViewById(R.id.pic_cmt);
        AVQuery<AVObject> query=new AVQuery<>("_User");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if(e==null){
                    if(avObject!=null){
                        user=avObject;
                    }
                }
            }
        });
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
        from=intent.getIntExtra("from",0);
        if(from==FROM_COMMENT||from==FROM_REPLY){
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
                dialog=Utilty.createDiaglog(CommentActivity.this,"发表中...");
                if(from==FROM_COMMENT){
                    if(commentCheck(content.getText().toString())){
                        final AVObject comment=new AVObject("cscmt_commentlist");
                        comment.put("from", AVUser.getCurrentUser());
                        comment.put("content",content.getText());
                        try{
                            final AVObject cmt=AVObject.parseAVObject(intent.getStringExtra("cmt"));
                            if(cmt!=null){
                                AVQuery<AVObject> query=new AVQuery<AVObject>("Course_comment");
                                query.getInBackground(cmt.getObjectId(), new GetCallback<AVObject>() {
                                    @Override
                                    public void done(final AVObject avObject, AVException e) {
                                        if(avObject!=null){
                                            comment.put("talkGroup",avObject.getInt("maxTalkGroup")+1);//获取最新的maxtalkgroup
                                            AVObject targetUser=cmt.getAVObject("from");
                                            comment.put("targetUser",targetUser);
                                            comment.put("to",cmt);
                                            comment.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(AVException e) {
                                                    if (e!=null){
                                                        Toast.makeText(CommentActivity.this,"发表失败",Toast.LENGTH_SHORT).show();
                                                        Utilty.dismissDiaglog(dialog,1000);
                                                        e.printStackTrace();
                                                    }
                                                    else {
                                                        //子评论发表成功后，父评论最大talk组+1
                                                        avObject.increment("maxTalkGroup");
                                                        avObject.increment("hot",2);
                                                        avObject.increment("commentCount");
                                                        avObject.saveInBackground();
                                                        if(content.getText().length()>=15){
                                                            Toast.makeText(CommentActivity.this,"发表成功,积分+1",Toast.LENGTH_SHORT).show();
                                                            addPoints();
                                                        }
                                                        else  Toast.makeText(CommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                                        Utilty.dismissDiaglog(dialog,1000);
                                                        setResult(1);
                                                        CommentActivity.this.finish();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        }
                        catch (Exception e){
                            Utilty.dismissDiaglog(dialog,1000);
                            e.printStackTrace();
                        }


                }
                else {
                        Toast.makeText(CommentActivity.this,"黄牌警告 请不要发表辣鸡评论！",Toast.LENGTH_SHORT).show();
                        Utilty.dismissDiaglog(dialog,0);
                    }
                }
                if(from==FROM_REPLY){
                    if(commentCheck(content.getText().toString())) {
                        final AVObject comment = new AVObject("cscmt_commentlist");
                        comment.put("from", AVUser.getCurrentUser());
                        comment.put("content", content.getText());
                        try{
                            AVObject reply=AVObject.parseAVObject(intent.getStringExtra("reply"));
                            if(reply!=null){
                                AVObject targetUser=reply.getAVObject("from");
                                comment.put("targetUser",targetUser);
                                comment.put("talkGroup",reply.getInt("talkGroup"));
                                comment.put("to",reply.getAVObject("to"));
                                AVObject tocmt=reply.getAVObject("to");
                                tocmt.increment("hot",2);
                                tocmt.increment("commentCount");
                                tocmt.saveInBackground();
                                comment.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e!=null){
                                                Toast.makeText(CommentActivity.this,"发表失败",Toast.LENGTH_SHORT).show();
                                                Utilty.dismissDiaglog(dialog,1000);
                                                e.printStackTrace();
                                            }
                                            else {
                                                if(content.getText().length()>=15){
                                                    Toast.makeText(CommentActivity.this,"发表成功,积分+1",Toast.LENGTH_SHORT).show();
                                                    addPoints();
                                                }
                                                else  Toast.makeText(CommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                                setResult(1);
                                                Utilty.dismissDiaglog(dialog,1000);
                                                CommentActivity.this.finish();
                                            }
                                        }
                                });
                            }
                        }
                        catch (Exception e){
                            Utilty.dismissDiaglog(dialog,1000);
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(CommentActivity.this,"黄牌警告 请不要发表辣鸡评论！",Toast.LENGTH_SHORT).show();
                        Utilty.dismissDiaglog(dialog,0);
                    }
                }
                if(from==FROM_REQUEST){
                    type=intent.getIntExtra("type",0);
                    courseName=intent.getStringExtra("courseName");
                    teacher=intent.getStringExtra("teacher");
                    if(commentCheck(content.getText().toString())){
                        incHot();
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
                                    Utilty.dismissDiaglog(dialog,1000);
                                    Toast.makeText(CommentActivity.this,"发表失败",Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                                else {
                                    Utilty.dismissDiaglog(dialog,1000);
                                    if(content.getText().length()>=15){
                                        Toast.makeText(CommentActivity.this,"发表成功,积分+1",Toast.LENGTH_SHORT).show();
                                        addPoints();
                                    }
                                    else  Toast.makeText(CommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
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
                    else {
                        Toast.makeText(CommentActivity.this,"黄牌警告 请不要发表辣鸡评论！",Toast.LENGTH_SHORT).show();
                        Utilty.dismissDiaglog(dialog,0);
                    }
                }
                if(from==FROM_CONTENT){
                    type=intent.getIntExtra("type",0);
                    courseName=intent.getStringExtra("courseName");
                    teacher=intent.getStringExtra("teacher");
                    if(commentCheck(content.getText().toString())){
                        incHot();
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
                                    Utilty.dismissDiaglog(dialog,1000);
                                    e.printStackTrace();
                                }
                                else {
                                    if(content.getText().length()>=15){
                                        Toast.makeText(CommentActivity.this,"发表成功,积分+1",Toast.LENGTH_SHORT).show();
                                        addPoints();
                                    }
                                    else  Toast.makeText(CommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                    setResult(1);
                                    Utilty.dismissDiaglog(dialog,1000);
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
                    else {
                        Toast.makeText(CommentActivity.this,"黄牌警告 请不要发表辣鸡评论！",Toast.LENGTH_SHORT).show();
                        Utilty.dismissDiaglog(dialog,0);
                    }
                }
                if(from==FROM_MATERIAL){//从资料评论
                        try{
                            AVObject material=AVObject.parseAVObject(intent.getStringExtra("material"));
                            AVObject toUser=AVObject.parseAVObject(intent.getStringExtra("to_User"));//download里的getowner
                            if(commentCheck(content.getText().toString())){
                                final AVObject comment=new AVObject("Material_comment");
                                comment.put("material",material);
                                comment.put("to_User",toUser);
                                comment.put("from_User",AVUser.getCurrentUser());
                                comment.put("content",content.getText());
                                comment.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if(path.size()!=0){
                                            int index=adapter.getItemCount();
                                            for(int i=0;i<index;i++){
                                                preview.getChildAt(i);
                                                AVObject imageFile=new AVObject("cscmt_imagelist");
                                                imageFile.put("from_material",comment);
                                                Bitmap bitmap=BitmapFactory.decodeFile(path.get(i));
                                                byte[] bytes=Utilty.Bitmap2Bytes(bitmap);
                                                AVFile file=new AVFile("image",bytes);
                                                imageFile.put("image",file);
                                                imageFile.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(AVException e) {
                                                        Intent intent=new Intent();
                                                        intent.putExtra("newcmt",comment.toString());
                                                        setResult(1,intent);
                                                        CommentActivity.this.finish();
                                                    }
                                                });
                                            }
                                        }
                                        else {
                                            Intent intent=new Intent();
                                            intent.putExtra("newcmt",comment.toString());
                                            if(content.getText().length()>=15){
                                                Toast.makeText(CommentActivity.this,"发表成功,积分+1",Toast.LENGTH_SHORT).show();
                                                addPoints();
                                            }
                                            else  Toast.makeText(CommentActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                            setResult(1,intent);
                                            CommentActivity.this.finish();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(CommentActivity.this,"黄牌警告 请不要发表辣鸡评论！",Toast.LENGTH_SHORT).show();
                                Utilty.dismissDiaglog(dialog,0);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                if(from==FROM_MATERIALCHD){
                    try{
                        AVObject to_comment=AVObject.parseAVObject(intent.getStringExtra("to_comment"));
                        AVObject to_childcmt=null;
                        if(intent.getStringExtra("to_childcmt")!=null){
                            to_childcmt=AVObject.parseAVObject(intent.getStringExtra("to_childcmt"));
                        }
                        AVObject toUser=AVObject.parseAVObject(intent.getStringExtra("to_User"));
                        if(commentCheck(content.getText().toString())){
                            final AVObject comment=new AVObject("Material_comment");
                            if(intent.getStringExtra("to_childcmt")!=null)
                                comment.put("to_childcmt",to_childcmt);
                            comment.put("to_comment",to_comment);
                            comment.put("to_User",toUser);
                            comment.put("from_User",AVUser.getCurrentUser());
                            comment.put("content",content.getText());
                            comment.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if(path.size()!=0){
                                        int index=adapter.getItemCount();
                                        for(int i=0;i<index;i++){
                                            preview.getChildAt(i);
                                            AVObject imageFile=new AVObject("cscmt_imagelist");
                                            imageFile.put("from_material",comment);
                                            Bitmap bitmap=BitmapFactory.decodeFile(path.get(i));
                                            byte[] bytes=Utilty.Bitmap2Bytes(bitmap);
                                            AVFile file=new AVFile("image",bytes);
                                            imageFile.put("image",file);
                                            imageFile.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(AVException e) {
                                                    Intent intent=new Intent();
                                                    intent.putExtra("newcmt",comment.toString());
                                                    setResult(2,intent);
                                                    CommentActivity.this.finish();
                                                }
                                            });
                                        }
                                    }
                                    else {
                                        Intent intent=new Intent();
                                        intent.putExtra("newcmt",comment.toString());
                                        setResult(2,intent);
                                        CommentActivity.this.finish();
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(CommentActivity.this,"黄牌警告 请不要发表辣鸡评论！",Toast.LENGTH_SHORT).show();
                            Utilty.dismissDiaglog(dialog,0);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
 /*  private void savePermission(){
        user=AVObject.createWithoutData("_User",id);
        user.put("permissionD",true);
        user.saveInBackground();
    }*/
    private boolean commentCheck(String content){
        boolean isWordorNumber=content.matches("^[A-Za-z0-9 ]+$");
        boolean isNull="".equals(content);
        return !isNull&&!isWordorNumber;
    }
    private void addPoints(){
        if(user!=null){
            AVQuery avQuery=new AVQuery("UserPoints");
            avQuery.whereEqualTo("User",user);
            avQuery.getFirstInBackground(new GetCallback() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if(avObject!=null){
                        avObject.increment("points");
                        avObject.saveInBackground();
                    }
                }
            });
        }
    }
    private void incHot(){
        AVQuery<AVObject> avQuery=new AVQuery<>("Course");
        avQuery.whereEqualTo("courseName",courseName);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list!=null){
                    for(AVObject obj:list){
                        obj.increment("hot",2);
                        obj.saveInBackground();
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
