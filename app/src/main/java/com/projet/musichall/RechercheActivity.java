package com.projet.musichall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class RechercheActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;

    //items to show in listview

    String[] elementsRecherche;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        listView = (ListView)findViewById(R.id.listView);
        editText = (EditText)findViewById(R.id.recherche);

        //Fonction pour initaliser la list de recherche
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
