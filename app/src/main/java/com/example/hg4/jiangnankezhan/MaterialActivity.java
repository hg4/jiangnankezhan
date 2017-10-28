package com.example.hg4.jiangnankezhan;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.hg4.jiangnankezhan.Adapter.MaterialAdapter;

import java.util.ArrayList;
import java.util.List;

public class MaterialActivity extends BaseActivity {
    private ImageView back;
    private Button upload;
    private ListView material;
    private List<String> materialList=new ArrayList<>();
    private MaterialAdapter adapter;
    private Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        back = (ImageView) findViewById(R.id.back);
        upload = (Button) findViewById(R.id.upload);
        material=(ListView) findViewById(R.id.material);
        search=(Button)findViewById(R.id.meterial_search);
        adapter=new MaterialAdapter(MaterialActivity.this, R.layout.setting_item,materialList);
        material.setAdapter(adapter);
        material.setDivider(null);
        AVQuery<AVObject> query = new AVQuery<>("Course_file");
        query.whereEqualTo("Course",getIntent().getStringExtra("courseName"));
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e){
                {
                    if (e == null) {
                        int i;
                        for(i=0;i<list.size();i++){
                            AVObject m=list.get(i);
                            materialList.add(m.getString("Title"));
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        });
        material.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MaterialActivity.this,DownloadActivity.class);
                intent.putExtra("content",materialList.get(i));
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialActivity.this.finish();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MaterialActivity.this,UploadActivity.class);
                intent.putExtra("courseName",getIntent().getStringExtra("courseName"));
                startActivityForResult(intent,1);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MaterialActivity.this,SearchActivity.class);
                intent.putExtra("findname","Course_file");
                intent.putExtra("adaptertype",2);
                Bundle bundle=new Bundle();
                bundle.putInt("conditiontype",2);
                bundle.putString("courseName",getIntent().getStringExtra("courseName"));
                intent.putExtra("condition",bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {
            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK) {
                        materialList.clear();
                        AVQuery<AVObject> query = new AVQuery<>("Course_file");
                        query.whereEqualTo("Course",getIntent().getStringExtra("courseName"));
                        query.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e){
                                {
                                    if (e == null) {
                                        int i;
                                        for(i=0;i<list.size();i++){
                                            AVObject m=list.get(i);
                                            materialList.add(m.getString("Title"));
                                            adapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }

                    break;
            }
        }
    }


}
