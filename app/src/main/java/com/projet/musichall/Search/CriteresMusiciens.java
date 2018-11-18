package com.projet.musichall.Search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;

import java.util.Date;

public class CriteresMusiciens extends BaseActivity {
    RadioGroup instruments;
    RadioGroup niveau;
    RadioGroup motivation;
    TextView indicteur_instruments;
    TextView indicteur_niveau;
    TextView indicteur_motivation;
    TextView indicteur_localisation;
    SeekBar valeurDistance;
    Button lancerRecherche;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_criteres_musiciens);

        instruments = (RadioGroup)findViewById(R.id.instruments);
        niveau = (RadioGroup)findViewById(R.id.niveau);
        motivation = (RadioGroup)findViewById(R.id.motivation);
        indicteur_instruments = findViewById(R.id.indicateur_instrument);
        indicteur_niveau = findViewById(R.id.indicateur_niveau);
        indicteur_motivation = findViewById(R.id.indicateur_motivation);
        indicteur_localisation = findViewById(R.id.indicateur_localisation);
        valeurDistance = findViewById(R.id.valeurDistance);
        lancerRecherche = findViewById(R.id.lancerRecherche);








    }

}
