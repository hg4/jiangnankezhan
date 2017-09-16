package com.example.hg4.jiangnankezhan.Utils;

import java.security.PublicKey;

/**
 * Created by HG4 on 2017/9/6.
 */

public class Constants {
	//教务系统网址
	public static final String EDU_LOGIN_URL="http://202.195.144.163/jndx/default2.aspx";
	//验证码网址
	public static final String VERTIFICATION_CODE_URL="http://202.195.144.163/jndx/CheckCode.aspx?";
	//课表网址
	public static String EDU_SCHEDULE_URL="http://202.195.144.163/jndx/xskbcx.aspx?xh="+"user"+"&xm="+"studentName"+"&gnmkdm=N121603";
	//学生姓名
	public static String STUDENTNAME = "studentName";
	//请求头
	public static final String HEAD_HOST_KEY="Host";
	public static final String HEAD_HOST_VALUE="202.195.144.163";
	public static final String HEAD_REFERER_KEY="Referer";
	public static final String HEAD_REFERER_LOGIN="http://202.195.144.163/jndx/default2.aspx";
	public static final String HEAD_REFERER_SCHEDULE="http://202.195.144.163/jndx/xs_main.aspx?xh=";
	public static final String HEAD_AGENT_KEY="User-Agent";
	public static final String HEAD_AGENT_VALUE="Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)";

	//登录时请求参数
	public static final String LOGIN_BODY_VIEWSTATE_KEY="__VIEWSTATE";
	public static String LOGIN_BODY_VIEWSTATE_VALUE="dDwtMTMxNjk0NzYxNTs7PmBHL1DqGk24Rz/u2bGTtjVYYwtT";
	public static final String LOGIN_BODY_USERNAME_KEY="TextBox1";
	public static String LOGIN_BODY_USERNAME_VALUE="user";
	public static final String LOGIN_BODY_PASSWORD_KEY="TextBox2";
	public static String LOGIN_BODY_PASSWORD_VALUE="";
	public static final String LOGIN_BODY_VERCODE_KEY="TextBox3";
	public static String LOGIN_BODY_VERCODE_VALUE="";
	public static final String LOGIN_BODY_TYPE_KEY="RadioButtonList1";
	public static final String LOGIN_BODY_TYPE_VALUE="学生";
	public static final String LOGIN_BODY_BUTTON_KEY="Button1";
	public static final String LOGIN_BODY_BUTTON_VALUE="";
	public static final String LOGIN_BODY_LANGUAGE_KEY="lbLanguage";
	public static final String LOGIN_BODY_LANGUAGE_VALUE="";
	//查询课表时请求参数
	public static final String SCHEDULE_BODY_EVENTARGUMENT_KEY = "__EVENTARGUMENT";
	public static final String SCHEDULE_BODY_EVENTARGUMENT_VALUE = "";
	public static final String SCHEDULE_BODY_EVENTTARGET_KEY = "__EVENTTARGET";
	public static final String SCHEDULE_BODY_EVENTTARGET_VALUE = "";
	public static final String SCHEDULE_BODY_VIEWSTATE_KEY = "__VIEWSTATE";
	public static final String SCHEDULE_BODY_VIEWSTATE_VALUE = "";
	public static final String SCHEDULE_BODY_SCHOOLYEAR_KEY = "xnd";
	public static final String SCHEDULE_BODY_SCHOOLYEAR_VALUE = "";
	public static final String SCHEDULE_BODY_TERM_KEY = "xqd";
	public static final String SCHEDULE_BODY_TERM_VALUE = "";

}
