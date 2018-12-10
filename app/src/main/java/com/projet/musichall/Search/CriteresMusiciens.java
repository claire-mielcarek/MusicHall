package com.projet.musichall.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.projet.musichall.discussion.Chat;
import com.projet.musichall.discussion.ProfilDiscussion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Result;


public class CriteresMusiciens extends BaseActivity/* implements AdapterView.OnItemSelectedListener */{
    private static final int Max_DISTANCE = 500;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;
    private boolean first = true;
    private int numbre_radio = 0;
    private boolean[] first_entries = new boolean[]{true, true, true, true, true, true, true};  // avoid bad modifications on firebase
    //TextView indicateur_instruments;
    //TextView indicateur_niveau;
    //TextView indicateur_localisation;
    TextView affichageDistance;
    RadioGroup motivation;
    SeekBar valeurDistance;
    Spinner spinnerInstruments;
    Spinner spinnerEcoute;
    Spinner spinnerJoue;
    Spinner spinnerNiveau;
    EditText search_name;
    Button lancerRecherche;

    //Array list
    ArrayList<String> listItemsNoms;
    ArrayList<String> listItemsDates;
    ArrayList<String> listItemsInfos;
    List<String> liste_temp;
    ResultPresentation adapter;

    Context context;
    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_criteres_musiciens);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // init list view
        listView = (ListView) findViewById(R.id.listView);
        context = this;

        // connexion a la base de donnee
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        // initialisation des widgets
        spinnerInstruments = findViewById(R.id.instruments);
        liste_temp = ToList(getResources().getStringArray(R.array.instruments));
        liste_temp.add(0, "Tout");
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_temp);    //createFromResource(this, R.array.instruments, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInstruments.setAdapter(adapter1);
        spinnerInstruments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = String.valueOf(parent.getItemAtPosition(position));
                if (!first_entries[0]) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("instru").setValue(val);
                    }
                }
                first_entries[0] = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spinnerInstruments.setOnItemSelectedListener(this);

        spinnerEcoute = findViewById(R.id.musiqueJouee);
        liste_temp = ToList(getResources().getStringArray(R.array.genres));
        liste_temp.add(0, "Tout");
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_temp);     // ArrayAdapter.createFromResource(this, R.array.genres, android.R.layout.simple_spinner_item );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEcoute.setAdapter(adapter2);
        spinnerEcoute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = String.valueOf(parent.getItemAtPosition(position));
                if (!first_entries[1]) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("musique_ecoutee").setValue(val);
                    }
                }
                first_entries[1] = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spinnerEcoute.setOnItemSelectedListener(this);

        spinnerJoue = findViewById(R.id.musiqueEcoutee);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_temp);        //ArrayAdapter.createFromResource(this, R.array.genres, android.R.layout.simple_spinner_item );
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJoue.setAdapter(adapter3);
        spinnerJoue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = String.valueOf(parent.getItemAtPosition(position));
                if (!first_entries[2]) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("musique_jouee").setValue(val);
                    }
                }
                first_entries[2] = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spinnerJoue.setOnItemSelectedListener(this);

        spinnerNiveau = findViewById(R.id.niveau);
        liste_temp = ToList(getResources().getStringArray(R.array.niveaux));
        liste_temp.add(0, "Tout");
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_temp);    // ArrayAdapter.createFromResource(this, R.array.niveaux, android.R.layout.simple_spinner_item );
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNiveau.setAdapter(adapter4);
        spinnerNiveau.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = String.valueOf(parent.getItemAtPosition(position));
                if (!first_entries[3]) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("niveau").setValue(val);
                    }
                }
                first_entries[3] = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //spinnerNiveau.setOnItemSelectedListener(this);


        motivation = (RadioGroup)findViewById(R.id.motivation);
        //indicateur_instruments = findViewById(R.id.indicateur_instrument);
        //indicateur_niveau = findViewById(R.id.indicateur_niveau);
        affichageDistance = findViewById(R.id.affichageDistance);
        valeurDistance = findViewById(R.id.valeurDistance);
        search_name = findViewById(R.id.search_name);
        //lancerRecherche = findViewById(R.id.lancerRecherche);

        // recuperation des valeurs de recherche dans la bdd a chaque changement
        valeurDistance.setMax(Max_DISTANCE);
        valeurDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String resultat = String.valueOf(progress)+" km";
                affichageDistance.setText(resultat);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (user != null) {
                    database.child("Users").child(user.getUid()).child("info_recherche").child("distance").setValue(seekBar.getProgress());
                }
            }
        });

        motivation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String select = checkedId == R.id.amateur?"Amateur":"Professionel";
                Log.d("Check Radio Group", first_entries[5]+"");
                if (!first_entries[5] && numbre_radio > 3) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("motivation").setValue(select);
                    }
                }
                numbre_radio = numbre_radio+1;
                first_entries[5] = false;
            }
        });

        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (!first_entries[6]) {
                   **/if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("nom").setValue(s.toString());
                    }
                /*}
                first_entries[6] = false;*/
            }
        });

        /*lancerRecherche.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CriteresMusiciens.this, RechercheActivity.class);
                intent.putExtra("Search",1);
                startActivity(intent);
            }
        });*/

        // recuperation des donnees de la bdd a chaque changement dans la base de donnee
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot users, info_current_user;
                if (user != null) {
                    // get first data to search users
                    info_current_user = dataSnapshot.child("Users").child(user.getUid()).child("info_recherche");
                    if (first) {
                        getDataFromFirebase(info_current_user);
                        first = false;
                    }

                    // set new adapter to result
                    listItemsNoms = new ArrayList<>();
                    listItemsDates = new ArrayList<>();
                    listItemsInfos = new ArrayList<>();

                    // now apply search algorithm
                    users = dataSnapshot.child("Users");
                    getResultFromFirebase(users, info_current_user);

                    //make an array of the objects according to a layout design
                    adapter = new ResultPresentation(context, listItemsNoms, listItemsDates, listItemsInfos);                                 //new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listItems);
                    //set the adapter for listview
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){



                String userName = adapter.getNoms().get(position);
                String userFirstName = userName.split(" ")[0];
                String userLastName = userName.split(" ")[1];
                /*parent.getNoms().get.toString();*/

                Intent intent = new Intent(CriteresMusiciens.this, ProfilDiscussion.class);

                intent.putExtra("nom", userLastName);
                intent.putExtra("prenom",userFirstName);

                Toast.makeText(getApplicationContext(), userLastName,Toast.LENGTH_LONG).show();


                startActivity(intent);


            }
        });
    }


    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/

    private void getDataFromFirebase(DataSnapshot current_user){
        if (current_user.child("distance").exists())
            valeurDistance.setProgress(Integer.valueOf(String.valueOf(current_user.child("distance").getValue())));
        motivation.clearCheck();
        if (current_user.child("motivation").getValue() == "Amateur")
            motivation.check(R.id.amateur);
        else motivation.check(R.id.professionel);
        if (current_user.child("instru").exists())
            spinnerInstruments.setSelection(getPositionSpinner(spinnerInstruments, String.valueOf(current_user.child("instru").getValue())));
        if (current_user.child("niveau").exists())
            spinnerNiveau.setSelection(getPositionSpinner(spinnerNiveau, String.valueOf(current_user.child("niveau").getValue())));
        if (current_user.child("musique_ecoutee").exists())
            spinnerEcoute.setSelection(getPositionSpinner(spinnerEcoute, String.valueOf(current_user.child("musique_ecoutee").getValue())));
        if (current_user.child("musique_jouee").exists())
            spinnerJoue.setSelection(getPositionSpinner(spinnerJoue, String.valueOf(current_user.child("musique_jouee").getValue())));
        if (current_user.child("name").exists())
            search_name.setText(String.valueOf(current_user.child("nom").getValue()));
        Log.d("INFO GET DATA", String.valueOf(current_user.child("nom").getValue()));
    }

    private void getResultFromFirebase(DataSnapshot users, DataSnapshot current_user){
        List<String> sort_name = new ArrayList<>();   // sort results by priority
        List<String> sort_all_condition = new ArrayList<>();
        List<String> sort_sub_spinner = new ArrayList<>();
        DataSnapshot one_user;
        String sprenom1, snom1, sville1, smotivation1, sniveau1, resultat;
        boolean memeMusiqueJouee, memeMusiqueEcoutee, memeInstru;
        boolean cond_name, cond_with_spinner, cond_without_spinner;
        String sname, sdistance, smotivation2, sniveau2, smusiqueEcoutee2, smusiqueJouee2, sinstru2;
        Iterator<DataSnapshot> it = users.getChildren().iterator();

        // get search data of current user    // TODO faire recherche avec ville
        sname = String.valueOf(current_user.child("nom").getValue());
        sdistance = String.valueOf(current_user.child("distance").getValue());
        smotivation2 = String.valueOf(current_user.child("motivation").getValue());
        sniveau2 = String.valueOf(current_user.child("niveau").getValue());
        smusiqueEcoutee2 = String.valueOf(current_user.child("musique_ecoutee").getValue());
        smusiqueJouee2 = String.valueOf(current_user.child("musique_jouee").getValue());
        sinstru2 = String.valueOf(current_user.child("instru").getValue());

        while (it.hasNext()) {
            one_user = it.next();

            // get user's data
            sprenom1 = String.valueOf(one_user.child("prenom").getValue());
            snom1 = String.valueOf(one_user.child("nom").getValue());
            sville1 = String.valueOf(one_user.child("ville").getValue());
            smotivation1 = String.valueOf(one_user.child("motivation").getValue());
            sniveau1 = String.valueOf(one_user.child("niveau").getValue());
            memeMusiqueEcoutee = !smusiqueEcoutee2.equals("Tout")? InChildrenValue(one_user.child("genreEcoute"), smusiqueEcoutee2) : true;
            memeInstru = !sinstru2.equals("Tout")? InChildrenValue(one_user.child("instrus"), sinstru2): true;
            memeMusiqueJouee = !smusiqueJouee2.equals("Tout")? InChildrenValue(one_user.child("genreJoue"), smusiqueJouee2): true;

            cond_name = sprenom1.toLowerCase().matches("[a-zA-Z0-9]*"+sname.toLowerCase()+"[a-zA-Z0-9]*") || snom1.toLowerCase().matches("[a-zA-Z0-9]*"+sname.toLowerCase()+"[a-zA-Z0-9]*");
            cond_with_spinner = smotivation1.equals(smotivation2) && (!sniveau2.equals("Tout")? sniveau1.equals(sniveau2):true) && memeInstru && memeMusiqueEcoutee && memeMusiqueJouee;
            cond_without_spinner = smotivation1.equals(smotivation2) && sniveau1.equals(sniveau2);

            // get user's information if match with search name
            Log.d("INFO RECHERCHE", "[a-zA-Z0-9 *]"+sname.toLowerCase()+"[a-zA-Z0-9 *] "+" : "+sprenom1.toLowerCase()+" "+snom1.toLowerCase()+" : "+" [a-zA-Z0-9 *]"+sname.toLowerCase()+"[a-zA-Z0-9 *]");

            if (cond_with_spinner && cond_name) {
                resultat = sprenom1 + " " + snom1;
                sort_name.add(resultat);
                resultat = "Membre depuis le " + String.valueOf(one_user.child("dateMembre").getValue());
                sort_name.add(resultat);
                resultat = smotivation1;
                resultat += " de niveau " + sniveau1;
                resultat += " habitant la ville de " + sville1;
                sort_name.add(resultat);
            }

            /*if (cond_name) {
                resultat = sprenom1 + " " + snom1;
                sort_name.add(resultat);
                resultat = "Membre depuis le " + String.valueOf(one_user.child("dateMembre").getValue());
                sort_name.add(resultat);
                resultat = smotivation1;
                resultat += " de niveau " + sniveau1;
                resultat += " habitant la ville de " + sville1;
                sort_name.add(resultat);
            } else {

                // get user's information if match with all search conditions
                if (cond_with_spinner) {
                    resultat = sprenom1 + " " + snom1;
                    sort_all_condition.add(resultat);
                    resultat = "Membre depuis le " + String.valueOf(one_user.child("dateMembre").getValue());
                    sort_all_condition.add(resultat);
                    resultat = smotivation1;
                    resultat += " de niveau " + sniveau1;
                    resultat += " habitant la ville de " + sville1;
                    sort_all_condition.add(resultat);
                } else {

                    // get user's information if match with 'motivation' and 'niveau'
                    if (cond_without_spinner) {
                        resultat = sprenom1 + " " + snom1;
                        sort_sub_spinner.add(resultat);
                        resultat = "Membre depuis le " + String.valueOf(one_user.child("dateMembre").getValue());
                        sort_sub_spinner.add(resultat);
                        resultat = smotivation1;
                        resultat += " de niveau " + sniveau1;
                        resultat += " habitant la ville de " + sville1;
                        sort_sub_spinner.add(resultat);
                    }
                }
            }*/
        }

        Log.d("INFO RECHERCHE", "Taille name : "+sort_name.size());
        Log.d("INFO RECHERCHE", "Taille all condition: "+sort_all_condition.size());
        Log.d("INFO RECHERCHE", "Taille without spinner: "+sort_sub_spinner.size());

        // get results by type
        for (int i = 0; i<sort_name.size(); i+=3) {
            listItemsNoms.add(sort_name.get(i));
            listItemsDates.add(sort_name.get(i+1));
            listItemsInfos.add(sort_name.get(i+2));
        }

        /*for (int i = 0; i<sort_all_condition.size(); i+=3) {
            listItemsNoms.add(sort_all_condition.get(i));
            listItemsDates.add(sort_all_condition.get(i+1));
            listItemsInfos.add(sort_all_condition.get(i+2));
        }

        for (int i = 0; i < sort_sub_spinner.size(); i+=3) {
            listItemsNoms.add(sort_sub_spinner.get(i));
            listItemsDates.add(sort_sub_spinner.get(i+1));
            listItemsInfos.add(sort_sub_spinner.get(i+2));
        }*/
    }

    // usesull function
    private boolean InChildrenValue(DataSnapshot listvalue, String value){
        Iterator<DataSnapshot> it = listvalue.getChildren().iterator();
        DataSnapshot ref;

        while (it.hasNext()) {
            ref = it.next();
            if (value.equals(ref.getValue()))
                return true;
        }

        return false;
    }

    private int getPositionSpinner(Spinner spin, String value){
        for (int i=0;i<spin.getCount();i++){
            if (spin.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                return i;
            }
        }

        return 0;
    }

    private List<String> ToList(String [] tab){
        List<String> resultat = new ArrayList<>();

        for (int i = 0; i<tab.length; i++){
            resultat.add(tab[i]);
        }

        return resultat;
    }
}
