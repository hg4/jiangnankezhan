package com.example.hg4.jiangnankezhan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/10.
 */

public class FragmentOfschedule extends Fragment   {
    private String[] data={"1","2","3","4","5","6","7","8","9","10","11","12"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup contain, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_schedule,contain,false);
        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.classlist,data);
        ListView listView=(ListView)getView().findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setDivider(null);
    }


}
