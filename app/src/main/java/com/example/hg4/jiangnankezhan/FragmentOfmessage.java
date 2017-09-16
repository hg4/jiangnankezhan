package com.example.hg4.jiangnankezhan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/9/10.
 */

public class FragmentOfmessage extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contain, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_message,contain,false);
        return view;
    }
}
