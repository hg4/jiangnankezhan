package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by HG4 on 2017/9/1.
 */

public class ListGradeActivity extends BaseActivity {
	private String[] grade = {"2018","2017", "2016", "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008"};
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_diaglog);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dialog_list_item, grade);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView college = (TextView) view.findViewById(R.id.data);
				final String gradeData = college.getText().toString();
				AVUser user=AVUser.getCurrentUser();
				user.put("grade",gradeData);
				user.saveInBackground(new SaveCallback() {
					@Override
					public void done(AVException e) {
						if(e==null){
							PerferencesUtils.saveUserStringData(ListGradeActivity.this,ListGradeActivity.this.id,"入学年份",gradeData);
							Intent intent = new Intent();
							intent.putExtra("grade", gradeData);
							ListGradeActivity.this.setResult(1, intent);
							ListGradeActivity.this.finish();
						}
						else {
							ListGradeActivity.this.setResult(0);
							ListGradeActivity.this.finish();
						}
					}
				});

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ListGradeActivity.this.setResult(0);
	}
}
