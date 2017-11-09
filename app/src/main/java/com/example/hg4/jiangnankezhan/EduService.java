package com.example.hg4.jiangnankezhan;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.Model.Course;
import com.example.hg4.jiangnankezhan.Utils.Constants;
import com.example.hg4.jiangnankezhan.Utils.HttpUtils;
import com.example.hg4.jiangnankezhan.Utils.JsoupUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EduService extends Service {
	private static String urlEncodeStudentName = null;
	private static Map<String, String> requestHeadersMap,pyjhRequestBodyMap;
	public EduService() {

	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent,int flags, int startId) {
		urlEncodeStudentName=intent.getStringExtra("studentName");
		initRequestData();
		searchPyjhOperation();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	private void initRequestData(){
		requestHeadersMap=new LinkedHashMap<>();
		pyjhRequestBodyMap=new LinkedHashMap<>();
		requestHeadersMap.put(Constants.HEAD_HOST_KEY,Constants.HEAD_HOST_VALUE);
		requestHeadersMap.put(Constants.HEAD_AGENT_KEY,Constants.HEAD_AGENT_VALUE);
		requestHeadersMap.put(Constants.HEAD_REFERER_KEY,Constants.HEAD_REFERER_LOGIN);

	}
	private void searchPyjhOperation(){
		final String pyjhScheduleUrl= Constants.EDU_PYJH_URL.replace("studentName", urlEncodeStudentName)
				.replace("user",Constants.LOGIN_BODY_USERNAME_VALUE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					Response response= HttpUtils.postSync(pyjhScheduleUrl,pyjhRequestBodyMap,requestHeadersMap);
					String result = new String(response.body().bytes(), "gb2312");
					JsoupUtils.setViewStateValue(result);
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
				catch (Exception e){
					e.printStackTrace();
				}
			}
		}).start();

	}
	private void saveCourseInCloud(){
		List<Course> list= DataSupport.findAll(Course.class);
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
							if(!courses[0].getLength().equals(""))
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
		stopService(new Intent(EduService.this,EduService.class));
	}
}
