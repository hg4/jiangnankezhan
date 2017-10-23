package com.example.hg4.jiangnankezhan.Utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HG4 on 2017/9/19.
 */

public class TimeUtils {
	public static Long getTimeOfWeekStart(){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.clear(Calendar.MINUTE);
		ca.clear(Calendar.SECOND);
		ca.clear(Calendar.MILLISECOND);
		ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());
		return ca.getTimeInMillis();
	}
	public static long getTimeOfMonthStart(){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.clear(Calendar.MINUTE);
		ca.clear(Calendar.SECOND);
		ca.clear(Calendar.MILLISECOND);
		ca.set(Calendar.DAY_OF_MONTH, 1);
		return ca.getTimeInMillis();
	}

	public static long getTimeOfYearStart(){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.clear(Calendar.MINUTE);
		ca.clear(Calendar.SECOND);
		ca.clear(Calendar.MILLISECOND);
		ca.set(Calendar.DAY_OF_YEAR, 1);
		return ca.getTimeInMillis();
	}
	public static String dateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startTime = sdf.format(date);
		return startTime;
	}
	public static String dateToHMSString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String startTime = sdf.format(date);
		return startTime;
	}
}
