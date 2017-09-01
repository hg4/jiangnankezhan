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

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class PersonInfoActivity extends AppCompatActivity {
	private RecyclerView baseInfoView;
	private RecyclerView schoolInfoView;
	private List<Info> baseInfoList=new ArrayList<>();
	private List<Info> schoolInfoList=new ArrayList<>();
	private List<String> keyInfoList=new ArrayList<String>(){{add("昵称");add("性别");add("手机");add("邮箱");}};
	private List<String> schoolkeyInfoList=new ArrayList<String>(){{add("学院");add("专业");add("年级");add("学历");}};
	private List<String> findkeyList1=new ArrayList<String>(){{add("nickname");add("sex");add("mobilePhoneNumber");add("email");}};
	private List<String> findkeyList2=new ArrayList<String>(){{add("college");add("major");add("grade");add("education");}};
	private String key;
	private String value;
	private SharedPreferences pref;
	private AVUser user;
	private String id=AVUser.getCurrentUser().getObjectId();
	private String username=AVUser.getCurrentUser().getUsername();
	private BaseInfoAdapter baseAdapter;
	private BaseInfoAdapter schoolinfoAdapter;
	private int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info);
		ImmersionBar.with(this).init();
		pref=getSharedPreferences(id+"userdata",MODE_PRIVATE);
		//SharedPreferences.Editor editor=getSharedPreferences(id+"userdata",MODE_PRIVATE).edit();
		//editor.clear();
		//editor.apply();

		user=AVUser.getCurrentUser();
		baseInfoView=(RecyclerView)findViewById(R.id.baseinfo);
		LinearLayoutManager layoutManager1=new LinearLayoutManager(this);
		baseInfoView.setLayoutManager(layoutManager1);
		baseAdapter=new BaseInfoAdapter(baseInfoList);
		baseAdapter.setOnItemClickLitener(new baseOnItemClickListener());
		baseInfoView.setAdapter(baseAdapter);
		initBaseInfo();
		schoolInfoView=(RecyclerView)findViewById(R.id.schoolinfo);
		LinearLayoutManager layoutManager2=new LinearLayoutManager(this);
		schoolInfoView.setLayoutManager(layoutManager2);
		schoolinfoAdapter=new BaseInfoAdapter(schoolInfoList);
		schoolInfoView.setAdapter(schoolinfoAdapter);
		initSchoolInfo();
	}
	private void initBaseInfo(){
		for(i=0;i<keyInfoList.size();i++){
			String defaultValue="";
			key=keyInfoList.get(i);
			value=pref.getString(key,defaultValue);
			if(value!=defaultValue){
				Info info=new Info(key,value);
				baseInfoList.add(info);
			}
			else {
				String findkey=findkeyList1.get(i);
				String cql="select "+findkey+" from _User where username='"+username+"'";
				findBaseDataInBackground(cql,key,findkey);
			}
		}
	}
	private void initSchoolInfo(){
		for(i=0;i<schoolkeyInfoList.size();i++){
			String defaultValue="";
			key=schoolkeyInfoList.get(i);
			value=pref.getString(key,defaultValue);
			Log.d("initschool",key+value);
			if(value!=defaultValue){
				Info info=new Info(key,value);
				schoolInfoList.add(info);
			}
			else {
				String findkey=findkeyList2.get(i);
				String cql="select "+findkey+" from _User where username='"+username+"'";
				findSchoolDataInBackground(cql,key,findkey);
			}
		}
	}
	private void saveString(String key,String data){
		SharedPreferences.Editor editor=getSharedPreferences(id+"userdata",MODE_PRIVATE).edit();
		editor.putString(key,data);
		editor.apply();
	}
	private void findBaseDataInBackground(String cql,final String key,final String findkey){
		AVQuery<AVObject> query=new AVQuery<>("_User");
		query.doCloudQueryInBackground(cql, new CloudQueryCallback<AVCloudQueryResult>() {
			@Override
			public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
				AVObject avObject=avCloudQueryResult.getResults().get(0);
				String findValue=avObject.getString(findkey);
				SharedPreferences.Editor editor=getSharedPreferences(id+"userdata",MODE_PRIVATE).edit();
				editor.putString(key,findValue);
				editor.apply();
				Info info=new Info(key,findValue);
				baseInfoList.add(info);
				baseAdapter.notifyDataSetChanged();
			}
		});
	}
	private void findSchoolDataInBackground(String cql,final String key,final String findkey){
		AVQuery<AVObject> query=new AVQuery<>("_User");
		query.doCloudQueryInBackground(cql, new CloudQueryCallback<AVCloudQueryResult>() {
			@Override
			public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
				AVObject avObject=avCloudQueryResult.getResults().get(0);
				String findValue=avObject.getString(findkey);
				SharedPreferences.Editor editor=getSharedPreferences(id+"userdata",MODE_PRIVATE).edit();
				editor.putString(key,findValue);
				editor.apply();
				Info info=new Info(key,findValue);
				schoolInfoList.add(info);
				schoolinfoAdapter.notifyDataSetChanged();
			}
		});
	}
	class baseOnItemClickListener implements BaseInfoAdapter.OnItemClickLitener{
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
					AlertDialog dialog=builder.create();
					dialog.show();
					dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
					dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
					break;
			}
		}
	}
}
