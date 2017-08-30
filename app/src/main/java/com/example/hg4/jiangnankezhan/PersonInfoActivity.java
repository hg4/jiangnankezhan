package com.example.hg4.jiangnankezhan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class PersonInfoActivity extends AppCompatActivity {
	private ImageButton back;
	private RecyclerView baseInfoView;
	private RecyclerView schoolInfoView;
	private List<Info> baseInfoList=new ArrayList<>();
	private List<Info> schoolInfoList=new ArrayList<>();
	private List<String> keyInfoList=new ArrayList<String>(){{add("昵称");add("性别");add("手机");add("邮箱");}};
	private List<String> schoolkeyInfoList=new ArrayList<String>(){{add("学院");add("专业");add("年级");add("学历");}};
	private String key;
	private String value;
	private SharedPreferences pref;
	private AVUser user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info);
		ImmersionBar.with(this).init();
		pref=getSharedPreferences("userdata",MODE_PRIVATE);
		initBaseInfo();
		initSchoolInfo();
		user=AVUser.getCurrentUser();
		back=(ImageButton)findViewById(R.id.info_back);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PersonInfoActivity.this.finish();
				startActivity(new Intent(PersonInfoActivity.this,MainActivity.class));
			}
		});
		baseInfoView=(RecyclerView)findViewById(R.id.baseinfo);
		LinearLayoutManager layoutManager1=new LinearLayoutManager(this);
		baseInfoView.setLayoutManager(layoutManager1);
		BaseInfoAdapter baseAdapter=new BaseInfoAdapter(baseInfoList);
		baseAdapter.setOnItemClickLitener(new BaseInfoAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(final View view, int position) {
				switch (position){
					case 0:
						final ConstraintLayout nameChange=(ConstraintLayout) getLayoutInflater().inflate(R.layout.nicknamedialog,null);
						AlertDialog.Builder builder=new AlertDialog.Builder(PersonInfoActivity.this);
						builder.setView(nameChange)
						.setPositiveButton("保存", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								EditText nameInput = (EditText) nameChange.findViewById(R.id.nicknameinput);
								final String newName=nameInput.getText().toString();
								if (Utilty.nameIsValid(newName)) {
									user.put("nickname", newName);
									user.saveInBackground(new SaveCallback() {
										@Override
										public void done(AVException e) {
											saveString("昵称",newName);
											TextView textView=(TextView)view.findViewById(R.id.info_value);
											textView.setText(newName);
										}
									});
									dialog.dismiss();
								}
								else{
									Toast.makeText(PersonInfoActivity.this,"用户名不合法",Toast.LENGTH_SHORT).show();
								}
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						});
						builder.create().show();
						break;
				}
			}
		});
		baseInfoView.setAdapter(baseAdapter);

		schoolInfoView=(RecyclerView)findViewById(R.id.schoolinfo);
		LinearLayoutManager layoutManager2=new LinearLayoutManager(this);
		schoolInfoView.setLayoutManager(layoutManager2);
		schoolInfoView.setAdapter(new BaseInfoAdapter(schoolInfoList));
	}
	private void initBaseInfo(){
		for(int i=0;i<keyInfoList.size();i++){
			String defaultValue="（请填写）";
			key=keyInfoList.get(i);
			value=pref.getString(key,defaultValue);
			if(value!=defaultValue){
				Info info=new Info(key,value);
				baseInfoList.add(info);
			}
			else {
				String id=AVUser.getCurrentUser().getObjectId();
				AVQuery<AVObject> query=new AVQuery<>("_User");
				query.getInBackground(id, new GetCallback<AVObject>() {
					@Override
					public void done(AVObject avObject, AVException e) {
						if(e==null){
							String nickname=avObject.getString("nickname");
							int sex=avObject.getInt("sex");
							String mobilePhoneNumber=avObject.getString("mobilePhoneNumber");
							String email=avObject.getString("email");
							SharedPreferences.Editor editor=getSharedPreferences("userdata",MODE_PRIVATE).edit();
							switch (sex){
								case 1:
									editor.putString("性别","男");
									break;
								case 2:
									editor.putString("性别","女");
									break;
							}
							editor.putString("昵称",nickname);
							editor.putString("手机",mobilePhoneNumber);
							editor.putString("邮箱",email);
							editor.apply();
							key=keyInfoList.get(0);
							Info info=new Info(key,nickname);
							baseInfoList.add(info);
						}
					}
				});
			}
		}
	}
	private void initSchoolInfo(){
		for(int i=0;i<schoolkeyInfoList.size();i++){
			String defaultValue="（请填写）";
			key=schoolkeyInfoList.get(i);
			value=pref.getString(key,defaultValue);
			Log.d("initschool",key+value);
			if(value!=defaultValue){
				Info info=new Info(key,value);
				schoolInfoList.add(info);
			}
			else {
				String id=AVUser.getCurrentUser().getObjectId();
				AVQuery<AVObject> query=new AVQuery<>("_User");
				query.getInBackground(id, new GetCallback<AVObject>() {
					@Override
					public void done(AVObject avObject, AVException e) {
						if(e==null){
							String college=avObject.getString("college");
							String major=avObject.getString("major");
							String grade=avObject.getString("grade");
							String education=avObject.getString("education");
							SharedPreferences.Editor editor=getSharedPreferences("userdata",MODE_PRIVATE).edit();
							editor.putString("学院",college);
							editor.putString("专业",major);
							editor.putString("年级",grade);
							editor.putString("学历",education);
							Log.d("schooldata",college+major+grade+education);
							editor.apply();
							key=schoolkeyInfoList.get(0);
							Info info=new Info(key,college);
							schoolInfoList.add(info);
						}
					}
				});
			}
		}
	}
	private void saveString(String key,String data){
		SharedPreferences.Editor editor=getSharedPreferences("userdata",MODE_PRIVATE).edit();
		editor.putString(key,data);
		editor.apply();
	}
}
