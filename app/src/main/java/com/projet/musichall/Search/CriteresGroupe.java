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

    EditText search_name;

    //Array list
    ArrayList<String> listItemsNoms;
    ArrayList<String> listItemsDates;
    ArrayList<String> listItemsInfos;
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
        search_name = findViewById(R.id.search_name);

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
        if (current_user.child("nom").exists())
            search_name.setText(String.valueOf(current_user.child("nom").getValue()));
    }

    private void getResultFromFirebase(DataSnapshot groups, DataSnapshot current_user){
        List<String> sort_name = new ArrayList<>();   // sort results by priority
        DataSnapshot one_group;
        Iterator<DataSnapshot> it = groups.getChildren().iterator();
        String snom1, smotivation1, sniveau1, sstyle1, resultat;
        boolean cond_name;
        String sname;

        // get search data of current user    // TODO faire recherche avec ville
        sname = String.valueOf(current_user.child("nom").getValue());

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


