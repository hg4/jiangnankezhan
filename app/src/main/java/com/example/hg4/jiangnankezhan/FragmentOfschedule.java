package com.example.hg4.jiangnankezhan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.example.hg4.jiangnankezhan.Model.Course;
import com.example.hg4.jiangnankezhan.Utils.Constants;
import com.example.hg4.jiangnankezhan.Utils.HttpUtils;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/10.
 */

public class FragmentOfschedule extends Fragment implements View.OnClickListener {
    private String[] data={"1","2","3","4","5","6","7","8","9","10","11","12"};
    private Button addCourse;
	private Button openDrawer;
	private DrawerLayout drawer;
	private TextView week;
	private Button lastWeek;
	private Button nextWeek;
	private Integer curWeek=1;
	private RelativeLayout courseLayout;
	private ImageView solid;
	private int solidWidth;
	private int width;
	private int height;
	private static final int PERLENGTH=148;
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
		WindowManager wm = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);

		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
		solid=(ImageView)getView().findViewById(R.id.schedule_solid);
		solidWidth=solid.getWidth();
		PERWIDTH=(width-solidWidth)/7-10;
        addCourse=(Button)getView().findViewById(R.id.schedule_add_course);
		openDrawer=(Button)getView().findViewById(R.id.open_drawer);
		drawer = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
		week=(TextView)getView().findViewById(R.id.schedule_week);
		lastWeek=(Button)getView().findViewById(R.id.week_last);
		nextWeek=(Button)getView().findViewById(R.id.week_next);
		courseLayout=(RelativeLayout)getView().findViewById(R.id.table_schedule);
		initEvent();
		updateTime();
		createschedule();
		Log.e("test", TimeUtils.getTimeOfWeekStart().toString());
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			case 1:
				if(resultCode==1){
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
                HttpUtils.sendGetRequest(Constants.VERTIFICATION_CODE_URL, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Intent intent=new Intent(getActivity(),EduLoginActivity.class);
                        intent.putExtra("verificationCode",response.body().bytes());
                        startActivityForResult(intent,1);
                    }
                });
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
        }
    }

    private void initEvent(){
		addCourse.setOnClickListener(this);
		openDrawer.setOnClickListener(this);
		lastWeek.setOnClickListener(this);
		nextWeek.setOnClickListener(this);
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
				courseButton.setText(course.getCourseName()+"\n"+course.getClassroom());
				courseButton.setMaxLines(6);
				courseButton.setTextSize(12);
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
						startActivity(intent);
					}
				});
			}
		}

	}
}
