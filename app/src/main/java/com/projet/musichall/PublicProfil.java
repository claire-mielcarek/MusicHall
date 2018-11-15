package com.projet.musichall;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;




public class PublicProfil extends Fragment {
    private SeekBar seek;
    private TextView niveau;
    private ListView listinstru;
    private ListView genreMusicalJouer;
    private ListView genreMusicalEcouter;
    private MyListAdapter adapterInstru;
    private MyListAdapter adapterJouer;
    private MyListAdapter adapterEcouter;


    public static PublicProfil newInstance() {
        return new PublicProfil();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ArrayList<String> instrus =  new ArrayList<>();
        ArrayList<String> jouer =  new ArrayList<>();
        ArrayList<String> ecouter =  new ArrayList<>();
        instrus.add("Instrument1");  instrus.add("Instrument2"); instrus.add("Instrument3");
        jouer.add("GenreJouer1"); jouer.add("GenreJouer2"); jouer.add("GenreJouer3");
        ecouter.add("GenreEcouter1"); ecouter.add("GenreEcouter2"); ecouter.add("GenreEcouter3");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.public_profil, container, false);

        niveau = rootView.findViewById(R.id.editniveau);

        seek = rootView.findViewById(R.id.editniveauseek);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 0 && progress <= 20){
                    niveau.setText("Débutant");
                }else if (progress >= 21 && progress <= 40){
                    niveau.setText("Intermédiaire");
                }else if (progress >= 41 && progress <= 60){
                    niveau.setText("Avancé");
                }else if (progress >= 61 && progress <= 80){
                    niveau.setText("Expert");
                }else if (progress >= 81 && progress <= 100){
                    niveau.setText("Maitre");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // recupere et rempli les list view
        listinstru = rootView.findViewById(R.id.listinstru);
        genreMusicalJouer = rootView.findViewById(R.id.listjouer);
        genreMusicalEcouter = rootView.findViewById(R.id.listecouter);
        adapterInstru = new MyListAdapter(getContext(), instrus);
        adapterJouer = new MyListAdapter(getContext(), jouer);
        adapterEcouter = new MyListAdapter(getContext(), ecouter);
        listinstru.setAdapter(adapterInstru);
        genreMusicalJouer.setAdapter(adapterJouer);
        genreMusicalEcouter.setAdapter(adapterEcouter);

        listinstru.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

        genreMusicalJouer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

        genreMusicalEcouter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getContext(),
                        "Click ListItem Number " + position, Toast.LENGTH_LONG)
                        .show();
            }
        });

        return rootView;
    }
}
