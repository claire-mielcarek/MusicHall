package com.projet.musichall.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class CriteresGroupe extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseUser user;
    private DatabaseReference database;
    private boolean first = true;
    private boolean ok = false;

    /*RadioGroup motivation;
    Spinner spinnerInstruments;
    Spinner spinnerJoue;
    Spinner spinnerNiveau;*/
    EditText search_name;

    //Array list
    ArrayList<String> listItemsNoms;
    ArrayList<String> listItemsDates;
    ArrayList<String> listItemsInfos;
    //List<String> liste_temp;
    ResultPresentation adapter;

    Context context;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_criteres_groupe);
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
        //spinnerInstruments = findViewById(R.id.instruments);
        //liste_temp = ToList(getResources().getStringArray(R.array.instruments));
        //liste_temp.add(0, "Tout");
        //final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_temp);    //createFromResource(this, R.array.instruments, android.R.layout.simple_spinner_item);
        //adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /*spinnerInstruments.setAdapter(adapter1);
        spinnerInstruments.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = String.valueOf(parent.getItemAtPosition(position));
                if (ok) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("groupe").child("instru").setValue(val);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*spinnerJoue = findViewById(R.id.musiqueJouee);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_temp);        //ArrayAdapter.createFromResource(this, R.array.genres, android.R.layout.simple_spinner_item );
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJoue.setAdapter(adapter3);
        spinnerJoue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = String.valueOf(parent.getItemAtPosition(position));
                if (ok) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("groupe").child("musique_jouee").setValue(val);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*spinnerNiveau = findViewById(R.id.niveau);
        liste_temp = ToList(getResources().getStringArray(R.array.niveaux));
        liste_temp.add(0, "Tout");
        final ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste_temp);     // ArrayAdapter.createFromResource(this, R.array.genres, android.R.layout.simple_spinner_item );
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNiveau.setAdapter(adapter4);
        spinnerNiveau.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String val = String.valueOf(parent.getItemAtPosition(position));
                if (ok) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("groupe").child("niveau").setValue(val);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



        //motivation = (RadioGroup) findViewById(R.id.motivation);
        search_name = findViewById(R.id.search_name);

        /*motivation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String select = checkedId == R.id.amateur?"Amateur":"Professionel";
                if (ok) {
                    if (user != null) {
                        database.child("Users").child(user.getUid()).child("info_recherche").child("groupe").child("motivation").setValue(select);
                    }
                }
            }
        });*/

        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                 if (user != null) {
                    database.child("Users").child(user.getUid()).child("info_recherche").child("groupe").child("nom").setValue(s.toString());
                }
            }
        });


        // recuperation des donnees de la bdd a chaque changement
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot groups, info_current_user;
                if (user != null) {
                    // get first data to search users
                    info_current_user = dataSnapshot.child("Users").child(user.getUid()).child("info_recherche").child("groupe");
                    if (first) {
                        getDataFromFirebase(info_current_user);
                        first = false;
                    }

                    // set new adapter to result
                    listItemsNoms = new ArrayList<>();
                    listItemsDates = new ArrayList<>();
                    listItemsInfos = new ArrayList<>();

                    // now apply search algorithm
                    groups = dataSnapshot.child("Groupes");
                    getResultFromFirebase(groups, info_current_user);

                    // make an array of the objects according to a layout design
                    adapter = new ResultPresentation(context, listItemsNoms, listItemsDates, listItemsInfos, null);    //new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, listItems);
                    // set the adapter for listview
                    listView.setAdapter(adapter);

                    // allow to widget's listeners to modify firebase data
                    ok = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataFromFirebase(DataSnapshot current_user){
        /*motivation.clearCheck();
        if (String.valueOf(current_user.child("motivation").getValue()).equals("Amateur"))
            motivation.check(R.id.amateur);
        else motivation.check(R.id.professionel);
        if (current_user.child("instru").exists())
            spinnerInstruments.setSelection(getPositionSpinner(spinnerInstruments, String.valueOf(current_user.child("instru").getValue())));
        if (current_user.child("niveau").exists())
            spinnerNiveau.setSelection(getPositionSpinner(spinnerNiveau, String.valueOf(current_user.child("niveau").getValue())));
        if (current_user.child("genre_jouee").exists())
            spinnerJoue.setSelection(getPositionSpinner(spinnerJoue, String.valueOf(current_user.child("genre_jouee").getValue())));*/
        if (current_user.child("nom").exists())
            search_name.setText(String.valueOf(current_user.child("nom").getValue()));
    }

    private void getResultFromFirebase(DataSnapshot groups, DataSnapshot current_user){
        List<String> sort_name = new ArrayList<>();   // sort results by priority
        DataSnapshot one_group;
        Iterator<DataSnapshot> it = groups.getChildren().iterator();
        String snom1, smotivation1, sniveau1, sstyle1, resultat;
        //boolean memeMusiqueJouee, memeInstru;
        boolean cond_name/*, cond_with_spinner, cond_without_spinner*/;
        String sname/*, smotivation2, sniveau2, sgenreJouee2, sinstru2*/;

        // get search data of current user    // TODO faire recherche avec ville
        sname = String.valueOf(current_user.child("nom").getValue());
        /*smotivation2 = String.valueOf(current_user.child("motivation").getValue());
        sniveau2 = String.valueOf(current_user.child("niveau").getValue());
        sgenreJouee2 = String.valueOf(current_user.child("genre_jouee").getValue());
        sinstru2 = String.valueOf(current_user.child("instru").getValue());*/

        while (it.hasNext()) {
            one_group = it.next();

            // get user's data
            snom1 = String.valueOf(one_group.child("nom").getValue());
            //sville1 = String.valueOf(one_group.child("ville").getValue());
            smotivation1 = String.valueOf(one_group.child("motivation").getValue());
            sniveau1 = String.valueOf(one_group.child("niveau").getValue());
            sstyle1 = String.valueOf(one_group.child("style").getValue());
            //memeInstru = !sinstru2.equals("Tout")? InChildrenValue(one_group.child("instrus"), sinstru2): true;
            //memeMusiqueJouee = !sgenreJouee2.equals("Tout")? InChildrenValue(one_group.child("genreJoue"), sgenreJouee2): true;

            cond_name = snom1.toLowerCase().matches(sname.toLowerCase()+"[a-zA-Z0-9]*");
            /*cond_with_spinner = smotivation1.equals(smotivation2) && (!sniveau2.equals("Tout")? sniveau1.equals(sniveau2):true) && memeInstru && memeMusiqueJouee*/;
            //cond_without_spinner = smotivation1.equals(smotivation2) && sniveau1.equals(sniveau2);

            // get user's information if match with search name
            if (cond_name && !sname.isEmpty()) {
                resultat = snom1;
                sort_name.add(resultat);
                resultat = "Créer le " + String.valueOf(one_group.child("dateMembre").getValue());
                sort_name.add(resultat);
                resultat = "Groupe " + smotivation1;
                resultat += " de " + sstyle1;
                resultat += " et de niveau " + sniveau1;
                sort_name.add(resultat);
                Log.d("INFO RECHERCHE", "Nom : "+snom1);
            }
        }


        // get results by type
        for (int i = 0; i<sort_name.size(); i+=3) {
            listItemsNoms.add(sort_name.get(i));
            listItemsDates.add(sort_name.get(i+1));
            listItemsInfos.add(sort_name.get(i+2));
        }


        Log.d("INFO RECHERCHE GROUPE", "Taille name : "+sort_name.size());
        Log.d("INFO RECHERCHE GROUPE", "Taille name : "+listItemsNoms.size());
        Log.d("INFO RECHERCHE GROUPE", "Taille dates: "+listItemsDates.size());
        Log.d("INFO RECHERCHE GROUPE", "Taille infos: "+listItemsInfos.size());
    }

    // usesull function
    /*private boolean InChildrenValue(DataSnapshot listvalue, String value){
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
    }*/

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*private List<String> ToList(String [] tab){
        List<String> resultat = new ArrayList<>();

        for (int i = 0; i<tab.length; i++){
            resultat.add(tab[i]);
        }

        return resultat;
    }*/
}


