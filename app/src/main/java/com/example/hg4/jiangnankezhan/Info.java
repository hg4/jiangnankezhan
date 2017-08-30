package com.example.hg4.jiangnankezhan;

/**
 * Created by HG4 on 2017/8/29.
 */

public class Info {
	private String key;
	private String value;
	public Info(String key,String value){
		this.key=key;
		this.value=value;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
