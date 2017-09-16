package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {
    private RecyclerView settingView;
    private List<Setting> settingList=new ArrayList<>();
    private Button back;
    private SettingAdapter settingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingView=(RecyclerView)findViewById(R.id.recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        settingView.setLayoutManager(layoutManager);
        settingView.setHasFixedSize(true);
        back=(Button)findViewById(R.id.back);
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
                    Intent intent = new Intent(SettingActivity.this,Chgpswd1ctivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void initItems() {

        Setting changepswd = new Setting("修改密码");
        settingList.add(changepswd);
        Setting update = new Setting("版本更新");
        settingList.add(update);
        Setting clear = new Setting("清理缓存");
        settingList.add(clear);

    }
}
