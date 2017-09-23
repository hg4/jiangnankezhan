package com.example.hg4.jiangnankezhan.Utils;

import android.os.Handler;
import android.os.Looper;

import com.example.hg4.jiangnankezhan.Model.Info;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by HG4 on 2017/9/5.
 */

public class HttpUtils {
	private static HttpUtils mHttpUtil;
	private OkHttpClient.Builder mOkHttpClientBuilder;
	private OkHttpClient mOkHttpClient;

	private HttpUtils() {
		mOkHttpClientBuilder = new OkHttpClient.Builder();
		mOkHttpClientBuilder.cookieJar(new CookieJar());
		mOkHttpClient = mOkHttpClientBuilder.build();
	}

	private static HttpUtils getInstance() {
		if (mHttpUtil == null) {
			synchronized (HttpUtils.class) {
				if (mHttpUtil == null) {
					mHttpUtil = new HttpUtils();
				}
			}
		}
		return mHttpUtil;
	}
	private Response mPostSync(String url, Info[] params,
							   Info... headers) throws IOException {
		Request request = buildPostRequest(url, params, headers);
		Response response = mOkHttpClient.newCall(request).execute();
		return response;
	}
	private Response mPostSync(String url, Map<String,String> params,Map <String,String> headers) throws IOException {
		Request request = buildPostRequest(url, mapToInfos(params),mapToInfos(headers));
		Response response = mOkHttpClient.newCall(request).execute();
		return response;
	}
	private void mSendGetRequest(String url,Callback callback){
		Request request=new Request.Builder().url(url).build();
		mOkHttpClient.newCall(request).enqueue(callback);
	}

	private void mSendPostRequest(String url, Callback callback, Info[] params, Info... headers) {
		Request request=buildPostRequest(url,params,headers);
		mOkHttpClient.newCall(request).enqueue(callback);
	}
	private void mSendPostRequest(String url,Callback callback,Map<String,String> params,Map <String,String> headers){
		Request request=buildPostRequest(url,mapToInfos(params),mapToInfos(headers));
		mOkHttpClient.newCall(request).enqueue(callback);
	}

	private Info[] mapToInfos(Map<String, String> params) {
		int index = 0;

		if (params == null) {
			return new Info[0];
		}
		int size = params.size();

		Info[] res = new Info[size];
		Set<Map.Entry<String, String>> entries = params.entrySet();
		for (Map.Entry<String, String> entry : entries) {
			res[index++] = new Info(entry.getKey(), entry.getValue());
		}
		return res;
	}

	/**
	 * 构建post请求参数
	 */
	private Request buildPostRequest(String url, Info[] params, Info... headers) {
		if (headers == null) {
			headers = new Info[0];
		}
		Headers.Builder headBuilder = new Headers.Builder();
		for (Info header : headers) {
			headBuilder.add(header.getKey(), header.getValue());
		}
		Headers requestHead = headBuilder.build();
		if (params == null) {
			params = new Info[0];
		}
		FormBody.Builder formBuilder = new FormBody.Builder();
		for (Info param : params) {
			formBuilder.add(param.getKey(), param.getValue());
		}
		RequestBody requestBody = formBuilder.build();
		return new Request.Builder()
				.url(url)
				.headers(requestHead)
				.post(requestBody)
				.build();
	}
	public static void sendPostRequest(String url, Callback callback, Info[] params, Info... headers){
		getInstance().mSendPostRequest(url,callback,params,headers);
	}
	public static void sendPostRequest(String url, Callback callback, Map<String,String> params, Map<String,String> headers){
		getInstance().mSendPostRequest(url,callback,params,headers);
	}
	public static void sendGetRequest(String url,Callback callback){
		getInstance().mSendGetRequest(url,callback);
	}
	public static Response postSync(String url, Map<String,String> params,Map <String,String> headers) throws IOException {
		return getInstance().mPostSync(url, params, headers);
	}
}

