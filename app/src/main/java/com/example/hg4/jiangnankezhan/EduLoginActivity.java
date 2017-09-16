package com.example.hg4.jiangnankezhan;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
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
	private static Spinner sp_school_year;
	private static Spinner sp_term;
	private static String outputInfo = null;
	private static String urlEncodeStudentName = null;
	private static Map<String, String> requestHeadersMap, loginRequestBodyMap, scheduleRequestBodyMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edu_login);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		initIntent();
		initView();
		setTimeAdapter();
		initRequestData();
		initRequestBody();
	}
	private void initView(){
		verficationCode=(ImageView)this.findViewById(R.id.edulogin_code);
		initVerCode();
		username=(EditText)this.findViewById(R.id.edulogin_username);
		password=(EditText)this.findViewById(R.id.edulogin_password);
		codeInput=(EditText)this.findViewById(R.id.edulogin_codeinput);
		refreshCode=(Button)this.findViewById(R.id.edulogin_refresh);
		login=(Button)this.findViewById(R.id.edulogin_login);
		refreshCode.setOnClickListener(this);
		login.setOnClickListener(this);
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
						Map<String, String> returnInfo = JsoupUtils.getNameOrFailedInfo(result);
						studentName = returnInfo.get(Constants.STUDENTNAME);
						if (null != studentName) {
							// 将gb2312编码的学生姓名转为url编码的字符串
							urlEncodeStudentName = URLEncoder.encode(studentName, "gb2312");
							outputInfo = "登录成功";
							searchScheduleOperation(EduLoginActivity.this);
						} else {
							String failedInfo = returnInfo.get("failedinfo");
							if (null != failedInfo) {
								outputInfo = failedInfo;
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
		String newScheduleUrl = Constants.EDU_SCHEDULE_URL
				.replace("studentName", urlEncodeStudentName)
				.replace("user",Constants.LOGIN_BODY_USERNAME_VALUE);

		// 这里需要注意，OkHttp这里设置requestHeader的Referer时，如果出现中文，会报错。所以之前就对学生姓名进行了url编码
		requestHeadersMap.put(Constants.HEAD_REFERER_KEY, newScheduleUrl);
		scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_SCHOOLYEAR_KEY,EduLoginActivity.getSelectedSchoolYearValue());
		scheduleRequestBodyMap.put(Constants.SCHEDULE_BODY_TERM_KEY, EduLoginActivity.getSelectedTermValue());

		// 这里的请求地址和Referer一样，不过这里学生姓名直接用中文也没问题。就和之前的Referer保持一致了，也用那个使用率url编码的地址
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
						List<Map<String, Course[]>> courseList = JsoupUtils.getCourseList(result);
						if (null != courseList) {
							outputInfo = "获取课表成功!";
							//查询新课表时，删除本地原有课表
							DataSupport.deleteAll(Course.class);
							//查询本地数据库，不存在则存入数据库
							for(String date:time){
								for(Map<String,Course[]> temp:courseList){
									Course[] courseArray=temp.get(date);
									if(null!=courseArray){
										for(Course course:courseArray){
											if (DaoUtil.courseQueryByCourseData(course.getCoursedata())==null){
												boolean b=course.save();
												Log.d("test",course.getCoursedata());
											}
											else {
												//本地存在则返回数据

											}
										}

										//云端查询课程名，不存在则存到云端
									}

								}
							}

						} else {
							outputInfo = "获取课表失败,请重试!";
						}
					} else {
						outputInfo = "获取课表失败,请重试!";
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					outputInfo = "获取课表失败,请重试!";
				} finally {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(context, outputInfo, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}, scheduleRequestBodyMap, requestHeadersMap);
	}
	private void setTimeAdapter() {
		ArrayList<String> mList = new ArrayList<String>();
		String strTime= PerferencesUtils.getUserStringData(this,id,"入学年份");
		int time = Integer.parseInt(strTime);
		for(int i = 0;i < 4;i++) {
			mList.add((time+i)+"-"+(time+i+1));
		}
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(EduLoginActivity.this, android.R.layout.simple_spinner_item, mList);

		mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_school_year.setAdapter(mAdapter);
	}
	public static void actionStart(Context context, byte[] verificationCode) {
		Intent intent = new Intent(context,EduLoginActivity.class);
		intent.putExtra("verificationCode", verificationCode);
		context.startActivity(intent);
	}

	private void initIntent() {
		Intent intent = getIntent();
		byteCode = intent.getByteArrayExtra("verificationCode");
	}

	public static String getSelectedSchoolYearValue() {
		return sp_school_year.getSelectedItem().toString();
	}

	public static String getSelectedTermValue() {
		return sp_term.getSelectedItem().toString();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.edulogin_refresh:
				initVerCode();
				break;
			case R.id.edulogin_login:
				addRequestBody();
				break;
			case R.id.edu_back_home:
				this.finish();
				break;
		}
	}

}
