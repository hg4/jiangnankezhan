package com.example.hg4.jiangnankezhan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.Model.Info;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonInfoActivity extends BaseActivity {
	private RecyclerView baseInfoView;
	private RecyclerView schoolInfoView;
	private List<Info> baseInfoList=new ArrayList<>();
	private List<Info> schoolInfoList=new ArrayList<>();
	private List<String> keyInfoList=new ArrayList<String>(){{add("昵称");add("性别");add("手机");add("邮箱");}};
	private List<String> schoolkeyInfoList=new ArrayList<String>(){{add("学院");add("专业");add("入学年份");add("学历");}};
	private List<String> findkeyList1=new ArrayList<String>(){{add("nickname");add("sex");add("mobilePhoneNumber");add("email");}};
	private List<String> findkeyList2=new ArrayList<String>(){{add("college");add("major");add("grade");add("education");}};
	private TextView top_nicknameHolder;
	private TextView top_collegeHolder;
	private String key;
	private String value;
	private SharedPreferences pref;
	private AVUser user;
	private String id=AVUser.getCurrentUser().getObjectId();
	private String username=AVUser.getCurrentUser().getUsername();
	private BaseInfoAdapter baseAdapter;
	private BaseInfoAdapter schoolinfoAdapter;
	private int i;
	private TextView collegeText;
	private TextView gradeText;
	private ImageView headView;
	private Bitmap head;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info);
		top_collegeHolder=(TextView)findViewById(R.id.info_college);
		top_nicknameHolder=(TextView)findViewById(R.id.info_nickname);
		pref=getSharedPreferences(id+"userdata",MODE_PRIVATE);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		initBaseData();
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
		schoolinfoAdapter.setOnItemClickLitener(new schoolOnItemClickListener());
		schoolInfoView.setAdapter(schoolinfoAdapter);
		initSchoolInfo();
		initView();
		headView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTypeDialog();
			}
		});

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
	class baseOnItemClickListener implements BaseInfoAdapter.OnItemClickLitener {
		@Override
		public void onItemClick(final View view, int position) {
			switch (position) {
				case 0:
					final ConstraintLayout nameChange = (ConstraintLayout) getLayoutInflater().inflate(R.layout.nicknamedialog, null);
					AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);
					builder.setView(nameChange)
							.setPositiveButton("保存", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									EditText nameInput = (EditText) nameChange.findViewById(R.id.nicknameinput);
									final String newName = nameInput.getText().toString();
									if (Utilty.nameIsValid(newName)) {
										user.put("nickname", newName);
										user.saveInBackground(new SaveCallback() {
											@Override
											public void done(AVException e) {
												if (e == null) {
													saveString("昵称", newName);
													TextView textView = (TextView) view.findViewById(R.id.info_value);
													textView.setText(newName);
													top_nicknameHolder.setText(newName);
												}

											}
										});
										dialog.dismiss();
									} else {
										Toast.makeText(PersonInfoActivity.this, "用户名不合法", Toast.LENGTH_SHORT).show();
									}
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();
					dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
					dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
					break;
				case 1:
					final LinearLayout sexchange = (LinearLayout) getLayoutInflater().inflate(R.layout.sex_dialog, null);

					AlertDialog.Builder sexbuilder = new AlertDialog.Builder(PersonInfoActivity.this);
					sexbuilder.setView(sexchange);
					final AlertDialog sexdialog = sexbuilder.create();
					sexdialog.show();
					final TextView male = (TextView) sexchange.findViewById(R.id.male_item);
					male.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							final String newSex = ((TextView) v).getText().toString();
							user.put("sex", newSex);
							user.saveInBackground(new SaveCallback() {
								@Override
								public void done(AVException e) {
									if (e == null) {
										saveString("性别", newSex);
										TextView textView = (TextView) view.findViewById(R.id.info_value);
										textView.setText(newSex);
										sexdialog.dismiss();
									}

								}
							});
						}
					});
					TextView female = (TextView) sexchange.findViewById(R.id.female_item);
					female.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							final String newSex = ((TextView) v).getText().toString();
							user.put("sex", newSex);
							user.saveInBackground(new SaveCallback() {
								@Override
								public void done(AVException e) {
									if (e == null) {
										saveString("性别", newSex);
										TextView textView = (TextView) view.findViewById(R.id.info_value);
										textView.setText(newSex);
										sexdialog.dismiss();
									}

								}
							});
						}
					});
					break;
				case 2:
					final ConstraintLayout phoneChange = (ConstraintLayout) getLayoutInflater().inflate(R.layout.phone_dialog, null);
					AlertDialog.Builder phoneBuilder = new AlertDialog.Builder(PersonInfoActivity.this);
					phoneBuilder.setView(phoneChange)
							.setPositiveButton("保存", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									EditText phoneInput = (EditText) phoneChange.findViewById(R.id.phoneinput);
									final String newPhone = phoneInput.getText().toString();
									if (Utilty.phoneIsValid(newPhone)) {
										user.put("mobilePhoneNumber", newPhone);
										user.saveInBackground(new SaveCallback() {
											@Override
											public void done(AVException e) {
												if (e == null) {
													saveString("手机", newPhone);
													TextView textView = (TextView) view.findViewById(R.id.info_value);
													textView.setText(newPhone);
												}

											}
										});
										dialog.dismiss();
									} else {
										Toast.makeText(PersonInfoActivity.this, "手机号不合法", Toast.LENGTH_SHORT).show();
									}
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							});
					AlertDialog phoneDialog = phoneBuilder.create();
					phoneDialog.show();
					phoneDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
					phoneDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
					break;
				case 3:
					final ConstraintLayout emailChange = (ConstraintLayout) getLayoutInflater().inflate(R.layout.email_dialog, null);
					AlertDialog.Builder emailBuilder = new AlertDialog.Builder(PersonInfoActivity.this);
					emailBuilder.setView(emailChange)
							.setPositiveButton("保存", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									EditText emailInput = (EditText) emailChange.findViewById(R.id.emailinput);
									final String newEmail = emailInput.getText().toString();
									if (Utilty.emailIsValid(newEmail)) {
										user.put("email", newEmail);
										user.saveInBackground(new SaveCallback() {
											@Override
											public void done(AVException e) {
												if (e == null) {
													saveString("邮箱", newEmail);
													TextView textView = (TextView) view.findViewById(R.id.info_value);
													textView.setText(newEmail);
												}
											}
										});
										dialog.dismiss();
									} else {
										Toast.makeText(PersonInfoActivity.this, "邮箱号不合法", Toast.LENGTH_SHORT).show();
									}
								}
							})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							});
					AlertDialog emailDialog = emailBuilder.create();
					emailDialog.show();
					emailDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
					emailDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
					break;
			}
		}
	}
	class schoolOnItemClickListener implements BaseInfoAdapter.OnItemClickLitener{
			@Override
			public void onItemClick(final View view, int position) {
				switch (position){
					case 0:
							collegeText=(TextView)view.findViewById(R.id.info_value);
							startActivityForResult(new Intent(PersonInfoActivity.this,ListDiaglogActivity.class),4);
						break;
					case 1:
						final ConstraintLayout majorChange=(ConstraintLayout) getLayoutInflater().inflate(R.layout.major_dialog,null);
						AlertDialog.Builder majorBuilder=new AlertDialog.Builder(PersonInfoActivity.this);
						majorBuilder.setView(majorChange)
								.setPositiveButton("保存", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										EditText majorInput = (EditText) majorChange.findViewById(R.id.majorinput);
										final String newmajor=majorInput.getText().toString();
										if (Utilty.emailIsValid(newmajor)) {
											user.put("major", newmajor);
											user.saveInBackground(new SaveCallback() {
												@Override
												public void done(AVException e) {
													if(e==null) {
														saveString("邮箱", newmajor);
														TextView textView = (TextView) view.findViewById(R.id.info_value);
														textView.setText(newmajor);
													}
												}
											});
											dialog.dismiss();
										}
										else{
											Toast.makeText(PersonInfoActivity.this,"邮箱号不合法",Toast.LENGTH_SHORT).show();
										}
									}
								})
								.setNegativeButton("取消", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {

									}
								});
						AlertDialog majorDialog=majorBuilder.create();
						majorDialog.show();
						majorDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
						majorDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
						break;
					case 2:
						gradeText=(TextView)view.findViewById(R.id.info_value);
						startActivityForResult(new Intent(PersonInfoActivity.this,ListGradeActivity.class),5);
						break;
					case 3:
						final LinearLayout educationchange = (LinearLayout) getLayoutInflater().inflate(R.layout.edu_dialog, null);
						AlertDialog.Builder educationBuilder = new AlertDialog.Builder(PersonInfoActivity.this);
						educationBuilder.setView(educationchange);
						final AlertDialog edudialog = educationBuilder.create();
						edudialog.show();
						final TextView undergraduate = (TextView) educationchange.findViewById(R.id.undergraduate_item);
						undergraduate.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								final String newEdu = ((TextView) v).getText().toString();
								user.put("education", newEdu);
								user.saveInBackground(new SaveCallback() {
									@Override
									public void done(AVException e) {
										if (e == null) {
											saveString("学历", newEdu);
											TextView textView = (TextView) view.findViewById(R.id.info_value);
											textView.setText(newEdu);
											edudialog.dismiss();
										}

									}
								});
							}
						});
						final TextView postgraduate = (TextView) educationchange.findViewById(R.id.postgraduate_item);
						postgraduate.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								final String newEdu = ((TextView) v).getText().toString();
								user.put("education", newEdu);
								user.saveInBackground(new SaveCallback() {
									@Override
									public void done(AVException e) {
										if (e == null) {
											saveString("学历", newEdu);
											TextView textView = (TextView) view.findViewById(R.id.info_value);
											textView.setText(newEdu);
											edudialog.dismiss();
										}

									}
								});
							}
						});
						break;
				}
			}
		}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode!=0){
			switch (requestCode){
				case 1:
					if (resultCode == RESULT_OK) {
						cropPhoto(data.getData());
					}

					break;
				case 2:
					if (resultCode == RESULT_OK) {
						File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
						cropPhoto(Uri.fromFile(temp));
					}

					break;
				case 3:
					if (data != null) {
						Bundle extras = data.getExtras();
						head = extras.getParcelable("data");
						if (head != null) {
							AVFile file=new AVFile("head.jpg",Utilty.Bitmap2Bytes(head));
							user.put("head",file);
							user.saveInBackground();
							headView.setImageBitmap(head);// 用ImageView显示出来
							saveBitmapToSharedPreferences(head);

						}
					}
					break;
				default:
					break;
				case 4:
					final String collegedata=data.getStringExtra("college");
					user.put("college",collegedata);
					user.saveInBackground(new SaveCallback() {
						@Override
						public void done(AVException e) {
							if(e==null){
								saveString("学院",collegedata);
								collegeText.setText(collegedata);
								top_collegeHolder.setText(collegedata);
							}
						}
					});
					break;
				case 5:
					final String gradedata=data.getStringExtra("grade");
					user.put("grade",gradedata);
					user.saveInBackground(new SaveCallback() {
						@Override
						public void done(AVException e) {
							if(e==null){
								saveString("入学年份",gradedata);
								gradeText.setText(gradedata);
							}
						}
					});
					break;
			}
		}

	}
	private void initBaseData(){
			String top_nickname=pref.getString("昵称","");
			String top_college=pref.getString("学院","");
			if(top_nickname!=""){
				top_nicknameHolder.setText(top_nickname);
			}
			else top_nicknameHolder.setText("（请填写）");
			if(top_college!=""){
				top_collegeHolder.setText(top_college);
			}
			else top_nicknameHolder.setText("（请填写）");

		}
	private void initView() {
		headView = (ImageView) findViewById(R.id.info_imageView);
		if(getBitmapFromSharedPreferences()!=null)
		headView.setImageBitmap(getBitmapFromSharedPreferences());

	}



	private void showTypeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		View view = View.inflate(this, R.layout.dialog_select_photo, null);
		TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
		TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
		tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);
				intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent1, 1);
				dialog.dismiss();
			}
		});
		tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent2.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
				startActivityForResult(intent2, 2);
				dialog.dismiss();
			}
		});
		dialog.setView(view);
		dialog.show();
	}


	private void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}



	private void saveBitmapToSharedPreferences(Bitmap bitmap){
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
		byte[] byteArray=byteArrayOutputStream.toByteArray();
		String imageString=new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
		SharedPreferences sharedPreferences=getSharedPreferences(id+"userdata", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.putString("image", imageString);
		editor.commit();
	}
	private Bitmap getBitmapFromSharedPreferences(){
		SharedPreferences sharedPreferences=getSharedPreferences(id+"userdata", Context.MODE_PRIVATE);
		String imageString=sharedPreferences.getString("image", "");
		byte[] byteArray=Base64.decode(imageString, Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
		Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
		return bitmap;
	}
}
