package com.example.hg4.jiangnankezhan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{
	private List<TextView> text=new ArrayList<>();
	private List<String> findkeyList=new ArrayList<String>(){{add("nickname");add("sex");add("mobilePhoneNumber");add("email");add("college");add("grade");add("major");add("education");}};
	private List<String> savekeyList=new ArrayList<String>(){{add("昵称");add("性别");add("手机");add("邮箱");add("学院");add("年级");add("专业");add("学历");}};
	private TextView top_nicknameHolder;
	private TextView top_collegeHolder;
	private String key;
	private String value;
	private SharedPreferences pref;
	private AVUser user;
	private String id=AVUser.getCurrentUser().getObjectId();
	private String username=AVUser.getCurrentUser().getUsername();
	private ImageView headView;
	private Bitmap head;
	private ConstraintLayout nickname_holder;
	private ConstraintLayout sex_holder;
	private ConstraintLayout phone_holder;
	private ConstraintLayout email_holder;
	private ConstraintLayout college_holder;
	private ConstraintLayout grade_holder;
	private ConstraintLayout major_holder;
	private ConstraintLayout education_holder;
	private TextView nickname;
	private TextView sex;
	private TextView phone;
	private TextView email;
	private TextView college;
	private TextView grade;
	private TextView major;
	private TextView edu;
	private AVObject getUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_info);
		top_collegeHolder=(TextView)findViewById(R.id.info_college);
		top_nicknameHolder=(TextView)findViewById(R.id.info_nickname);
		nickname_holder=(ConstraintLayout)findViewById(R.id.nickname_holder);
		sex_holder=(ConstraintLayout)findViewById(R.id.sex_holder);
		phone_holder=(ConstraintLayout)findViewById(R.id.phone_holder);
		email_holder=(ConstraintLayout)findViewById(R.id.email_holder);
		college_holder=(ConstraintLayout)findViewById(R.id.college_holder);
		grade_holder=(ConstraintLayout)findViewById(R.id.grade_holder);
		major_holder=(ConstraintLayout)findViewById(R.id.major_holder);
		education_holder=(ConstraintLayout)findViewById(R.id.education_holder);
		nickname=(TextView)findViewById(R.id.item_nickname);
		sex=(TextView)findViewById(R.id.item_sex);
		phone=(TextView)findViewById(R.id.item_phone);
		email=(TextView)findViewById(R.id.item_email);
		college=(TextView)findViewById(R.id.item_college);
		grade=(TextView)findViewById(R.id.item_grade);
		major=(TextView)findViewById(R.id.item_major);
		edu=(TextView)findViewById(R.id.item_education);
		text.add(nickname);
		text.add(sex);
		text.add(phone);
		text.add(email);
		text.add(college);
		text.add(grade);
		text.add(major);
		text.add(edu);
		findUser();
		pref=getSharedPreferences(id+"userdata",MODE_PRIVATE);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		//SharedPreferences.Editor editor=getSharedPreferences(id+"userdata",MODE_PRIVATE).edit();
		//editor.clear();
		//editor.apply();
		user=AVUser.getCurrentUser();
		initBaseData();
		initView();
		if(!Utilty.isNetworkAvailable(this))//加载离线数据
			initInfo();
		headView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showTypeDialog();
			}
		});
		nickname_holder.setOnClickListener(this);
		sex_holder.setOnClickListener(this);
		phone_holder.setOnClickListener(this);
		email_holder.setOnClickListener(this);
		college_holder.setOnClickListener(this);
		grade_holder.setOnClickListener(this);
		major_holder.setOnClickListener(this);
		education_holder.setOnClickListener(this);
	}
	private void initInfo(){
		String defaultValue="";
		for(int i=0;i<text.size();i++){
			value=pref.getString(savekeyList.get(i),defaultValue);
			text.get(i).setText(value);
			if(getUser!=null){
				if(!value.equals(getUser.getString(findkeyList.get(i)))){
					text.get(i).setText(getUser.getString(findkeyList.get(i)));
					saveString(savekeyList.get(i),getUser.getString(findkeyList.get(i)));
				}

			}
		}
	}
	private void saveString(String key,String data){
		SharedPreferences.Editor editor=getSharedPreferences(id+"userdata",MODE_PRIVATE).edit();
		editor.putString(key,data);
		editor.apply();
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()){
			case R.id.nickname_holder:
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
												nickname.setText(newName);
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
			case R.id.sex_holder:
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
									sex.setText(newSex);
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
									sex.setText(newSex);
									sexdialog.dismiss();
								}

							}
						});
					}
				});
				break;
			case R.id.phone_holder:
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
												phone.setText(newPhone);
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
			case R.id.email_holder:
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
												email.setText(newEmail);
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
			case R.id.college_holder:
				startActivityForResult(new Intent(PersonInfoActivity.this,ListDiaglogActivity.class),4);
				break;
			case R.id.grade_holder:
				startActivityForResult(new Intent(PersonInfoActivity.this,ListGradeActivity.class),5);
				break;
			case R.id.education_holder:
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
									edu.setText(newEdu);
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
									edu.setText(newEdu);
									edudialog.dismiss();
								}

							}
						});
					}
				});
				break;
			case R.id.major_holder:
				final ConstraintLayout majorChange=(ConstraintLayout) getLayoutInflater().inflate(R.layout.major_dialog,null);
				AlertDialog.Builder majorBuilder=new AlertDialog.Builder(PersonInfoActivity.this);
				majorBuilder.setView(majorChange)
						.setPositiveButton("保存", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								EditText majorInput = (EditText) majorChange.findViewById(R.id.majorinput);
								final String newmajor=majorInput.getText().toString();
								if (Utilty.nameIsValid(newmajor)) {
									user.put("major", newmajor);
									user.saveInBackground(new SaveCallback() {
										@Override
										public void done(AVException e) {
											if(e==null) {
												saveString("专业", newmajor);
												major.setText(newmajor);
											}
										}
									});
									dialog.dismiss();
								}
								else{
									Toast.makeText(PersonInfoActivity.this,"专业名不合法",Toast.LENGTH_SHORT).show();
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
							AVUser.getCurrentUser().remove("head");
							user.put("head",file);
							user.saveInBackground();
							headView.setImageBitmap(head);// 用ImageView显示出来
							PerferencesUtils.saveBitmapToSharedPreferences(head,id,PersonInfoActivity.this);

						}
					}
					break;
				default:
					break;
				case 4:
					String collegedata=data.getStringExtra("college");
					college.setText(collegedata);
					top_collegeHolder.setText(collegedata);
					break;
				case 5:
					String gradedata=data.getStringExtra("grade");
					grade.setText(gradedata);
					break;
			}
		}

	}
	private void initBaseData(){
			String top_nickname=pref.getString("昵称","");
			String top_college=pref.getString("学院","");
			if(!top_nickname.equals("")){
				top_nicknameHolder.setText(top_nickname);
			}
			else top_nicknameHolder.setText("（请填写）");
			if(!top_college.equals("")){
				top_collegeHolder.setText(top_college);
			}
			else top_nicknameHolder.setText("（请填写）");

		}
	private void initView() {
		headView = (ImageView) findViewById(R.id.info_imageView);
		Bitmap getBitmap=PerferencesUtils.getBitmapFromSharedPreferences(id,PersonInfoActivity.this);
		if(getBitmap!=null)
			headView.setImageBitmap(getBitmap);
		else {
			AVQuery<AVUser> query=new AVQuery<>("_User");
			query.getInBackground(id, new GetCallback<AVUser>() {
				@Override
				public void done(AVUser avUser, AVException e) {
					if (avUser != null) {
						AVFile file = avUser.getAVFile("head");
						if (file != null) {
							file.getDataInBackground(new GetDataCallback() {
								@Override
								public void done(byte[] bytes, AVException e) {
									if (e == null) {
										Bitmap head = Utilty.Bytes2Bimap(bytes);
										headView.setImageBitmap(head);
										PerferencesUtils.saveBitmapToSharedPreferences(head, id, PersonInfoActivity.this);
									}
								}
							});

						}
					}
				}
			});
		}
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
	private void findUser(){
		AVQuery query=new AVQuery("_User");
		query.whereEqualTo("objectId",id);
		query.getFirstInBackground(new GetCallback() {
			@Override
			public void done(AVObject avObject, AVException e) {
				if(e==null){
					if(avObject!=null) {
						getUser = avObject;
						initInfo();
					}
				}
			}
		});
	}
}
