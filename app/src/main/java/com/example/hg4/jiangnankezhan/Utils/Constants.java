package com.example.hg4.jiangnankezhan.Utils;

import java.security.PublicKey;

/**
 * Created by HG4 on 2017/9/6.
 */

public class Constants {
	//教务系统网址
	public static final String EDU_LOGIN_URL="http://202.195.145.170/jndx/default2.aspx";
	//验证码网址
	public static final String VERTIFICATION_CODE_URL="http://202.195.144.170/jndx/CheckCode.aspx?";
	//培养计划网址
	public static String EDU_PYJH_URL="http://202.195.145.170/jndx/pyjh.aspx?xh="+"user"+"&xm="+"studentName"+"gnmkdm=N121607";
	//课表网址
	public static String EDU_SCHEDULE_URL="http://202.195.145.170/jndx/xskbcx.aspx?xh="+"user"+"&xm="+"studentName"+"&gnmkdm=N121603";
	//学生姓名
	public static String STUDENTNAME = "studentName";
	//请求头
	public static final String HEAD_HOST_KEY="Host";
	public static final String HEAD_HOST_VALUE="202.195.145.170";
	public static final String HEAD_REFERER_KEY="Referer";
	public static final String HEAD_REFERER_LOGIN="http://202.195.145.170/jndx/default2.aspx";
	public static final String HEAD_REFERER_SCHEDULE="http://202.195.145.170/jndx/xs_main.aspx?xh=";
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
	public static String SCHEDULE_BODY_EVENTARGUMENT_VALUE = "";
	public static final String SCHEDULE_BODY_EVENTTARGET_KEY = "__EVENTTARGET";
	public static String SCHEDULE_BODY_EVENTTARGET_VALUE = "";
	public static final String SCHEDULE_BODY_VIEWSTATE_KEY = "__VIEWSTATE";
	public static  String SCHEDULE_BODY_VIEWSTATE_VALUE = "dDwtMTY3ODA2Njg2OTt0PDtsPGk8MT47PjtsPHQ8O2w8aTwxPjtpPDI+O2k8ND47aTw3PjtpPDk+O2k8MTE+O2k8MTM+O2k8MTU+O2k8MjE+O2k8MjM+O2k8MjU+O2k8Mjc+O2k8Mjk+O2k8MzE+Oz47bDx0PHA8cDxsPFRleHQ7PjtsPDEyMDEyLTIwMTMwOz4+Oz47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPHhuO3huOz4+Oz47dDxpPDM+O0A8MjAxNy0yMDE4OzIwMTYtMjAxNzsyMDE1LTIwMTY7PjtAPDIwMTctMjAxODsyMDE2LTIwMTc7MjAxNS0yMDE2Oz4+O2w8aTwwPjs+Pjs7Pjt0PHQ8OztsPGk8MD47Pj47Oz47dDxwPHA8bDxUZXh0Oz47bDzlrablj7fvvJoxMDcwMTE1MjIzOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlp5PlkI3vvJrmnLHmmLblrok7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtpumZou+8mueJqeiBlOe9keW3peeoi+WtpumZojs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85LiT5Lia77ya55S15rCU5bel56iL5Y+K5YW26Ieq5Yqo5YyWOz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDzooYzmlL/nj63vvJrnlLXmsJQxNTAyOz4+Oz47Oz47dDw7bDxpPDE+Oz47bDx0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+O3Q8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47bDxpPDE+Oz47bDx0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+O3Q8QDA8cDxwPGw8UGFnZUNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3VudDtEYXRhS2V5czs+O2w8aTwxPjtpPDA+O2k8MD47bDw+Oz4+Oz47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwwPjtpPDA+O2w8Pjs+Pjs+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8MD47aTwwPjtsPD47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8UGFnZUNvdW50O18hSXRlbUNvdW50O18hRGF0YVNvdXJjZUl0ZW1Db3VudDtEYXRhS2V5czs+O2w8aTwxPjtpPDA+O2k8MD47bDw+Oz4+Oz47Ozs7Ozs7Ozs7Pjs7Pjs+Pjs+Pjs+JprM0lYy4xAkGkUbVgpDaCD1PyY=";
	public static final String SCHEDULE_BODY_SCHOOLYEAR_KEY = "xnd";
	public static final String SCHEDULE_BODY_SCHOOLYEAR_VALUE = "";
	public static final String SCHEDULE_BODY_TERM_KEY = "xqd";
	public static final String SCHEDULE_BODY_TERM_VALUE = "";
	//查询学分、课程考察类型
	public static final String PYJH_BODY_EVENTARGUMENT_KEY="__EVENTARGUMENT";
	public static final String PYJH_BODY_EVENTARGUMENT_VALUE="";
	public static final String PYJH_BODY_EVENTTARGET_KEY = "__EVENTTARGET";
	public static String PYJH_BODY_EVENTTARGET_VALUE = "xq";
	public static final String PYJH_BODY_VIEWSTATE_KEY = "__VIEWSTATE";
	public static final String PYJH_BODY_XQ_KEY="xq";
	public static final String PYJH_BODY_XQ_VALUE="全部";
	public static final String PYJH_BODY_KCXZ_KEY="kcxz";
	public static final String PYJH_BODY_KCXZ_VALUE="全部";
	public static final String PYJH_BODY_BUTTON_KEY="Button1";
	public static final String PYJH_BODY_BUTTON_VALUE="可替换课程";
	public static final String PYJH_BODY_PAGE_KEY="dpDBGrid:txtChoosePage";
	public static final String PYJH_BODY_PAGE_VALUE="1";
	public static final String PYJH_BODY_SIZE_KEY="dpDBGrid:txtPageSize";
	public static final String PYJH_BODY_SIZE_VALUE="100";


}
