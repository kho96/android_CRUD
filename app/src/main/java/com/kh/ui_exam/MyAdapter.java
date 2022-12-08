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
    // 어댑터에서 사용될 context, data, view가 필요하다.

    public MyAdapter(Context context, ArrayList<StudentVo> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    // 3개짜리 생성자.. 필요 (context, data, view)

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
        // if (view == null) { view = View.inflate(context, this.view, null);

        // id 찾기
        TextView tvSno = view.findViewById(R.id.tvSno);
        TextView tvSname = view.findViewById(R.id.tvSname);
        TextView tvScore = view.findViewById(R.id.tvScore);

        //StudentVo studentVo = sample.get(position)
        //tvSno.setText(studentVo.getSno());

        // 찾은 id에 값 넣기
        tvSno.setText(sample.get(position).getSno());
        tvSname.setText(sample.get(position).getSname());
        tvScore.setText(String.valueOf(sample.get(position).getScore()));

        return view;
    }


}
