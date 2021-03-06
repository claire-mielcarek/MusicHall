package com.projet.musichall;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projet.musichall.R;
import com.projet.musichall.group.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends BaseAdapter{
    private ArrayList<Post> datas;
    private Context context;

    public PostAdapter(Context context, ArrayList<Post> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Post p = (Post) getItem(i);
        Log.d("[ POST_ADAPTER ]", p.toString());
        String writer = p.getWriter();
        String date = p.getDate();
        String content = p.getContent();
        if (view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.post, viewGroup, false);
        }

        TextView writerTextView = (TextView) view.findViewById(R.id.post_writer);
        TextView dateTextView = (TextView) view.findViewById(R.id.post_date);
        TextView contentTextView = (TextView) view.findViewById(R.id.post_content);
        writerTextView.setText(writer);
        dateTextView.setText(date);
        contentTextView.setText(content);

        return view;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public int getCount() {
        //Log.d("[POST_ADAPTER]", "posts : " + datas.toString());
        return datas.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d("[POST_ADAPTER]", "notifyDataSetChanged : " + datas.toString());
    }
}
