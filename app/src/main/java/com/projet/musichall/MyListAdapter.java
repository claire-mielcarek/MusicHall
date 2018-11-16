package com.projet.musichall;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;




public class MyListAdapter extends BaseAdapter {
    private List<String> data;
    private Context context;
    private int count;



    public MyListAdapter(Context context, List<String> noms){
        data = noms;
        count = data.size();
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            list = new View(context);

            // get layout from mobile.xml
            list = inflater.inflate(R.layout.simple_list_view, null);

            if (list != null) {
                // set image based on selected text
                TextView textView = (TextView) list.findViewById(R.id.text_list_view);
                textView.setText(data.get(position%count));    //.toCharArray(), 0, data[position].length()
            }
        }else{
            list = convertView;
        }

        return list;
    }

    public List<String> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
}