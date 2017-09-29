package com.example.hg4.jiangnankezhan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/9/10.
 */

public class FragmentOfhomepage extends Fragment {
    private LinearLayout hpLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contain, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_homepage,contain,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hpLayout=(LinearLayout)getView().findViewById(R.id.hp_layout);
        ImageView error=new ImageView(this.getContext());
        error.setImageResource(R.drawable.net_error);
        error.setScaleType(ImageView.ScaleType.CENTER_CROP);
        hpLayout.addView(error);
    }
}
