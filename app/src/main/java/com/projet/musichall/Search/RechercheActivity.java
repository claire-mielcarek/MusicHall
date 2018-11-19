package com.projet.musichall.Search;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;

import java.util.ArrayList;
import java.util.Arrays;

public class RechercheActivity extends BaseActivity {

    ListView listView;
    EditText editText;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;

    //items to show in listview

    String[] elementsRecherche;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_listview);

        listView = (ListView)findViewById(R.id.listView);
        editText = (EditText)findViewById(R.id.recherche);

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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef= database.getReference("message");

        myRef.setValue("instrus");

        // Lire la database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        elementsRecherche = new String[]{"Claire","Bastien","Arthur"};

        listItems = new ArrayList<>(Arrays.asList(elementsRecherche));
        //make an array of the objects according to a layout design
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, elementsRecherche);
        //set the adapter for listview
        listView.setAdapter(adapter);
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
