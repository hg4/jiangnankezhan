package com.example.hg4.jiangnankezhan;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.example.hg4.jiangnankezhan.Adapter.SettingAdapter;

import java.util.ArrayList;
import java.util.List;


public class SettingActivity extends AppCompatActivity {
    private RecyclerView settingView;
    private List<Setting> settingList=new ArrayList<>();
    private ImageView back;
    private SettingAdapter settingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingView=(RecyclerView)findViewById(R.id.recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        settingView.setLayoutManager(layoutManager);
        settingView.setHasFixedSize(true);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });
        settingAdapter=new SettingAdapter(settingList);
        settingView.setAdapter(settingAdapter);
        settingAdapter.setOnItemClickLitener(new settingOnItemClickListener());
        initItems();
    }

    class settingOnItemClickListener implements SettingAdapter.OnItemClickLitener {
        @Override
        public void onItemClick(final View view, int position) {
            switch (position) {
                case 0:
                    Intent intent = new Intent(SettingActivity.this,Chgpswd1Activity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Intent intent1 = new Intent(SettingActivity.this,SuggestionActivity.class);
                    startActivity(intent1);
                    break;
                case 2:
                    AVQuery<AVObject> query = new AVQuery<>("AppVersion");
                    query.getFirstInBackground(new GetCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            {
                                if (e == null) {
                                    if (avObject!=null){
                                        if(getVersion()<avObject.getNumber("VersionCode").intValue()){
                                            Toast.makeText(SettingActivity.this,"若安装失败请先把原应用卸载再尝试哦！",Toast.LENGTH_LONG).show();
                                            Uri uri = Uri.parse(avObject.getAVFile("Appapk").getUrl());
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            startActivity(intent);
                                        }else{
                                            Toast.makeText(SettingActivity.this,"当前已是最新版本啦！",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } else {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                    break;
            }
        }
    }

    private void initItems() {
        Setting changepswd = new Setting("修改密码");
        settingList.add(changepswd);
        Setting suggestion=new Setting("意见反馈");
        settingList.add(suggestion);
        Setting update = new Setting("版本更新");
        settingList.add(update);
        /*Setting clear = new Setting("清理缓存");
        settingList.add(clear);*/

    }
    private int getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
