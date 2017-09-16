package com.example.hg4.jiangnankezhan.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HG4 on 2017/9/14.
 */

public class RegexUtil {
	public static List<String> regexMatches(String str, String regex){
		ArrayList<String> list=new ArrayList<>();
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(str);
		while (m.find()){
			list.add(m.group());
		}
		if(list.size()==0){
			list.add("0");
		}
		return list;
	}
}
