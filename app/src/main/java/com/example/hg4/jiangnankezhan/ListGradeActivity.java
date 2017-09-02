package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by HG4 on 2017/9/1.
 */

public class ListGradeActivity extends AppCompatActivity {
	private String[] grade = {"2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017"};
	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_diaglog);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dialog_list_item, grade);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView college = (TextView) view.findViewById(R.id.data);
				String gradeData = college.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("grade", gradeData);
				ListGradeActivity.this.setResult(1, intent);
				ListGradeActivity.this.finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ListGradeActivity.this.setResult(0);
	}
}
