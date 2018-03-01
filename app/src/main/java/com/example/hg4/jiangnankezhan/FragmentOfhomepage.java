package com.example.hg4.jiangnankezhan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.example.hg4.jiangnankezhan.Adapter.FollowerAdapter;
import com.example.hg4.jiangnankezhan.Adapter.HomeCmtAdapter;
import com.example.hg4.jiangnankezhan.Adapter.HomeUserAdapter;
import com.example.hg4.jiangnankezhan.Model.Course;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.example.hg4.jiangnankezhan.Utils.TimeUtils;
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/9/10.
 */

public class FragmentOfhomepage extends Fragment {
    private ImageView head;
    private TextView nowTime;
    private TextView nextTime;
    private TextView nowClass;
    private TextView nextClass;
    private TextView nowCourse;
    private TextView nextCourse;
    private TextView material;
    private AVObject materialObj;
    private AVObject courseObj;
    private TextView course;
	private String teacher;
    private List<AVObject> cmtList=new ArrayList<>();
    private List<AVObject> hotuserlist=new ArrayList<>();
    private ConstraintLayout hotmater;
    private ConstraintLayout hotcourse;
    private RecyclerView cmtRecommend;
    private RecyclerView hotuser;
    private NestedScrollView scroll;
    Integer curWeek=0;
    private List<Course> courseList=new ArrayList<>();
    private AVUser user=AVUser.getCurrentUser();
    static final String[] PERMISSION = new String[]{
            Manifest.permission.READ_CONTACTS,// 写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
            Manifest.permission.WRITE_CALL_LOG,        //读取设备信息
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contain, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_page,contain,false);
        head=(ImageView)view.findViewById(R.id.head);
        nowTime=(TextView)view.findViewById(R.id.nowtime);
        nextTime=(TextView)view.findViewById(R.id.nexttime);
        nowClass=(TextView)view.findViewById(R.id.nowclassroom);
        nextClass=(TextView)view.findViewById(R.id.nextclassroom);
        nowCourse=(TextView)view.findViewById(R.id.nowcourse);
        nextCourse=(TextView)view.findViewById(R.id.nextcourse);
        material=(TextView)view.findViewById(R.id.material);
        course=(TextView)view.findViewById(R.id.course);
        hotuser=(RecyclerView)view.findViewById(R.id.hotuser_recycleview);
        cmtRecommend=(RecyclerView)view.findViewById(R.id.hotcmt_recycleview);
        hotmater=(ConstraintLayout)view.findViewById(R.id.hotmaterial);
        hotcourse=(ConstraintLayout)view.findViewById(R.id.hotcourse);
        hotmater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),DownloadActivity.class);
                intent.putExtra("courseName",materialObj.getString("Course"));
                intent.putExtra("teacher","");
                intent.putExtra("content",materialObj.getString("Title"));
                startActivity(intent);
            }
        });
        hotcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),CosDetailsActivity.class);
                intent.putExtra("courseBeginNumber",courseObj.getInt("courseBeginNumber"));
                intent.putExtra("teacher",courseObj.getString("teacher"));
                intent.putExtra("name",courseObj.getString("courseName"));
                intent.putExtra("date",courseObj.getString("date"));
                startActivity(intent);
            }
        });
        head.setFocusableInTouchMode(true);
        head.requestFocus();//解决nestscrollview嵌套recycleview导致直接显示recycleview第一个item的问题
        sethead();
        setNowAndNext();
        setHotMaterial();
        setHotCourse();
        setHotRec();
        setUser();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // ImageView error=new ImageView(this.getContext());
       // error.setImageResource(R.drawable.net_error);
       // error.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // hpLayout.addView(error);

    }
    private void sethead(){
        if (PerferencesUtils.getBitmapFromSharedPreferences(user.getObjectId(),getContext()) != null)
            head.setImageBitmap(PerferencesUtils.getBitmapFromSharedPreferences(user.getObjectId(),getContext()));
        else {
            AVQuery<AVObject> query = new AVQuery<>("_User");
            query.getInBackground(user.getObjectId(), new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (avObject != null) {
                        AVFile file = avObject.getAVFile("head");
                        if (file != null) {
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, AVException e) {
                                    if (e == null) {
                                        head.setImageBitmap(Utilty.Bytes2Bimap(bytes));
                                    } else e.printStackTrace();
                                }
                            });
                        }
                    }
                }
            });
        }
    }
    private void findCourse(long time,List<Course> list){
        int courseBeginNumber=trans2Begin(time);
        int index=0;
        boolean flag=true;
        for(Course c:list){
            int temp=c.getCourseBeginNumber()-1;
            if(temp==courseBeginNumber){
                nowCourse.setText(c.getCourseName());
                nowClass.setText(c.getClassroom());
                nowTime.setText(setTime(temp,temp + Integer.parseInt(c.getLength()) - 1));
                index++;
                flag=false;
                break;
            }
            index++;
        }
        if(flag==true){
            nowCourse.setText("暂无课程");
        }
        if(index<=list.size()-1){
            int temp=list.get(index).getCourseBeginNumber()-1;
            nextCourse.setText(list.get(index).getCourseName());
            nextClass.setText(list.get(index).getClassroom());
            nextTime.setText(setTime(temp,temp + Integer.parseInt(list.get(index).getLength()) - 1));
        }
        else nextCourse.setText("暂无课程");
    }
    private void setNowAndNext(){
       String hhmmss= TimeUtils.getCurrentDateHHMMSS();
       long nowTime= TimeUtils.getLongTime(hhmmss)/1000;
        String date=TimeUtils.getDayOfWeek();
        String savedWeek=PerferencesUtils.getUserStringData(this.getContext(),user.getObjectId(),"savedWeek");
        if(savedWeek!=""){
            curWeek=Integer.parseInt(savedWeek);
        }
        if(curWeek%2==0){
            courseList=DataSupport.where("start<=? and end>=? and isDouble=1 and date=?",curWeek.toString(),curWeek.toString(),date).find(Course.class);
        }
        else courseList=DataSupport.where("start<=? and end>=? and isSingle=1 and date=?",curWeek.toString(),curWeek.toString(),date).find(Course.class);
        findCourse(nowTime,courseList);
    }
    private void setHotMaterial(){
        AVQuery<AVObject> avQuery=new AVQuery<>("Course_file");
        avQuery.orderByDescending("hot");
        avQuery.setLimit(30);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list!=null){
                    try{
                        long time=TimeUtils.currentDateParserLong();
                        Random random=new Random(time);
                        while(true){
                            int index=random.nextInt(30);
                            if(index<list.size()){
                                materialObj=list.get(index);
                                material.setText(materialObj.getString("Title"));
                                break;
                            }
                        }

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }


            }
        });
    }
    private void setHotCourse(){
		AVQuery<AVObject> avQuery=new AVQuery<>("Course");
		avQuery.orderByDescending("hot");
		avQuery.setLimit(100);
		avQuery.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(list!=null){
					try{
						long time=TimeUtils.currentDateParserLong();
						Random random=new Random(time);
						while(true){
							int index=random.nextInt(100);
							if(index<list.size()){
                                courseObj=list.get(index);
								course.setText(list.get(index).getString("courseName"));
								teacher=list.get(index).getString("teacher");
								break;
							}
						}

					}catch (Exception ex){
						ex.printStackTrace();
					}
				}


			}
		});
    }
    private int time2int(int h,int m,int s){
        return h*3500+m*60+s;
    }
    private int trans2Begin(long time){
        if(time>time2int(8,0,0)&&time<time2int(20,55,0)){
            if(time>time2int(8,0,0)&&time<time2int(9,55,0)){
                return 1;
            }
            if(time>=time2int(9,55,0)&&time<time2int(13,30,0)){
                return 3;
            }
            if(time>=time2int(13,30,0)&&time<time2int(15,25,0)){
                return 8;
            }
            if(time>=time2int(15,25,0)&&time<time2int(20,55,0)){
                return 10;
            }
            return 0;
        }
        else return 0;
    }
    private void setHotRec(){
        AVQuery<AVObject> avQuery=new AVQuery<>("Course_comment");
        avQuery.setLimit(60);
        avQuery.include("from");
        avQuery.orderByDescending("hot");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(list!=null){
                    try{
                        long time=TimeUtils.currentDateParserLong();
                        Random random=new Random(time);
                        int count=0;
                        while(count<3){
                            int index=random.nextInt(60);
                            if(index<list.size()){
                                AVObject cmtObj=list.get(index);
                                cmtList.add(cmtObj);
                                count++;
                            }
                        }
                        HomeCmtAdapter cmtAdapter=new HomeCmtAdapter(cmtList);
                        cmtRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
                        cmtRecommend.setAdapter(cmtAdapter);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

    }
    private void setUser(){
        AVQuery<AVObject> avQuery=new AVQuery<>("_User");
        avQuery.setLimit(10);
        avQuery.orderByDescending("activevalue");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                try{
                    long time=TimeUtils.currentDateParserLong();
                    Random random=new Random(time);
                    int count=0;
                    while(count<3){
                        int index=random.nextInt(list.size());
                        if(index<list.size()){
                            AVObject u=list.get(index);
                            hotuserlist.add(u);
                            list.remove(index);
                            count++;
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
                hotuser.setLayoutManager(layoutManager);
                HomeUserAdapter adapter=new HomeUserAdapter(hotuserlist);
                hotuser.setAdapter(adapter);
            }
        });
    }
    private String setTime(int cosbeginnumber,int cosendnumber){
        String begintime,endtime;
        switch (cosbeginnumber) {
            case 1:
                begintime = "8:00";
                break;
            case 3:
                begintime = "9:55";
                break;
            case 6:
                begintime = "13:30";
                break;
            case 8:
                begintime = "15:25";
                break;
            case 10:
                begintime = "18:30";
                break;
            default:
                begintime="0";
                break;
        }
        switch (cosendnumber) {
            case 2:
                endtime = "9:35";
                break;
            case 4:
                endtime = "11:30";
                break;
            case 5:
                endtime = "12:20";
                break;
            case 7:
                endtime = "15:05";
                break;
            case 8:
                endtime="16:10";
                break;
            case 9:
                endtime = "17:00";
                break;
            case 11:
                endtime = "20:05";
                break;
            case 12:
                endtime = "20:55";
                break;
            default:
                endtime="0";
                break;
        }
        return begintime + "-" + endtime;
    }
}
