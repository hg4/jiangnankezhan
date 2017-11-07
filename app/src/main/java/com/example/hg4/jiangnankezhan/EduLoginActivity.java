package com.example.hg4.jiangnankezhan;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.Model.Course;
import com.example.hg4.jiangnankezhan.Utils.Constants;
import com.example.hg4.jiangnankezhan.Utils.DaoUtil;
import com.example.hg4.jiangnankezhan.Utils.HttpUtils;
import com.example.hg4.jiangnankezhan.Utils.JsoupUtils;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.hg4.jiangnankezhan.R.id.code;

public class EduLoginActivity extends BaseActivity implements View.OnClickListener{
	private EditText username;
	private EditText password;
	private EditText codeInput;
	private ImageView verficationCode;
	private Button refreshCode;
	private Button login;
	private String[] time = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
	private String studentName;
	private byte[] byteCode;
	private ImageView back;
	private LoadingDialog dialog;
	private List<Map<String, Course[]>> courseList;
	private static Spinner sp_school_year;
	private static Spinner sp_term;
	private static String outputInfo = null;
	private static String urlEncodeStudentName = null;
	private static Map<String, String> requestHeadersMap, loginRequestBodyMap, scheduleRequestBodyMap,pyjhRequestBodyMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edu_login);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		initIntent();
		initView();
		setTimeAdapter();
		setTarget();
		initRequestData();
		initRequestBody();
	}
	private void initView(){
		verficationCode=(ImageView)this.findViewById(R.id.edulogin_code);
		back=(ImageView)this.findViewById(R.id.edu_back_home);
		initVerCode();
		username=(EditText)this.findViewById(R.id.edulogin_username);
		password=(EditText)this.findViewById(R.id.edulogin_password);
		codeInput=(EditText)this.findViewById(R.id.edulogin_codeinput);
		refreshCode=(Button)this.findViewById(R.id.edulogin_refresh);
		login=(Button)this.findViewById(R.id.edulogin_login);
		refreshCode.setOnClickListener(this);
		login.setOnClickListener(this);
		back.setOnClickListener(this);
		sp_school_year = (Spinner) this.findViewById(R.id.year_spinner);
		sp_term = (Spinner) this.findViewById(R.id.term_spinner);

	}
	private void initVerCode(){
		if(byteCode!=null){
			Bitmap bitmap=BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
			verficationCode.setBackground(new BitmapDrawable(getResources(),resizeBitmap(bitmap,100,60)));
			byteCode=null;

	}
		else {
			HttpUtils.sendGetRequest(Constants.VERTIFICATION_CODE_URL, new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					//加载获取失败图片
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(EduLoginActivity.this,"获取验证码失败,请使用校园网",Toast.LENGTH_SHORT).show();
						}
					});
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					final byte[] content=response.body().bytes();
					if(content!=null){
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Bitmap bitmap=BitmapFactory.decodeByteArray(content,0,content.length);
								verficationCode.setBackground(new BitmapDrawable(getResources(),resizeBitmap(bitmap,100,60)));
							}
						});
					}

				}
			});
		}

	}
	private Bitmap resizeBitmap(Bitmap bitmap,float width,float height){
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleX = (float) width / w;
			float scaleY = (float) height / h;
			matrix.postScale(scaleX, scaleY);
			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
			return resizeBitmap;
	}
	private void initRequestData(){
		requestHeadersMap=new LinkedHashMap<>();
		loginRequestBodyMap=new LinkedHashMap<>();
		scheduleRequestBodyMap=new LinkedHashMap<>();
		pyjhRequestBodyMap=new LinkedHashMap<>();
		requestHeadersMap.put(Constants.HEAD_HOST_KEY,Constants.HEAD_HOST_VALUE);
		requestHeadersMap.put(Constants.HEAD_AGENT_KEY,Constants.HEAD_AGENT_VALUE);
		requestHeadersMap.put(Constants.HEAD_REFERER_KEY,Constants.HEAD_REFERER_LOGIN);

	}
	private void initRequestBody(){
		HttpUtils.sendGetRequest(Constants.EDU_LOGIN_URL, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				if(response!=null){
					try{
						String html=new String(response.body().bytes(),"gb2312");
						JsoupUtils.setViewStateValue(html);
						if(Constants.LOGIN_BODY_VIEWSTATE_VALUE!=null)
							loginRequestBodyMap.put(Constants.LOGIN_BODY_VIEWSTATE_KEY,Constants.LOGIN_BODY_VIEWSTATE_VALUE);
						Log.d("test",Constants.LOGIN_BODY_VIEWSTATE_VALUE);
					}catch (UnsupportedEncodingException e){
						e.printStackTrace();
					}
				}
			}
		});
		loginRequestBodyMap.put(Constants.LOGIN_BODY_TYPE_KEY,Constants.LOGIN_BODY_TYPE_VALUE);
		loginRequestBodyMap.put(Constants.LOGIN_BODY_BUTTON_KEY,Constants.LOGIN_BODY_BUTTON_VALUE);
		loginRequestBodyMap.put(Constants.LOGIN_BODY_LANGUAGE_KEY,Constants.LOGIN_BODY_LANGUAGE_VALUE);
	}
	private void addRequestBody(){
		Constants.LOGIN_BODY_USERNAME_VALUE=username.getText().toString();
		Constants.LOGIN_BODY_PASSWORD_VALUE=password.getText().toString();
		Constants.LOGIN_BODY_VERCODE_VALUE=codeInput.getText().toString();
		loginRequestBodyMap.put(Constants.LOGIN_BODY_USERNAME_KEY,Constants.LOGIN_BODY_USERNAME_VALUE);
		loginRequestBodyMap.put(Constants.LOGIN_BODY_PASSWORD_KEY,Constants.LOGIN_BODY_PASSWORD_VALUE);
		loginRequestBodyMap.put(Constants.LOGIN_BODY_VERCODE_KEY,Constants.LOGIN_BODY_VERCODE_VALUE);

		HttpUtils.sendPostRequest(Constants.EDU_LOGIN_URL, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) {
				try{
					String result = null;

					try {
						result=new String(response.body().bytes(),"gb2312");
						Log.e("test", result);
						JsoupUtils.setViewStateValue(result);
						Map<String, String> returnInfo = JsoupUtils.getNameOrFailedInfo(result);
						studentName = returnInfo.get(Constants.STUDENTNAME);
						if (null != studentName) {
							// 将gb2312编码的学生姓名转为url编码的字符串
							urlEncodeStudentName = URLEncoder.encode(studentName, "gb2312");
							outputInfo = "登录成功，为防止课程数据丢失，请耐心等待不要离开";
							searchScheduleOperation(EduLoginActivity.this);
						} else {
							String failedInfo = returnInfo.get("failedInfo");
							if (null != failedInfo) {
								outputInfo = failedInfo;
								dialog.dismiss();
								initVerCode();
							} else {
								outputInfo = "服务器错误，请重试!";
							}
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						outputInfo = "服务器错误，请重试!";
					} finally {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(EduLoginActivity.this, outputInfo, Toast.LENGTH_SHORT).show();
							}
						});

					}
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		},loginRequestBodyMap,requestHeadersMap);
	}
	private void searchScheduleOperation(final Context context) {
		// 重新拼接好网址
		final String newScheduleUrl = Constants.EDU_SCHEDULE_URL
				.replace("studentName", urlEncodeStudentName)
				.replace("user",Constants.LOGIN_BODY_USERNAME_VALUE);

		// 这里需要注意，OkHttp这里设置requestHeader的Referer时，如果出现中文，会报错。所以之前就对学生姓名进行了url编码
		requestHeadersMap.put(Constants.HEAD_REFERER_KEY, newScheduleUrl);
		scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_SCHOOLYEAR_KEY,EduLoginActivity.getSelectedSchoolYearValue());
		scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_TERM_KEY, EduLoginActivity.getSelectedTermValue());
		//有蜜汁bug，post参数正确不能访问当前学期的课表，其他时间正常，但是少提交一些参数可以访问当前学期，长期使用需要根据时间修正当前学期参数
		boolean flag=EduLoginActivity.getSelectedSchoolYearValue().equals("2017-2018")&&EduLoginActivity.getSelectedTermValue().equals("1");
		getViewState(newScheduleUrl,scheduleRequestBodyMap,requestHeadersMap);
		if(!flag){
			scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_VIEWSTATE_KEY,Constants.LOGIN_BODY_VIEWSTATE_VALUE);
			scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_EVENTARGUMENT_KEY,Constants.SCHEDULE_BODY_EVENTARGUMENT_VALUE);
			scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_EVENTTARGET_KEY,Constants.SCHEDULE_BODY_EVENTTARGET_VALUE);
		}
		// 这里的请求地址和Referer一样，不过这里学生姓名直接用中文也没问题。就和之前的Referer保持一致了，也用那个使用率url编码的地址
		setAndSaveSchedule(context,newScheduleUrl);
	}
	private void searchPyjhOperation(){
		final String pyjhScheduleUrl=Constants.EDU_PYJH_URL.replace("studentName", urlEncodeStudentName)
				.replace("user",Constants.LOGIN_BODY_USERNAME_VALUE);

		try{
			Response response=HttpUtils.postSync(pyjhScheduleUrl,pyjhRequestBodyMap,requestHeadersMap);
			String result = new String(response.body().bytes(), "gb2312");
			JsoupUtils.setViewStateValue(result);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		requestHeadersMap.put(Constants.HEAD_REFERER_KEY, pyjhScheduleUrl);
		pyjhRequestBodyMap.put(Constants.PYJH_BODY_EVENTARGUMENT_KEY,Constants.PYJH_BODY_EVENTARGUMENT_VALUE);
		pyjhRequestBodyMap.put(Constants.PYJH_BODY_EVENTTARGET_KEY,Constants.PYJH_BODY_EVENTTARGET_VALUE);
		pyjhRequestBodyMap.put(Constants.PYJH_BODY_XQ_KEY,Constants.PYJH_BODY_XQ_VALUE);
		pyjhRequestBodyMap.put(Constants.PYJH_BODY_KCXZ_KEY,Constants.PYJH_BODY_KCXZ_VALUE);
		pyjhRequestBodyMap.put(Constants.PYJH_BODY_BUTTON_KEY,Constants.PYJH_BODY_BUTTON_VALUE);
		pyjhRequestBodyMap.put(Constants.PYJH_BODY_VIEWSTATE_KEY,Constants.LOGIN_BODY_VIEWSTATE_VALUE);

		pyjhRequestBodyMap.put(Constants.PYJH_BODY_PAGE_KEY,Constants.PYJH_BODY_PAGE_VALUE);
		pyjhRequestBodyMap.put(Constants.PYJH_BODY_SIZE_KEY,Constants.PYJH_BODY_SIZE_VALUE);
		HttpUtils.sendPostRequest(pyjhScheduleUrl, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				response=HttpUtils.postSync(pyjhScheduleUrl,pyjhRequestBodyMap,requestHeadersMap);
				String result = new String(response.body().bytes(), "gb2312");
				JsoupUtils.finishCourseData(result);
					// 云端查询课程名，不存在则存到云端
				saveCourseInCloud();
			}
		},pyjhRequestBodyMap,requestHeadersMap);
	}
	private void setAndSaveSchedule(final Context context,String newScheduleUrl){
			HttpUtils.sendPostRequest(newScheduleUrl, new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {

				}
				@Override
				public void onResponse(Call call, Response response) throws IOException {
					try {
						if (null != response) {
							String result = new String(response.body().bytes(), "gb2312");
							Log.e("test", result);
							JsoupUtils.setViewStateValue(result);
							courseList = JsoupUtils.getCourseList(result);
							if (null != courseList) {
								outputInfo = "获取课表成功!";

								//查询新课表时，删除本地原有课表
								DataSupport.deleteAll(Course.class);
								//查询本地数据库，不存在则存入数据库
								boolean finished=false;
								for(String date:time){

									for(Map<String,Course[]> temp:courseList){
										Course[] courseArray=temp.get(date);
										if(null!=courseArray){
											for(Course course:courseArray){
												if (DaoUtil.courseQueryByCourseData(course.getCoursedata())==null){
													boolean flag=course.save();
													if(!flag)
														Toast.makeText(EduLoginActivity.this,"存储课表失败",Toast.LENGTH_SHORT).show();
												}

											}

										}
									}
									finished=true;
								}
								if(finished)
									searchPyjhOperation();
							} else {
								outputInfo = "获取课表失败,请重试!";
							}
						} else {
							outputInfo = "获取课表失败,请重试!";
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						outputInfo = "获取课表失败,请重试!";
					}
				}
			}, scheduleRequestBodyMap, requestHeadersMap);


	}
	private void getViewState(String Url,Map<String,String> RequestBodyMap,Map<String,String> requestHeadersMap){
		try{
			//获取课程表界面的viewstate
			Response response=HttpUtils.postSync(Url,RequestBodyMap,requestHeadersMap);
			String result=new String(response.body().bytes(),"gb2312");
			JsoupUtils.setViewStateValue(result);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			case 1:
				if(resultCode==0){
					finish();
				}
				if(resultCode==1){
					setTimeAdapter();
				}
				break;
			default:
				break;
		}
	}

	private void setTimeAdapter() {
		ArrayList<String> mList = new ArrayList<String>();
		String strTime= PerferencesUtils.getUserStringData(this,id,"入学年份");
		if("".equals(strTime)){
			startActivityForResult(new Intent(EduLoginActivity.this,ListGradeActivity.class),1);
		}
		else {
			int time = Integer.parseInt(strTime);
			for(int i = 0;i < 4;i++) {
				mList.add((time+i)+"-"+(time+i+1));
			}
			ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(EduLoginActivity.this, android.R.layout.simple_spinner_item, mList);

			mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_school_year.setAdapter(mAdapter);
		}

	}

	private void initIntent() {
		Intent intent = getIntent();
		if(intent.getByteArrayExtra("verificationCode")!=null)
		byteCode = intent.getByteArrayExtra("verificationCode");
	}

	public static String getSelectedSchoolYearValue() {
		return sp_school_year.getSelectedItem().toString();
	}

	public static String getSelectedTermValue() {
		return sp_term.getSelectedItem().toString();
	}

	private void setTarget(){
		sp_school_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Constants.SCHEDULE_BODY_EVENTTARGET_VALUE="xnd";
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		sp_term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Constants.SCHEDULE_BODY_EVENTTARGET_VALUE="xqd";
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void saveCourseInCloud(){
		List<Course> list=DataSupport.findAll(Course.class);
		for(Course course:list){
			final Course[] courses=new Course[1];
			courses[0]=course;
			AVQuery<AVObject> query=new AVQuery<>("Course");
			query.whereEqualTo("coursedata",course.getCoursedata());
			query.findInBackground(new FindCallback<AVObject>() {
				@Override
				public void done(List<AVObject> list, AVException e) {
					if(list!=null&&e==null){
						if(list.size()!=0){
							Log.e("test","existed");
						}
						else {
							AVObject AVcourse=new AVObject("Course");
							AVcourse.put("coursedata",courses[0].getCoursedata());
							AVcourse.put("teacher",courses[0].getTeacher());
							AVcourse.put("courseName",courses[0].getCourseName());
							AVcourse.put("courseType",courses[0].getCourseType());
							AVcourse.put("isSingle",courses[0].getIsSingle());
							AVcourse.put("isDouble",courses[0].getIsDouble());
							AVcourse.put("start",courses[0].getStart());
							AVcourse.put("end",courses[0].getEnd());
							if(courses[0].getLength()!="")
								AVcourse.put("length",Integer.parseInt(courses[0].getLength()));
							AVcourse.put("weekLength",courses[0].getWeekLength());
							AVcourse.put("date",courses[0].getDate());
							AVcourse.put("duration",courses[0].getDuration());
							AVcourse.put("classroom",courses[0].getClassroom());
							AVcourse.put("courseBeginNumber",courses[0].getCourseBeginNumber());
							AVcourse.put("testType",courses[0].getTestType());
							AVcourse.put("point",courses[0].getPoint());
							AVcourse.saveInBackground(new SaveCallback() {
								@Override
								public void done(AVException e) {
									if(e==null){
										Log.e("test","ok");
									}
								}
							});
						}
					}
				}
			});
		}
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(EduLoginActivity.this, outputInfo, Toast.LENGTH_SHORT).show();
				setResult(1);
				dialog.dismiss();
				EduLoginActivity.this.finish();

			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.edulogin_refresh:
				initVerCode();
				break;
			case R.id.edulogin_login:
				if((!username.getText().toString().equals(""))
						&&(!password.getText().toString().equals(""))
						&&(!username.getText().toString().equals(""))){
					addRequestBody();
					dialog=new LoadingDialog(this);
					dialog.setMessage("获取课表中").show();
				}else{
					Toast.makeText(EduLoginActivity.this,"请完整填写信息哦",Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.edu_back_home:
				EduLoginActivity.this.finish();
				break;
		}
	}

}
