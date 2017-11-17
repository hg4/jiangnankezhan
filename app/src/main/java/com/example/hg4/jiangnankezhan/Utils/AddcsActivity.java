package com.example.hg4.jiangnankezhan.Utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.hg4.jiangnankezhan.R;

import java.util.List;

public class AddcsActivity extends AppCompatActivity {
	private EditText courseNameInput;
	private EditText teacherInput;
	private EditText classroomInput;
	private EditText endInput;
	private EditText durationInput;
	private Button submit;
	private String courseName;
	private String teacher;
	private String classroom;
	int start=4;
	int end;
	int length;
	String point="1.0";
	int isSingle=1;
	int isDouble=1;
	String testType="考查";
	String coursedata;
	int courseBeginNumber;
	String duration;
	String date;
	String weekLength;
	String courseType="素质";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcs);
		courseNameInput=(EditText)findViewById(R.id.courseName);
		teacherInput=(EditText)findViewById(R.id.teacher);
		classroomInput=(EditText)findViewById(R.id.classroom);
		endInput=(EditText)findViewById(R.id.end);
		durationInput=(EditText)findViewById(R.id.duration);
		submit=(Button)findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				courseName=courseNameInput.getText().toString();
				teacher=teacherInput.getText().toString();
				classroom=classroomInput.getText().toString();
				end=Integer.parseInt(endInput.getText().toString());
				duration=durationInput.getText().toString();
				date=duration.split("第")[0];
				weekLength="{第4-"+end+"周}";
				List<String> list=RegexUtil.regexMatches(duration,"\\d{1,}");
				courseBeginNumber=Integer.parseInt(list.get(0))+1;
				length=list.size();
				coursedata=courseName+" "+courseType+" "+duration+weekLength+" "+teacher+" "+classroom;
				AVObject course=new AVObject("Course");
				course.put("courseName",courseName);
				course.put("teacher",teacher);
				course.put("classroom",classroom);
				course.put("coursedata",coursedata);
				course.put("start",start);
				course.put("end",end);
				course.put("courseBeginNumber",courseBeginNumber);
				course.put("duration",duration);
				course.put("weekLength",weekLength);
				course.put("length",length);
				course.put("point",point);
				course.put("isSingle",isSingle);
				course.put("isDouble",isDouble);
				course.put("date",date);
				course.put("testType",testType);
				course.put("courseType",courseType);
				course.saveInBackground(new SaveCallback() {
					@Override
					public void done(AVException e) {
						if(e==null){
							Toast.makeText(AddcsActivity.this,"保存成功 继续加油(*^__^*) ",Toast.LENGTH_SHORT).show();
							courseNameInput.setText("");
							classroomInput.setText("");
							teacherInput.setText("");
							endInput.setText("");
							durationInput.setText("");
						}
					}
				});
			}
		});
	}
}
