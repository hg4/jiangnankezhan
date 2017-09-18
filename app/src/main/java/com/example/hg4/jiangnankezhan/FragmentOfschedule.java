package com.example.hg4.jiangnankezhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hg4.jiangnankezhan.Utils.Constants;
import com.example.hg4.jiangnankezhan.Utils.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/10.
 */

public class FragmentOfschedule extends Fragment implements View.OnClickListener {
    private String[] data={"1","2","3","4","5","6","7","8","9","10","11","12"};
    private Button addCourse;
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
        addCourse=(Button)getView().findViewById(R.id.schedule_add_course);
        addCourse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.schedule_add_course:
                HttpUtils.sendGetRequest(Constants.VERTIFICATION_CODE_URL, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Intent intent=new Intent(getActivity(),EduLoginActivity.class);
                        intent.putExtra("verificationCode",response.body().bytes());
                        startActivityForResult(intent,1);
                    }
                });
                break;
        }
    }
}
