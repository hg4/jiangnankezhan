package com.example.hg4.jiangnankezhan.Utils;

import android.content.Context;
import android.database.Cursor;


import com.example.hg4.jiangnankezhan.LeanCloudApp;
import com.example.hg4.jiangnankezhan.Model.Course;


import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by HG4 on 2017/9/14.
 */

public class DaoUtil {
	public static List<Course> courseQueryByName(String courseName){
		List<Course> list= DataSupport.where("courseName=?",courseName).find(Course.class);
		return list;
	}
	public static List<Course> courseQueryByTeacher(String teacher){
		List<Course> list= DataSupport.where("teacher=?",teacher).find(Course.class);
		return list;
	}
	public static List<Course> courseQueryByTeacherAndName(String courseName,String teacher){
		List<Course> list1=courseQueryByName(courseName);
		List<Course> list2=courseQueryByTeacher(teacher);
		list1.retainAll(list2);
		return list1;
	}
	public static Course courseQueryByCourseData(String courseData){
		return DataSupport.where("coursedata=?",courseData).findFirst(Course.class);
	}

}
