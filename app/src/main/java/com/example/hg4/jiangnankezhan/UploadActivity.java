package com.example.hg4.jiangnankezhan;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class UploadActivity extends BaseActivity {
    private Dialog dialog;
    private ImageView back;
    private Button upload;
    private EditText title;
    private EditText introduce;
    private ImageView addfile;
    private String path;
    private AVFile file;
    private ImageView cha;
    private TextView textnum;
    private int maxNum = 90;
    private ToggleButton[] buttons=new ToggleButton[4];
    private Integer index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        back = (ImageView) findViewById(R.id.back);
        upload = (Button) findViewById(R.id.upload);
        title=(EditText)findViewById(R.id.title);
        introduce=(EditText)findViewById(R.id.introduce);
        addfile=(ImageView)findViewById(R.id.addfile);
        cha=(ImageView)findViewById(R.id.cha);
        textnum=(TextView)findViewById(R.id.textnum1);
        buttons[0]=(ToggleButton)findViewById(R.id.toggleButton1);
        buttons[1]=(ToggleButton)findViewById(R.id.toggleButton2);
        buttons[2]=(ToggleButton)findViewById(R.id.toggleButton3);
        buttons[3]=(ToggleButton)findViewById(R.id.toggleButton4);
        for(final ToggleButton button:buttons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.toggleButton1:
                            setUnselected();
                            button.setChecked(true);
                            v.setBackgroundColor(getResources().getColor(R.color.colorRed));
                            ((ToggleButton)v).setTextColor(getResources().getColor(R.color.white));
                            index=0;
                            break;
                        case R.id.toggleButton2:
                            setUnselected();
                            button.setChecked(true);
                            v.setBackgroundColor(getResources().getColor(R.color.colorRed));
                            ((ToggleButton)v).setTextColor(getResources().getColor(R.color.white));
                            index=1;
                            break;
                        case R.id.toggleButton3:
                            setUnselected();
                            button.setChecked(true);
                            v.setBackgroundColor(getResources().getColor(R.color.colorRed));
                            ((ToggleButton)v).setTextColor(getResources().getColor(R.color.white));
                            index=2;
                            break;
                        case R.id.toggleButton4:
                            setUnselected();
                            button.setChecked(true);
                            v.setBackgroundColor(getResources().getColor(R.color.colorRed));
                            ((ToggleButton)v).setTextColor(getResources().getColor(R.color.white));
                            index=3;
                            break;
                    }

                }
            });
        }
        introduce.addTextChangedListener(new TextWatcher() {
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadActivity.this.finish();
            }
        });
        addfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                if (ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    try {
                        startActivityForResult(Intent.createChooser(intent, "选择一个文件上传"), 1);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(UploadActivity.this, "请安装一个文件管理器", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index==null||TextUtils.isEmpty(title.getText().toString())){
                    Toast.makeText(UploadActivity.this, "积分或标题不能为空哦！", Toast.LENGTH_SHORT).show();
                }else if(addfile.getDrawable().getCurrent().getConstantState().equals(ContextCompat.getDrawable(UploadActivity.this,R.drawable.shangchuanok1).getConstantState())){
                        final AVObject coursefile = new AVObject("Course_file");
                                    coursefile.put("resource", file);
                                    coursefile.put("Title", title.getText().toString());
                                    coursefile.put("Introduce", introduce.getText().toString());
                                    coursefile.put("Course", getIntent().getStringExtra("courseName"));
                                    coursefile.put("owner", AVUser.getCurrentUser());
                                    coursefile.put("points",index);
                                    coursefile.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e == null) {
                                                setResult(RESULT_OK);
                                                AVQuery<AVObject> avQuery=new AVQuery<AVObject>("UserPoints");
                                                avQuery.whereEqualTo("User",AVUser.getCurrentUser());
                                                avQuery.getFirstInBackground(new GetCallback<AVObject>() {
                                                    @Override
                                                    public void done(AVObject avObject, AVException e) {
                                                        if(avObject!=null){
                                                            avObject.increment("points",2);
                                                            avObject.saveInBackground();
                                                            finish();
                                                            Toast.makeText(UploadActivity.this, "上传成功,积分+2", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            } else {
                                                e.printStackTrace();
                                                Toast.makeText(UploadActivity.this, "上传失败", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                }else{
                    Toast.makeText(UploadActivity.this,"请选择一个文件上传哦！",Toast.LENGTH_SHORT).show();
                }
                }

        });
    }

    private void setUnselected(){
        for(ToggleButton button:buttons){
            button.setChecked(false);
            button.setBackgroundColor(getResources().getColor(R.color.white));
            button.setTextColor(getResources().getColor(R.color.black));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    path = getPath(UploadActivity.this, uri);
                    File f=new File(path);
                    String fileName=f.getName();
                    String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
                    dialog = Utilty.createDiaglog(UploadActivity.this, "正在上传中...");
                    try {
                        file = AVFile.withAbsoluteLocalPath("file."+prefix, path);
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Utilty.dismissDiaglog(dialog, 1000);
                                    addfile.setImageResource(R.drawable.shangchuanok1);
                                    addfile.setClickable(false);
                                    cha.setImageResource(R.drawable.cha);
                                    cha.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            file.deleteInBackground(new DeleteCallback() {
                                                @Override
                                                public void done(AVException e) {
                                                    if (e==null){
                                                        cha.setImageResource(0);
                                                        addfile.setImageResource(R.drawable.choose);
                                                        addfile.setClickable(true);
                                                    }else{
                                                        Toast.makeText(UploadActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                } else {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
                }
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    @Override
    protected void onDestroy(){
          super.onDestroy();
    }
}
