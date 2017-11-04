package com.example.hg4.jiangnankezhan;

import android.content.Intent;
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

public class ComquesListActivity extends BaseActivity{
	private String[] major={"课程表导入有问题", "图片加载慢", "课程表与日期不匹配", "无法进行课程评价",
			"学习资料不齐全", "无法上传学习资料", "发送消息慢", "启动画面时间长", "没有我想要的功能",
			"没有我想查的课程", "没有我想知道的课程信息", "没有我想找的学习资料", "界面操作不方便", "界面按键不灵敏",
			"App闪退", "导入课表慢", "界面不好看", "有些功能难以发现", "不能自定义界面"};
	private ListView list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comques_list);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.dialog_list_item,major);
		list=(ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView question=(TextView)view.findViewById(R.id.data);
				final String Data=question.getText().toString();
				Intent intent = new Intent();
				intent.putExtra("question", Data);
				ComquesListActivity.this.setResult(1, intent);
				ComquesListActivity.this.finish();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ComquesListActivity.this.setResult(0);
	}
}
