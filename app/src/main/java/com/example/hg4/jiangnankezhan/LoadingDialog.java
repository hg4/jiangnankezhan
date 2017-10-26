package com.example.hg4.jiangnankezhan;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by HG4 on 2017/10/13.
 */

public class LoadingDialog extends Dialog {
	private TextView loading_text;
	private ImageView loading_pic;
	public LoadingDialog(Context context) {
		super(context);
		/**设置对话框背景透明*/
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setContentView(R.layout.loading);
		loading_text = (TextView) findViewById(R.id.loading_text);
		loading_pic=(ImageView)findViewById(R.id.loading_img);
		Glide.with(context).load(R.drawable.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(loading_pic);
		setCanceledOnTouchOutside(false);
	}

	/**
	 * 为加载进度个对话框设置不同的提示消息
	 *
	 * @param message 给用户展示的提示信息
	 * @return build模式设计，可以链式调用
	 */
	public LoadingDialog setMessage(String message) {
		loading_text.setText(message);
		return this;
	}
}
