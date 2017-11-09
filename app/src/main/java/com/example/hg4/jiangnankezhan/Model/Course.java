package com.example.hg4.jiangnankezhan.Model;

import com.avos.avoscloud.AVObject;
import com.example.hg4.jiangnankezhan.Utils.RegexUtil;


import org.litepal.crud.DataSupport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by HG4 on 2017/9/6.
 */

public class Course extends DataSupport{
	private int id;
	private int courseBeginNumber;
	private int isSingle = 0;
	private int isDouble = 0;
	private int start;
	private int end;
	private String coursedata;
	private String date;
	private String length;
	private String testType;
	private String courseName;
	private String duration;
	private String teacher;
	private String classroom;
	private String weekLength;
	private String courseType;
	private String point;
	private String unpassRate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCoursedata() {
		return coursedata;
	}

	public void setCoursedata(String coursedata) {
		this.coursedata = coursedata;
	}
	public int getCourseBeginNumber() {
		return courseBeginNumber;
	}

	public void setCourseBeginNumber(int courseBeginNumber) {
		this.courseBeginNumber = courseBeginNumber;
	}

	public int getIsSingle() {
		return isSingle;
	}

	public void setIsSingle(int isSingle) {
		this.isSingle = isSingle;
	}

	public int getIsDouble() {
		return isDouble;
	}

	public void setIsDouble(int isDouble) {
		this.isDouble = isDouble;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTeacher() {
		return teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public String getWeekLength() {
		return weekLength;
	}

	public void setWeekLength(String weekLength) {
		this.weekLength = weekLength;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getUnpassRate() {
		return unpassRate;
	}

	public void setUnpassRate(String unpassRate) {
		this.unpassRate = unpassRate;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}
	public Course(String  coursedata,String length, int courseBeginNumber, String courseName, String courseType, String duration, String weekLength, String teacher, String classroom,String date) {
		this.coursedata=coursedata;
		this.courseBeginNumber = courseBeginNumber;
		this.length = length;
		if ("".equals(this.getLength()))
			this.length="1";
		this.courseName = courseName;
		this.courseType = courseType;
		this.duration = duration;
		this.weekLength = weekLength;
		this.teacher = teacher;
		this.classroom = classroom;
		start = Integer.parseInt(RegexUtil.regexMatches(weekLength, "\\d+").get(0));
		if(RegexUtil.regexMatches(weekLength, "\\d+").size()>1)
			end = Integer.parseInt(RegexUtil.regexMatches(weekLength, "\\d+").get(1));
		switch (RegexUtil.regexMatches(weekLength, "[单双]周").get(0)) {
			case "单周":
				isSingle = 1;
				break;
			case "双周":
				isDouble = 1;
				break;
			default:
				isSingle = 1;
				isDouble = 1;
				break;
		}
		this.date = date;
	}
}

