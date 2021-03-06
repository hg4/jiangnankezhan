package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;

public class ListDiaglogActivity extends BaseActivity{
	private String[] major={"至善学院","马克思主义学院","数字媒体学院","化学与材料工程学院","医学院","商学院","环境与土木工程学院","君远学院","外国语学院","国际教育学院","人文学院","机械工程学院","法学院","物联网工程学院","理学院","生物工程学院","纺织服装学院","北美学院","设计学院","食品学院"};
	private ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_diaglog);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.dialog_list_item,major);
		list=(ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView college=(TextView)view.findViewById(R.id.data);
				final String collegeData=college.getText().toString();
				AVUser user=AVUser.getCurrentUser();
				user.put("college",collegeData);
				user.saveInBackground(new SaveCallback() {
					@Override
					public void done(AVException e) {
						if(e==null){
							PerferencesUtils.saveUserStringData(ListDiaglogActivity.this,ListDiaglogActivity.this.id,"学院",collegeData);
							Intent intent = new Intent();
							intent.putExtra("college", collegeData);
							ListDiaglogActivity.this.setResult(1, intent);
							ListDiaglogActivity.this.finish();
						}
						else {
							ListDiaglogActivity.this.setResult(0);
							ListDiaglogActivity.this.finish();
						}
					}
				});
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ListDiaglogActivity.this.setResult(0);
	}
}
