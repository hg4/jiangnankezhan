package com.example.hg4.jiangnankezhan;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

/**
 * Created by HG4 on 2017/10/7.
 */

public class RefreshHeaderView extends AppCompatTextView implements SwipeRefreshTrigger, SwipeTrigger {

	public RefreshHeaderView(Context context) {
		super(context);
	}

	public RefreshHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onRefresh() {
		setText("刷新中");
	}

	@Override
	public void onPrepare() {
		setText("");
	}

	@Override
	public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
		if (!isComplete) {
			if (yScrolled >= getHeight()) {
				setText("释放刷新");
			} else {
				setText("上拉刷新");
			}
		} else {
			setText("");
		}
	}

	@Override
	public void onRelease() {
	}

	@Override
	public void onComplete() {
		setText("刷新完成");
	}

	@Override
	public void onReset() {
		setText("");
	}
}