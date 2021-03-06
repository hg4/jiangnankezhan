package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.example.hg4.jiangnankezhan.Utils.Utilty;

import org.litepal.crud.DataSupport;

import java.util.Arrays;
import java.util.List;

import static com.example.hg4.jiangnankezhan.R.id.examinetype;
import static com.example.hg4.jiangnankezhan.R.id.week;
import static org.litepal.crud.DataSupport.find;
import static org.litepal.crud.DataSupport.where;

public class CosDetailsActivity extends BaseActivity implements View.OnClickListener {
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
    private String begintime;
    private String endtime;
    private String examtype;

    private TextView cosname;
    private TextView costime;
    private TextView cosnumber;
    private TextView cosclassroom;
    private TextView cosweek;
    private TextView cosexamtype;
    private TextView costype;
    private TextView coscredit;
    private TextView costeacher;
    private ConstraintLayout rootview;
    private ImageView back;
    private TextView require;
    private ImageView require1;
    private ImageView content_button;
    private ConstraintLayout content_layout;
    private ConstraintLayout material_layout;
    private int courseBeginNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cos_details);
        rootview=(ConstraintLayout)findViewById(R.id.rootview);
        courseBeginNumber=getIntent().getIntExtra("courseBeginNumber",0);
        Name = getIntent().getStringExtra("name");
        weekday = getIntent().getStringExtra("date");
        teacher = getIntent().getStringExtra("teacher");
		incrementBrowse();
        content_button=(ImageView)findViewById(R.id.content_button);
        content_layout=(ConstraintLayout)findViewById(R.id.content_layout);
        material_layout=(ConstraintLayout)findViewById(R.id.material);
        require = (TextView) findViewById(R.id.requirement);
        require1 = (ImageView) findViewById(R.id.require);
        back = (ImageView) findViewById(R.id.back);
        require.setOnClickListener(this);
        require1.setOnClickListener(this);
        back.setOnClickListener(this);
        content_layout.setOnClickListener(this);
        content_button.setOnClickListener(this);
        material_layout.setOnClickListener(this);
        cosname = (TextView) findViewById(R.id.cosname);
        costime = (TextView) findViewById(R.id.costime);
        cosnumber = (TextView) findViewById(R.id.cosnumber);
        cosclassroom = (TextView) findViewById(R.id.cosclassroom);
        cosweek = (TextView) findViewById(R.id.cosweek);
        costype = (TextView) findViewById(R.id.costype);
        coscredit = (TextView) findViewById(R.id.coscredit);
        cosexamtype=(TextView)findViewById(R.id.cosexamtype);
        costeacher = (TextView) findViewById(R.id.costeacher);
        List<Course> courses = DataSupport.where("courseName=? and teacher=? and date=? and courseBeginNumber=?", Name, teacher, weekday,courseBeginNumber+"").find(Course.class);
        if (courses.size()!=0&&courses!=null){
            for (final Course course : courses) {
                classroom = course.getClassroom();
                cosbeginweek = course.getStart();
                cosendweek = course.getEnd();
                cosbeginnumber = course.getCourseBeginNumber() - 1;
                cosendnumber = cosbeginnumber + Integer.parseInt(course.getLength()) - 1;
                courseType = course.getCourseType();
                point=course.getPoint();
                examtype=course.getTestType();
                costeacher.setText(teacher);
                cosname.setText(Name);
                cosclassroom.setText(classroom);
                cosnumber.setText(weekday + " " + cosbeginnumber + "-" + cosendnumber + "节");
                cosweek.setText(cosbeginweek + "-" + cosendweek + "周");
                costype.setText(courseType);
                coscredit.setText(point);
                cosexamtype.setText(examtype);
                setTime();
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
                            point=avObject.getString("point");
                            examtype=avObject.getString("testType");
                            costeacher.setText(teacher);
                            cosname.setText(Name);
                            cosclassroom.setText(classroom);
                            cosnumber.setText(weekday + " " + cosbeginnumber + "-" + cosendnumber + "节");
                            cosweek.setText(cosbeginweek + "-" + cosendweek + "周");
                            costype.setText(courseType);
                            coscredit.setText(point);
                            cosexamtype.setText(examtype);
                            setTime();
                        } else {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        if(!Utilty.isNetworkAvailable(this))
            Utilty.showNetworkFail(this,rootview);
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
                intent.putExtra("courseName",Name);
                intent.putExtra("teacher",teacher);
                startActivity(intent);
                break;
            case R.id.require:
                Intent intent1= new Intent(CosDetailsActivity.this,RequirementsActivity.class);
                intent1.putExtra("courseName",Name);
                intent1.putExtra("teacher",teacher);
                startActivity(intent1);
                break;
            case R.id.content_button:
                Intent intent2= new Intent(CosDetailsActivity.this,CosContentActivity.class);
                intent2.putExtra("courseName",Name);
                intent2.putExtra("teacher",teacher);
                startActivity(intent2);
                break;
            case R.id.content_layout:
                Intent intent3= new Intent(CosDetailsActivity.this,CosContentActivity.class);
                intent3.putExtra("courseName",Name);
                intent3.putExtra("teacher",teacher);
                startActivity(intent3);
                break;
            case R.id.material:
                Intent intent4= new Intent(CosDetailsActivity.this,MaterialActivity.class);
                intent4.putExtra("courseName",Name);
                intent4.putExtra("teacher",teacher);
                startActivity(intent4);
                break;
        }
    }
    void incrementBrowse(){
		AVQuery<AVObject> avQuery=new AVQuery<>("Course");
		avQuery.whereEqualTo("courseName",Name);
		avQuery.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if(list!=null){
					for(AVObject obj:list){
						obj.increment("browse");
						obj.increment("hot");
						obj.saveInBackground();
					}
				}
			}
		});
	}
}