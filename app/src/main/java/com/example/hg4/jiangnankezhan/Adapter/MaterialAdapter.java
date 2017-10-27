package com.example.hg4.jiangnankezhan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hg4.jiangnankezhan.R;

import java.util.List;

/**
 * Created by Administrator on 2017/10/25.
 */

public class MaterialAdapter extends ArrayAdapter<String> {
    private int resourceId;
    public MaterialAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String item=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView title=(TextView)view.findViewById(R.id.item);
        title.setText(item);
        return view;

    }
}
