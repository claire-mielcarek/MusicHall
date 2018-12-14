package com.projet.musichall.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.projet.musichall.R;




public class MyStringArrayAddapter extends ArrayAdapter<String> {
    private final Context context;
    private String[] valeur;


    public MyStringArrayAddapter(@NonNull Context context, @NonNull String[] objects) {
        super(context, -1, objects);
        this.context = context;
        this.valeur = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.simple_list_view, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text_list_view);
        textView.setText(valeur[position]);

        return rowView;
    }

}
