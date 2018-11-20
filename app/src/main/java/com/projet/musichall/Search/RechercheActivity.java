package com.projet.musichall.Search;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;


public class RechercheActivity extends BaseActivity {

    ListView listView;
    EditText editText;
    //Firebase variable
    //Mettre plus tard pour la recherche auto
    //private FirebaseAuth rech;
    private DatabaseReference mDatabase;

    //Array list
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    //items to show in listview

    String[] elementsRecherche;



    private String TAG;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_listview);



        mDatabase= FirebaseDatabase.getInstance().getReference();
        listView = (ListView)findViewById(R.id.listView);
        editText = (EditText)findViewById(R.id.recherche);
        context = this;



        //Fonction pour initialiser la list de recherche
        init();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    //reset listview
                    init();
                }
                else{
                    //perform search
                    searchItem(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //Méthode lorsque l'on clique sur un item recherché

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                //ListView Clicked item index
                int itemPosition = position+1;
                //ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                //Show Toast
                Toast.makeText(getApplicationContext(),
                        "Position de l'item :" +itemPosition+" Element cliqué : " +itemValue, Toast.LENGTH_LONG)
                        .show();
            }
        });


    }

    public void init(){
        //recuperer data base reference
        //Mettre les éléments dont j'ai besoin de récupérer (instruments)
        //Utiliser un listener propre à firebase pour récupérer les données dans ma classe
        // (Les lignes au dessus et en dessous sont liés)
        ///child puis getvalue en utilisant le read from database le 5

        // Uid, Nom, prénom, photo, date membre, ville, niveau, motivation
        final int valeurRecherche = getIntent().getIntExtra("Search",0);
        mDatabase.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int counter=0;
                DataSnapshot ref;

                if(valeurRecherche==1) {
                    DataSnapshot userSnapshot = dataSnapshot.child("Users");
                    Iterator<DataSnapshot> it = userSnapshot.getChildren().iterator();
                    elementsRecherche = new String[(int) userSnapshot.getChildrenCount()];
                    while (it.hasNext()) {
                        ref = it.next();
                        elementsRecherche[counter] = String.valueOf(ref.child("prenom").getValue());
                        elementsRecherche[counter] += " " + String.valueOf(ref.child("nom").getValue());
                        elementsRecherche[counter] += " " + String.valueOf(ref.child("dateMembre").getValue());
                        elementsRecherche[counter] += " " + String.valueOf(ref.child("ville").getValue());
                        elementsRecherche[counter] += " " + String.valueOf(ref.child("motivation").getValue());
                        elementsRecherche[counter] += " " + String.valueOf(ref.child("niveau").getValue());


                        counter = counter + 1;
                    }
                }
                else {
                    DataSnapshot userSnapshot = dataSnapshot.child("Groupes");
                    Iterator<DataSnapshot> it = userSnapshot.getChildren().iterator();
                    elementsRecherche = new String[(int) userSnapshot.getChildrenCount()];
                    while (it.hasNext()) {
                        ref = it.next();
                        elementsRecherche[counter] = String.valueOf(ref.child("nom").getValue());



                        counter = counter + 1;
                    }

                }

                listItems = new ArrayList<>(Arrays.asList(elementsRecherche));
                //make an array of the objects according to a layout design
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, elementsRecherche);
                //set the adapter for listview
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //elementsRecherche = new String[]{"Claire","Bastien","Arthur"};





    }

    public void searchItem(String textToSearch){
        for(String item:elementsRecherche){
            if(!item.contains(textToSearch)){
                listItems.remove(item);
            }

        }
        adapter.notifyDataSetChanged();
    }
}
