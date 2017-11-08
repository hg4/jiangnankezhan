package com.example.hg4.jiangnankezhan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.Adapter.PreviewAdapter;
import com.example.hg4.jiangnankezhan.Utils.GlideLoader;
import com.example.hg4.jiangnankezhan.Utils.Utilty;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import java.util.ArrayList;
import java.util.List;

public class SuggestionActivity extends AppCompatActivity {
  private ImageView back;
    private EditText suggtext;
    private int maxNum = 100;
    private TextView textnumber;
    private ConstraintLayout comques;
    private ImageView suggaddpic;
    private RecyclerView picpreview;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> path = new ArrayList<>();
    private Button suggsubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SuggestionActivity.this.finish();
            }
        });
        suggtext=(EditText)findViewById(R.id.suggtext);
        textnumber=(TextView)findViewById(R.id.textnumber);
        comques=(ConstraintLayout)findViewById(R.id.comques);
        suggaddpic=(ImageView)findViewById(R.id.suggaddpic);
        comques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SuggestionActivity.this,ComquesListActivity.class),1);
            }
        });
        picpreview=(RecyclerView)findViewById(R.id.picpreview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(SuggestionActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        picpreview.setLayoutManager(layoutManager);
        adapter=new PreviewAdapter(path);
        picpreview.setAdapter(adapter);
        suggsubmit=(Button)findViewById(R.id.suggsubmit);
        suggtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String S = String.valueOf(maxNum-s.length());
                textnumber.setText(S);
            }

        });
        suggaddpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(SuggestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SuggestionActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                if(ContextCompat.checkSelfPermission(SuggestionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    ImageConfig imageConfig=new ImageConfig.Builder(new GlideLoader())
                            .mutiSelectMaxSize(3)
                            .pathList(path)
                            .requestCode(2)
                            .build();
                    ImageSelector.open(SuggestionActivity.this,imageConfig);
                }

            }
        });
        suggsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!suggtext.getText().toString().equals("")){
                    if(!"".equals(suggtext.getText().toString())){
                        AVObject Suggestion=new AVObject("Suggestions");
                        Suggestion.put("content",suggtext.getText());
                        Suggestion.put("from", AVUser.getCurrentUser());
                        Suggestion.saveInBackground();
                    }
                    if(path.size()!=0){
                        int index=adapter.getItemCount();
                        for(int i=0;i<index;i++){
                            picpreview.getChildAt(i);
                            AVObject pic=new AVObject("SuggestionPics");
                            pic.put("from", AVUser.getCurrentUser());
                            Bitmap bitmap=BitmapFactory.decodeFile(path.get(i));
                            byte[] bytes=Utilty.Bitmap2Bytes(bitmap);
                            AVFile file=new AVFile("image",bytes);
                            pic.put("picture",file);
                            pic.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e!=null){
                                        Toast.makeText(SuggestionActivity.this,"发表失败",Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                    else {
                                        Toast.makeText(SuggestionActivity.this,"发表成功",Toast.LENGTH_SHORT).show();
                                        SuggestionActivity.this.finish();
                                    }
                                }
                            });
                        }
                    }
                }else{
                   Toast.makeText(SuggestionActivity.this,"请简单描述您的意见哦！",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=0){
            switch (requestCode){
                case 1:
                    String question=data.getStringExtra("question");
                    suggtext.setText(suggtext.getText().toString()+question+"、");
                    suggtext.setSelection(suggtext.getText().length());
                    break;
                case 2:
                    if (data != null) {
                        List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                        path.clear();
                        path.addAll(pathList);
                        adapter.notifyDataSetChanged();
                    }
                    break;

            }
        }

    }
}
