package com.projet.musichall.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;

import java.util.Date;

public class CriteresMusiciens extends BaseActivity implements AdapterView.OnItemSelectedListener {

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

        Spinner spinnerInstruments = findViewById(R.id.instruments);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.instruments, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInstruments.setAdapter(adapter);
        spinnerInstruments.setOnItemSelectedListener(this);

        Spinner spinnerEcoute = findViewById(R.id.musiqueJouee);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.genres, android.R.layout.simple_spinner_item );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEcoute.setAdapter(adapter2);
        spinnerEcoute.setOnItemSelectedListener(this);

        Spinner spinnerJoue = findViewById(R.id.musiqueEcoutee);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.genres, android.R.layout.simple_spinner_item );
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJoue.setAdapter(adapter3);
        spinnerJoue.setOnItemSelectedListener(this);

        Spinner spinnerNiveau = findViewById(R.id.niveau);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.niveaux, android.R.layout.simple_spinner_item );
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNiveau.setAdapter(adapter4);
        spinnerNiveau.setOnItemSelectedListener(this);



        motivation = (RadioGroup)findViewById(R.id.motivation);
        indicteur_instruments = findViewById(R.id.indicateur_instrument);
        indicteur_niveau = findViewById(R.id.indicateur_niveau);
        indicteur_motivation = findViewById(R.id.indicateur_motivation);
        indicteur_localisation = findViewById(R.id.indicateur_localisation);
        valeurDistance = findViewById(R.id.valeurDistance);
        lancerRecherche = findViewById(R.id.lancerRecherche);

        lancerRecherche.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CriteresMusiciens.this, RechercheActivity.class);
                intent.putExtra("Search",1);
                startActivity(intent);
            }
        });









    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
