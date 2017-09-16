package com.example.hg4.jiangnankezhan.Utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by HG4 on 2017/9/5.
 */

public class CookieJar implements okhttp3.CookieJar{

	private static List<Cookie> cookies;

	@Override
	public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
		this.cookies =  cookies;
	}

	@Override
	public List<Cookie> loadForRequest(HttpUrl httpUrl) {
		if (null != cookies) {
			return cookies;
		} else {
			return new ArrayList<Cookie>();
		}
	}

	public static void resetCookies() {
			cookies = null;
	}
}
