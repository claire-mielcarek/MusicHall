package com.projet.musichall.discussion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projet.musichall.R;

import java.util.List;



public class AdapterMessage extends BaseAdapter{
    private Context context;
    private List<String> messages;


    public AdapterMessage(Context context, List<String> messages){
        this.context = context;
        this.messages = messages;
    }


    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list;
        String[] infos = messages.get(position).split(",");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (infos[0].equals("s"))
            list = inflater.inflate(R.layout.discussion_item, null);
        else list = inflater.inflate(R.layout.discussion_item_receiver, null);

        if (list != null) {

            // set informations based on text at position
            TextView author = (TextView) list.findViewById(R.id.nom_user);
            TextView time = (TextView) list.findViewById(R.id.message_time);
            TextView text = (TextView) list.findViewById(R.id.message_text);

            infos[1] += ", ";
            author.setText(infos[1]);
            time.setText(infos[2]);
            text.setText(infos[3]);
        }

        return list;
    }
}
