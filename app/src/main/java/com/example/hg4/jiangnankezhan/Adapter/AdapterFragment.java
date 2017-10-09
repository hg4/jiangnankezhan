package com.example.hg4.jiangnankezhan.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by HG4 on 2017/10/6.
 */

public class AdapterFragment extends FragmentStatePagerAdapter {
	private List<Fragment> mFragments;

	public AdapterFragment(FragmentManager fm, List<Fragment> mFragments) {
		super(fm);
		this.mFragments = mFragments;
	}

	@Override
	public Fragment getItem(int position) {//必须实现
		return mFragments.get(position);
	}

	@Override
	public int getCount() {//必须实现
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {//选择性实现
		return mFragments.get(position).getClass().getSimpleName();
	}
}
