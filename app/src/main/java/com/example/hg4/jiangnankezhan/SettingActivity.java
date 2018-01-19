package com.example.hg4.jiangnankezhan;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.example.hg4.jiangnankezhan.Adapter.SettingAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.hg4.jiangnankezhan.Utils.Utilty.getJsonToStringArray;


public class SettingActivity extends BaseActivity {
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
                    try {
                        PackageManager packageManager = getPackageManager();
                        final PackageInfo packageInfo = packageManager.getPackageInfo(
                                getPackageName(), 0);
                        AVQuery<AVObject> query = new AVQuery<>("AppVersion");
                        query.orderByDescending("createdAt");
                        query.getFirstInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(final AVObject avObject, AVException e) {
                                {
                                    if (e == null) {
                                        if (avObject!=null){
                                            if(packageInfo.versionCode<avObject.getNumber("VersionCode").intValue()){
                                                final AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
                                                LayoutInflater inflater = LayoutInflater.from(SettingActivity.this);
                                                View v = inflater.inflate(R.layout.version_dialog, null);
                                                String[] data=getJsonToStringArray(avObject.getJSONArray("content"));
                                                TextView currentver = (TextView) v.findViewById(R.id.currentver);
                                                TextView newver=(TextView)v.findViewById(R.id.newver);
                                                ListView content=(ListView) v.findViewById(R.id.vercontent);
                                                ArrayAdapter<String> adapter=new ArrayAdapter<String>(SettingActivity.this,
                                                        R.layout.vscontentlist,data);
                                                content.setAdapter(adapter);
                                                newver.setText("最新版本："+avObject.getString("VersionName"));
                                                currentver.setText("当前版本："+packageInfo.versionName);
                                                Button cancel = (Button) v.findViewById(R.id.cancel);
                                                Button sure = (Button) v.findViewById(R.id.sure);
                                                final Dialog dialog = builder.create();
                                                dialog.show();
                                                Window dialogWindow = dialog.getWindow();
                                                WindowManager m = getWindowManager();
                                                Display d = m.getDefaultDisplay();
                                                WindowManager.LayoutParams p = dialogWindow.getAttributes();
                                                p.width = (int) (d.getWidth() * 0.9);
                                                dialogWindow.setAttributes(p);
                                                dialog.getWindow().setContentView(v);
                                                cancel.setOnClickListener(new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                sure.setOnClickListener(new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View arg0) {
                                                        dialog.dismiss();
                                                        Uri uri = Uri.parse(avObject.getAVFile("Appapk").getUrl());
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                        startActivity(intent);
                                                    }
                                                });
                                                builder.create().show();
                                            }else {
                                                Toast.makeText(SettingActivity.this, "当前已是最新版本啦！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
