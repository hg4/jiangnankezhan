package com.example.hg4.jiangnankezhan.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by HG4 on 2017/8/30.
 */

public class Utilty {
	public  static boolean nameIsValid(String name){
		return true;
	}
	public static boolean phoneIsValid(String phone){return true;}
	public static boolean emailIsValid(String email){return true;}
	 /* 把Bitmap转Byte
     */
	public static byte[] Bitmap2Bytes(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
}
