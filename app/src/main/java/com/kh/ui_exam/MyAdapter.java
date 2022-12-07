package com.kh.ui_exam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<StudentVo> sample;


    public MyAdapter(Context context, ArrayList<StudentVo> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public StudentVo getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview, null);

        // id 찾기
        TextView tvSno = view.findViewById(R.id.tvSno);
        TextView tvSname = view.findViewById(R.id.tvSname);
        TextView tvScore = view.findViewById(R.id.tvScore);

        tvSno.setText(sample.get(position).getSno());
        tvSname.setText(sample.get(position).getSname());
        tvScore.setText(String.valueOf(sample.get(position).getScore()));

        return view;
    }


}
