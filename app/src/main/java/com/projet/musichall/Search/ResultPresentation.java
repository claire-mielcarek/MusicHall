package com.projet.musichall.Search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.projet.musichall.R;

import java.util.List;



public class ResultPresentation extends BaseAdapter {
    private List<String> uids;
    private List<String> noms;
    private List<String> dates_membre;
    private List<String> infos;
    //private List<Bitmap> avatars;
    private Context context;



    public ResultPresentation(Context context, List<String> noms, List<String> dates_membre, List<String> infos, List<String> ids){
        this.noms = noms;
        this.dates_membre = dates_membre;
        this.infos = infos;
        uids = ids;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //if (convertView == null){
            list = new View(context);

            // get layout from mobile.xml
            list = inflater.inflate(R.layout.result_search, null);

            if (list != null) {
                // set informations
                //ImageView avatar = (ImageView)  list.findViewById(R.id.photo);
                TextView nom_complet = (TextView) list.findViewById(R.id.nomc);
                TextView date_membre = (TextView) list.findViewById(R.id.datem);
                TextView info = (TextView) list.findViewById(R.id.infos);

                //avatar.setImageBitmap(avatars.get(position%avatars.size()));
                nom_complet.setText(noms.get(position));
                if(dates_membre.size() == 0){ // pour un groupe, on n'a pas de date
                    date_membre.setVisibility(View.INVISIBLE);
                }
                else {
                    date_membre.setText(dates_membre.get(position));
                }
                if(infos.size() == 0){
                    info.setVisibility(View.INVISIBLE);
                }
                else {
                    info.setText(infos.get(position));
                }
            }
        /*}else{
            list = convertView;
        }*/

        return list;
    }

    public List<String> getNoms() {
        return noms;
    }

    public List<String> getDates() {
        return dates_membre;
    }

    public List<String> getInfos() {
        return infos;
    }

    public List<String> getIds(){
        return uids;
    }

    @Override
    public int getCount() {
        return noms.size();
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
