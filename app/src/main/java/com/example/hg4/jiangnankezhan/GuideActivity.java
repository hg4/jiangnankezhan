package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hg4.jiangnankezhan.Adapter.DepthPageTransformer;
import com.example.hg4.jiangnankezhan.Adapter.QuickPageAdapter;
import com.example.hg4.jiangnankezhan.R;
import com.example.hg4.jiangnankezhan.Utils.PerferencesUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
	private ViewPager vp;
	private int []imageIdArray;//图片资源的数组
	private List<View> viewList;//图片资源的集合
	private Button start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		ImmersionBar.with(this).transparentBar().init();
		PerferencesUtils.saveFirstOp(GuideActivity.this,false);
		start=(Button)findViewById(R.id.firstopen_next);
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GuideActivity.this,LoginActivity.class));
				GuideActivity.this.finish();
			}
		});
		initViewPager();
	}

	@Override
	public void onPageSelected(int position) {
		if (position == imageIdArray.length - 1){
			start.setVisibility(View.VISIBLE);
			AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(GuideActivity.this, R.anim.alpha);
			start.startAnimation(alphaAnimation);
		}else {
			start.setVisibility(View.GONE);
			start.clearAnimation();
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
	private void initViewPager() {
		vp = (ViewPager) findViewById(R.id.firstopen_guidepager);
		CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
		//实例化图片资源
		imageIdArray = new int[]{R.drawable.firstop_guide1,R.drawable.firstop_guide2,R.drawable.firstop_guide3};
		viewList = new ArrayList<>();
		//获取一个Layout参数，设置为全屏
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

		//循环创建View并加入到集合中
		int len = imageIdArray.length;
		for (int i = 0;i<len;i++){
			//new ImageView并设置全屏和图片资源
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(params);
			imageView.setBackgroundResource(imageIdArray[i]);

			//将ImageView加入到集合中
			viewList.add(imageView);
		}

		//View集合初始化好后，设置Adapter
		vp.setAdapter(new QuickPageAdapter(viewList));
		//设置滑动监听
		vp.setOnPageChangeListener(this);
		vp.setPageTransformer(true,new DepthPageTransformer());
		indicator.setViewPager(vp);


	}
}
