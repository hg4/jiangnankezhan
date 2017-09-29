package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.example.hg4.jiangnankezhan.Model.Course;

import org.litepal.crud.DataSupport;

import java.util.Arrays;
import java.util.List;

import static com.example.hg4.jiangnankezhan.R.id.week;
import static org.litepal.crud.DataSupport.where;

public class CosDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String Name;
    private String teacher;
    private String classroom;
    private String weekday;
    private int cosbeginnumber;
    private int cosendnumber;
    private int cosbeginweek;
    private int cosendweek;
    private String courseType;
    private String point;
    private String unpassRate;
    private String begintime;
    private String endtime;
    private String examinetype;

    private TextView cosname;
    private TextView costime;
    private TextView cosnumber;
    private TextView cosclassroom;
    private TextView cosweek;
    private TextView cosexamtype;

    private TextView costype;
    private TextView coscredit;
    private TextView cosfailrate;
    private TextView costeacher;
    private ImageView back;
    private TextView require;
    private ImageView require1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cos_details);
        Name = getIntent().getStringExtra("name");
        weekday = getIntent().getStringExtra("date");
        teacher = getIntent().getStringExtra("teacher");

        require = (TextView) findViewById(R.id.requirement);
        require1 = (ImageView) findViewById(R.id.require);
        back = (ImageView) findViewById(R.id.back);
        require.setOnClickListener(this);
        require1.setOnClickListener(this);
        back.setOnClickListener(this);
        cosname = (TextView) findViewById(R.id.cosname);
        costime = (TextView) findViewById(R.id.costime);
        cosnumber = (TextView) findViewById(R.id.cosnumber);
        cosclassroom = (TextView) findViewById(R.id.cosclassroom);
        cosweek = (TextView) findViewById(R.id.cosweek);
        costype = (TextView) findViewById(R.id.costype);
        coscredit = (TextView) findViewById(R.id.coscredit);
        cosexamtype=(TextView)findViewById(R.id.examinetype);
        cosfailrate = (TextView) findViewById(R.id.cosfailrate);
        costeacher = (TextView) findViewById(R.id.costeacher);

        List<Course> courses = DataSupport.where("courseName=? and teacher=? and date=?", Name, teacher, weekday).find(Course.class);
        if (courses!=null){
            for (final Course course : courses) {
                classroom = course.getClassroom();
                cosbeginweek = course.getStart();
                cosendweek = course.getEnd();
                cosbeginnumber = course.getCourseBeginNumber() - 1;
                cosendnumber = cosbeginnumber + Integer.parseInt(course.getLength()) - 1;
                courseType = course.getCourseType();
                point=course.getPoint();
                examinetype=course.getTestType();
            }
        }else {
            AVQuery<AVObject> query = new AVQuery<>("Course");
            query.whereEqualTo("courseName", Name);
            query.whereEqualTo("date", weekday);
            query.whereEqualTo("teacher", teacher);
            query.getFirstInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    {
                        if (e == null) {
                            classroom = avObject.getString("classroom");
                            cosbeginweek = avObject.getInt("start");
                            cosendweek = avObject.getInt("end");
                            cosbeginnumber = avObject.getInt("courseBeginNumber") - 1;
                            cosendnumber = cosbeginnumber + avObject.getInt("length") - 1;
                            courseType = avObject.getString("courseType");
                            point=Integer.toString(avObject.getInt("point"));
                            examinetype=avObject.getString("testType");
                            costeacher.setText(teacher);
                            cosname.setText(Name);
                            cosclassroom.setText(classroom);
                            cosnumber.setText(weekday + " " + cosbeginnumber + "-" + cosendnumber + "节");
                            cosweek.setText(cosbeginweek + "-" + cosendweek + "周");
                            costype.setText(courseType);
                            coscredit.setText(point);
                            cosexamtype.setText(examinetype);
                            setTime();
                        } else {
                            e.printStackTrace();
                            Toast.makeText(CosDetailsActivity.this, "暂时没有这门课程的数据哦", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        costeacher.setText(teacher);
        cosname.setText(Name);
        cosclassroom.setText(classroom);
        cosnumber.setText(weekday + " " + cosbeginnumber + "-" + cosendnumber + "节");
        cosweek.setText(cosbeginweek + "-" + cosendweek + "周");
        costype.setText(courseType);
        coscredit.setText(point);
        cosexamtype.setText(examinetype);
        setTime();
    }
    private void setTime(){
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
        }
        costime.setText(begintime + "-" + endtime);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                CosDetailsActivity.this.finish();
                break;
            case R.id.requirement:
                Intent intent= new Intent(CosDetailsActivity.this,RequirementsActivity.class);
                startActivity(intent);
                break;
            case R.id.require:
                Intent intent1= new Intent(CosDetailsActivity.this,RequirementsActivity.class);
                startActivity(intent1);
                break;
        }
    }
}