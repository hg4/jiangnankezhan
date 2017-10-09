package com.example.hg4.jiangnankezhan;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by HG4 on 2017/10/3.
 */

public class LoadMoreFooterView extends AppCompatTextView implements SwipeTrigger,SwipeLoadMoreTrigger {

	public LoadMoreFooterView(Context context) {
		super(context);
	}

	public LoadMoreFooterView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onLoadMore() {
		setText("正在加载");
	}

	@Override
	public void onPrepare() {
		setText("");
	}

	@Override
	public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
		if (!isComplete) {
			if (yScrolled <= -getHeight()) {
				setText("释放后加载");
			} else {
				setText("下拉加载");
			}
		} else {
			setText("加载中");
		}
	}

	@Override
	public void onRelease() {
		setText("加载中");
	}

	@Override
	public void onComplete() {
		setText("加载完成");
	}

	@Override
	public void onReset() {
		setText("");
	}
}
