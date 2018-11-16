package com.projet.musichall.group.chat;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projet.musichall.group.Post;
import com.projet.musichall.R;

import java.util.ArrayList;

//adapter.notifyDataSetChanged();

public class ChatAdapter extends BaseAdapter{
    private ArrayList<Post> datas;
    private Context context;
    private String me;

    public ChatAdapter(Context context, ArrayList<Post> datas, String me) {
        this.context = context;
        this.datas = datas;
        this.me = me;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Post p = datas.get(i);
        String writer = p.getWriter();
        String content = p.getContent();
        View spacingView;

        if (writer.equals(me)){

            view = LayoutInflater.from(context).inflate(R.layout.chat_message, viewGroup, false);
            spacingView = view.findViewById(R.id.mess_spacing_view);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_answer, viewGroup, false);
            spacingView = view.findViewById(R.id.ans_spacing_view);
        }

        TextView writerTextView = (TextView) view.findViewById(R.id.post_writer);
        TextView contentTextView = (TextView) view.findViewById(R.id.post_content);

        writerTextView.setText(writer);
        contentTextView.setText(content);

        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        spacingView.setMinimumWidth(width/3);

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
        return datas.size();
    }
}
