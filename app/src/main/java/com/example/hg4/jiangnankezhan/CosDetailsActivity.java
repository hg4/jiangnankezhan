package com.example.hg4.jiangnankezhan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hg4.jiangnankezhan.Model.Course;

import org.litepal.crud.DataSupport;

import java.util.List;

import static com.example.hg4.jiangnankezhan.R.id.week;
import static org.litepal.crud.DataSupport.where;

public class CosDetailsActivity extends AppCompatActivity {
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

    private TextView cosname;
    private TextView costime;
    private TextView cosnumber;
    private TextView cosclassroom;
    private TextView cosweek;

    private TextView costype;
    private TextView coscredit;
    private TextView cosfailrate;
    private TextView costeacher;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cos_details);
        Name=getIntent().getStringExtra("name");
        weekday=getIntent().getStringExtra("date");
        teacher=getIntent().getStringExtra("teacher");

        back=(ImageView)findViewById(R.id.back);
        cosname=(TextView)findViewById(R.id.cosname);
        costime=(TextView)findViewById(R.id.costime);
        cosnumber=(TextView)findViewById(R.id.cosnumber);
        cosclassroom=(TextView)findViewById(R.id.cosclassroom);
        cosweek=(TextView)findViewById(R.id.cosweek);
        costype=(TextView)findViewById(R.id.costype);
        coscredit=(TextView)findViewById(R.id.coscredit);
        cosfailrate=(TextView)findViewById(R.id.cosfailrate);
        costeacher=(TextView)findViewById(R.id.costeacher);

        List<Course> courses=DataSupport.where("courseName=? and teacher=? and date=?",Name,teacher,weekday ).find(Course.class);
        for(final Course course:courses){
            classroom=course.getClassroom();
            cosbeginweek=course.getStart();
            cosendweek=course.getEnd();
            cosbeginnumber=course.getCourseBeginNumber()-1;
            cosendnumber=cosbeginnumber+Integer.parseInt(course.getLength())-1;
            courseType=course.getCourseType();
        }
        costeacher.setText(teacher);
        cosname.setText(Name);
        cosclassroom.setText(classroom);
        cosnumber.setText(weekday+" "+cosbeginnumber+"-"+cosendnumber+"节");
        cosweek.setText(cosbeginweek+"-"+cosendweek+"周");
        costype.setText(courseType);
        switch (cosbeginnumber){
            case 1:
                begintime="8:00";
                break;
            case 3:
                begintime="9:55";
                break;
            case 6:
                begintime="13:30";
                break;
            case 8:
                begintime="15:25";
                break;
            case 10:
                begintime="18:30";
                break;
        }
        switch (cosendnumber){
            case 2:
                endtime="9:35";
                break;
            case 4:
                endtime="11:30";
                break;
            case 5:
                endtime="12:20";
                break;
            case 7:
                endtime="15:05";
                break;
            case 9:
                endtime="17:00";
                break;
            case 11:
                endtime="19:15";
                break;
            case 12:
                endtime="20:55";
                break;
        }
        costime.setText(begintime+"-"+endtime);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CosDetailsActivity.this.finish();
            }
        });



    }


}
