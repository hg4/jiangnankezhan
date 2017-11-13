package com.example.hg4.jiangnankezhan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.example.hg4.jiangnankezhan.Model.Course;
import com.example.hg4.jiangnankezhan.Utils.Constants;
import com.example.hg4.jiangnankezhan.Utils.HttpUtils;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.example.hg4.jiangnankezhan.Utils.RegexUtil;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Administrator on 2017/9/10.
 */

public class FragmentOfschedule extends Fragment implements View.OnClickListener {
    private String[] data={"1","2","3","4","5","6","7","8","9","10","11","12"};
    private Button addCourse;
	private ImageView openDrawer;
	private DrawerLayout drawer;
	private TextView week;
	private Button lastWeek;
	private Button nextWeek;
	private Integer curWeek=1;
	private RelativeLayout courseLayout;
	private Button search;
	private int width;
	private int height;
	private float density;
	private static int PERLENGTH;
	private static int PERWIDTH;
	private String id= AVUser.getCurrentUser().getObjectId();
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contain, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_schedule,contain,false);
        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.classlist,data);
        ListView listView=(ListView)getView().findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setDivider(null);
		courseLayout=(RelativeLayout)getView().findViewById(R.id.table_schedule);
		addCourse=(Button)getView().findViewById(R.id.schedule_add_course);
		openDrawer=(ImageView)getView().findViewById(R.id.open_drawer);
		drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
		week=(TextView)getView().findViewById(R.id.schedule_week);
		lastWeek=(Button)getView().findViewById(R.id.week_last);
		nextWeek=(Button)getView().findViewById(R.id.week_next);
		search=(Button)getView().findViewById(R.id.schedule_search);
		getDensity();
		PERWIDTH=width/7-10;
		//PERLENGTH=(int)((height-130*(int)density)/9.2);
		//if(height>2000){
		//	PERLENGTH=(int)((height-130*(int)density)/11.0);
		//}
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		courseLayout.measure(w, h);
		height =courseLayout.getMeasuredHeight();
		PERLENGTH=height/12;
		if(PerferencesUtils.getState(getContext(),id,addCourse.getId())){
			Utilty.leadDialog(getContext(),R.drawable.lead);
			PerferencesUtils.saveState(getContext(),id,addCourse.getId(),false);
		}
		initEvent();
		getmainhead();
		updateTime();
		createschedule();
		Log.e("test", TimeUtils.getTimeOfWeekStart().toString());
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			case 1:
				if(resultCode==1){
					curWeek=1;
					setCurWeek(curWeek);
					createschedule();
				}
				if(resultCode==0){

				}
				break;

			default:
				break;
		}
	}

	@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.schedule_add_course:
            	if (Utilty.isNetworkAvailable(getActivity().getApplicationContext())==true){
					final Intent intent=new Intent(getActivity(),EduLoginActivity.class);
					HttpUtils.sendGetRequest(Constants.VERTIFICATION_CODE_URL, new Callback() {
						@Override
						public void onFailure(Call call, IOException e) {
						}

						@Override
						public void onResponse(Call call, Response response) throws IOException {
							Log.e("test",response.toString());
							intent.putExtra("verificationCode",response.body().bytes());
						}
					});
					startActivityForResult(intent,1);
				}else{
					Toast.makeText(getActivity().getApplicationContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
				}

                break;
			case R.id.open_drawer:
				drawer.openDrawer(GravityCompat.START);
				break;
			case R.id.week_last:
				if(curWeek>1){
					curWeek--;
					setCurWeek(curWeek);
					createschedule();
				}
				break;
			case R.id.week_next:
				if(curWeek<20){
					curWeek++;
					setCurWeek(curWeek);
					createschedule();
				}
				break;
			case R.id.schedule_search:
				Intent intent=new Intent(getActivity(),SearchActivity.class);
				intent.putExtra("findname","Course");
				intent.putExtra("adaptertype",0);
				startActivity(intent);
				break;
        }
    }
	private void getDensity() {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		density=displayMetrics.density;
		width=displayMetrics.widthPixels;
		height=displayMetrics.heightPixels;
		Log.d("test","Density is "+displayMetrics.density+" densityDpi is "+displayMetrics.densityDpi+" height: "+displayMetrics.heightPixels+
				" width: "+displayMetrics.widthPixels);
	}

	private void getmainhead(){
		if(PerferencesUtils.getBitmapFromSharedPreferences(id,getContext())!=null)
			openDrawer.setImageBitmap(PerferencesUtils.getBitmapFromSharedPreferences(id,getContext()));
		else {
			AVQuery<AVObject> query=new AVQuery<>("_User");
			query.getInBackground(id, new GetCallback<AVObject>() {
				@Override
				public void done(AVObject avObject, AVException e) {
					if(avObject!=null){
						AVFile file=avObject.getAVFile("head");
						if(file!=null){
							file.getDataInBackground(new GetDataCallback() {
								@Override
								public void done(byte[] bytes, AVException e) {
									if(e==null){
										openDrawer.setImageBitmap(Utilty.Bytes2Bimap(bytes));
									}
									else e.printStackTrace();
								}
							});
						}
					}
				}
			});

		}
	}
	private void initEvent(){
		addCourse.setOnClickListener(this);
		openDrawer.setOnClickListener(this);
		lastWeek.setOnClickListener(this);
		nextWeek.setOnClickListener(this);
		search.setOnClickListener(this);
		String savedWeek=PerferencesUtils.getUserStringData(this.getContext(),id,"savedWeek");
		if(savedWeek!=""){
			curWeek=Integer.parseInt(savedWeek);
			week.setText("第"+savedWeek+"周");
		}
	}

	private void updateTime(){
		String savedSystemWeek=PerferencesUtils.getUserStringData(this.getContext(),id,"systemWeek");
		String nowSystemWeek=TimeUtils.getTimeOfWeekStart().toString();
		if(savedSystemWeek!=""){
			if(!savedSystemWeek.equals(nowSystemWeek)){
				PerferencesUtils.saveUserStringData(this.getContext(),id,"systemWeek",nowSystemWeek);
				if(curWeek<20)
					setCurWeek(++curWeek);
			}
		}
		else PerferencesUtils.saveUserStringData(this.getContext(),id,"systemWeek",nowSystemWeek);
	}

    private void setCurWeek(Integer curWeek){
		if(curWeek>=1&&curWeek<=20){
			String strWeek=curWeek.toString();
			week.setText("第"+strWeek+"周");
			PerferencesUtils.saveUserStringData(this.getContext(),id,"savedWeek",strWeek);
		}

	}
	private int parseDate(String date){
		int i=1;
		switch (date){
			case "周一":
				i=1;
				break;
			case "周二":
				i=2;
				break;
			case "周三":
				i=3;
				break;
			case "周四":
				i=4;
				break;
			case "周五":
				i=5;
				break;
			default:break;
		}
		return i;
	}
    private void createschedule(){
		courseLayout.removeAllViews();
		List<Course> courseList=new ArrayList<>();
		if(curWeek%2==0){
			courseList=DataSupport.where("start<=? and end>=? and isDouble=1",curWeek.toString(),curWeek.toString()).find(Course.class);
		}
		else courseList=DataSupport.where("start<=? and end>=? and isSingle=1",curWeek.toString(),curWeek.toString()).find(Course.class);
		if(courseList!=null&&courseList.size()!=0){
			for(final Course course:courseList){
				Button courseButton=new Button(this.getContext());
				int length=0;
				if(!course.getLength().equals(""))
					length=Integer.parseInt(course.getLength());
				if(length==1){
					Integer csbgnb=course.getCourseBeginNumber()-2;
					final Course lastCourse=DataSupport.where("courseBeginNumber=? and date=? and courseName=?",csbgnb.toString(),course.getDate(),course.getCourseName())
							.findFirst(Course.class);
					if(lastCourse!=null){
						String name=lastCourse.getCourseName();
						if(!RegexUtil.regexMatches(name,".+（\\S）").get(0).equals("0")){
							name=name.split("（")[0]+"("+name.split("（")[1].substring(0,1)+")";
							lastCourse.setCourseName(name);
							lastCourse.save();
						}
						courseButton.setText(name+"\n"+lastCourse.getClassroom());
						courseButton.setTextSize(12);
						courseButton.setMaxLines(3*3);
						courseButton.setEllipsize(TextUtils.TruncateAt.END);
						courseButton.setBackgroundResource(R.drawable.shape);
						courseButton.setHeight(PERLENGTH*3);
						courseButton.setWidth(PERWIDTH);
						RelativeLayout.LayoutParams btParams=new RelativeLayout.LayoutParams(PERWIDTH,3*PERLENGTH);
						final String date=lastCourse.getDate();
						final int courseBeginNumber=lastCourse.getCourseBeginNumber();
						int i=parseDate(date);
						btParams.leftMargin=(i-1)*PERWIDTH;
						btParams.topMargin=(courseBeginNumber-2)*PERLENGTH;
						courseLayout.addView(courseButton,btParams);
						courseButton.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Intent intent=new Intent(getActivity(),CosDetailsActivity.class);
								intent.putExtra("date",date);
								intent.putExtra("name",lastCourse.getCourseName());
								intent.putExtra("teacher",lastCourse.getTeacher());
								intent.putExtra("courseBeginNumber",lastCourse.getCourseBeginNumber());
								startActivity(intent);
							}
						});
					}
				}
				else {
					String name=course.getCourseName();
					if(!RegexUtil.regexMatches(name,".+（\\S）").get(0).equals("0")){
						name=name.split("（")[0]+"("+name.split("（")[1].substring(0,1)+")";
						course.setCourseName(name);
						course.save();
					}

					courseButton.setText(name+"\n"+course.getClassroom());
					courseButton.setTextSize(12);
					courseButton.setMaxLines(3*length);
					courseButton.setEllipsize(TextUtils.TruncateAt.END);
					courseButton.setBackgroundResource(R.drawable.shape);
					courseButton.setHeight(PERLENGTH*length);
					courseButton.setWidth(PERWIDTH);
					RelativeLayout.LayoutParams btParams=new RelativeLayout.LayoutParams(PERWIDTH,length*PERLENGTH);
					final String date=course.getDate();
					final int courseBeginNumber=course.getCourseBeginNumber();
					int i=parseDate(date);
					btParams.leftMargin=(i-1)*PERWIDTH;
					btParams.topMargin=(courseBeginNumber-2)*PERLENGTH;
					courseLayout.addView(courseButton,btParams);
					courseButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent intent=new Intent(getActivity(),CosDetailsActivity.class);
							intent.putExtra("date",date);
							intent.putExtra("name",course.getCourseName());
							intent.putExtra("teacher",course.getTeacher());
							intent.putExtra("courseBeginNumber",course.getCourseBeginNumber());
							startActivity(intent);
						}
					});
				}
			}
		}
	}
}
