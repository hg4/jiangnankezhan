package com.example.hg4.jiangnankezhan.Utils;

import android.util.Log;

import com.example.hg4.jiangnankezhan.Model.Course;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HG4 on 2017/9/6.
 */

public class JsoupUtils {
	private static JsoupUtils mJsoupUtil;
	private int[] courseBeginNumber={2,4,7,9,11};
	private String[] time;

	private JsoupUtils() {
		time = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
	}

	private static JsoupUtils getInstance() {
		if (mJsoupUtil == null) {
			synchronized (HttpUtils.class) {
				if (mJsoupUtil == null) {
					mJsoupUtil = new JsoupUtils();
				}
			}
		}
		return mJsoupUtil;
	}



	private Map<String, String> _getNameOrFailedInfo(String html) {
		Map<String, String> returnInfo = new LinkedHashMap<>();
		if (null != html) {
			Document document = Jsoup.parse(html);
			Element nameElement = document.getElementById("xhxm");
			if (null != nameElement) {
				String studentName = nameElement.html();
				Pattern p = Pattern.compile("(.+)[^同学]");
				Matcher m = p.matcher(studentName);
				if(m.find()) {
					String name=m.group().split(" ")[1];
					returnInfo.put(Constants.STUDENTNAME, name);
				}
			} else { // 找不到学生姓名，说明登录失败，跳转回了登录界面。这里取得登录失败的原因后返回
				Element infoElement = document.select("script[defer]").first();
				if (null != infoElement) {
					String login_failed_info = infoElement.html();
					Pattern p = Pattern.compile("([\\u4E00-\\u9FA5]+)");
					Matcher m = p.matcher(login_failed_info);
					if(m.find()) {
						returnInfo.put("failedInfo", m.group());
					}
				}
			}
		}
		return returnInfo;
	}
	private void  _finishCourseData(String html){
		if (html!=null) {
			Document document = Jsoup.parse(html);
			Elements table = document.getElementsByTag("table");
			Element data = table.get(2);
			Elements tr = data.getElementsByTag("tr");
			for(int i=1;i<tr.size();i++){
				Elements td = tr.get(i).getElementsByTag("td");
				String courseName=td.get(1).text();
				String point=td.get(2).text();
				String testType=td.get(4).text();
				if(!RegexUtil.regexMatches(courseName,".+（\\S）").get(0).equals("0")){
					courseName=courseName.split("（")[0]+"("+courseName.split("（")[1].substring(0,1)+")";
				}
				List<Course> courseList=DaoUtil.courseQueryByName(courseName);
				if(courseList.size()!=0&&courseList!=null){
					for(int j=0;j<courseList.size();j++){
						Course course=courseList.get(j);
						course.setTestType(testType);
						course.setPoint(point);
						course.save();
					}
				}
			}
		}
	}
	private List<Map<String, Course[]>> _getCourseList(String html) {
		List<Map<String, Course[]>> courseList =  new ArrayList<>();

		if (null == html) {
			return courseList;
		}

		Document document = Jsoup.parse(html);
		Elements table = document.getElementsByTag("table");
		Element data = table.get(1);

		Elements tr = data.getElementsByTag("tr");

		int index = 0;
		for (int n=0;n<courseBeginNumber.length;n++) {
			int i=courseBeginNumber[n];
			Elements td = tr.get(i).getElementsByTag("td");
			Elements course = td.select("td[align=\"Center\"]");

			Map<String, Course[]> map = new HashMap<String, Course[]>();
			for (int j = 0; j < course.size(); j++) {
				String content = course.get(j).text();
				String length=course.get(j).attr("rowspan");
				if (content.length()>=2) {
					Course[] courses=parseCourseData(content,length,i,time[index]);
					map.put(time[index], courses);
				}
				index++;
			}
			courseList.add(map);
			index = 0;
		}
		return courseList;
	}
	private Course[] parseCourseData(String content,String length,int courseBeginNumber,String date){
		String[] data=content.split(" ");
		if (data.length > 5) {
			String coursedata1=data[0]+" "+data[1]+" "+data[2]+" "+data[3]+" "+data[4];
			String coursedata2=data[5]+" "+data[6]+" "+data[7]+" "+data[8]+" "+data[9];
			if(!RegexUtil.regexMatches(data[0],".+（\\S）").get(0).equals("0")){
				data[0]=data[0].split("（")[0]+"("+data[0].split("（")[1].substring(0,1)+")";
			}
			if(!RegexUtil.regexMatches(data[5],".+（\\S）").get(0).equals("0")){
				data[5]=data[5].split("（")[0]+"("+data[5].split("（")[1].substring(0,1)+")";
			}
			return new Course[]{new Course(coursedata1,length,courseBeginNumber,data[0],data[1],RegexUtil.regexMatches(data[2],"周[一二三四五六日]第\\S+节").get(0),RegexUtil.regexMatches(data[2],"\\{.+\\}").get(0),data[3],data[4],date),
								new Course(coursedata2,length,courseBeginNumber,data[5],data[6],RegexUtil.regexMatches(data[7],"周[一二三四五六日]第\\S+节").get(0),RegexUtil.regexMatches(data[7],"\\{.+\\}").get(0),data[8],data[9],date)};
		}
		else{
			if(!RegexUtil.regexMatches(data[0],".+（\\S）").get(0).equals("0")){
				data[0]=data[0].split("（")[0]+"("+data[0].split("（")[1].substring(0,1)+")";
			}
			return new Course[]{new Course(content,length,courseBeginNumber,data[0],data[1],RegexUtil.regexMatches(data[2],"周[一二三四五六日]第\\S+节").get(0),RegexUtil.regexMatches(data[2],"\\{.+\\}").get(0),data[3],data[4],date)};
		}
	}

	/************************ 以下为外部可以调用的方法 ************************/
	public static void setViewStateValue(String html) {
		if (null != html) {
			Document document = Jsoup.parse(html);
			Element viewstateElement = document.select("input[name=\"__VIEWSTATE\"]").first();

			if (null != viewstateElement) {
				Constants.LOGIN_BODY_VIEWSTATE_VALUE=viewstateElement.attr("value");
			}
		}
	}

	public static Map<String, String> getNameOrFailedInfo(String html) {
		return getInstance()._getNameOrFailedInfo(html);
	}

	public static List<Map<String, Course[]>> getCourseList(String html) {
		return getInstance()._getCourseList(html);
	}
	public static void finishCourseData(String html){
		getInstance()._finishCourseData(html);
	}
}

