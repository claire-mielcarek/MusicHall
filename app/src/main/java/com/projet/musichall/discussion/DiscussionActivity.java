package com.projet.musichall.discussion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.projet.musichall.Search.CriteresMusiciens;
import com.projet.musichall.Search.ResultPresentation;
import com.projet.musichall.ShouldLogin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DiscussionActivity extends BaseActivity {

    FirebaseUser user;
    ListView listView;
    FloatingActionButton boutonRecherche;
    //Firebase variable
    //Mettre plus tard pour la recherche auto
    //private FirebaseAuth rech;
    private DatabaseReference mDatabase;

    //Array list
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter adapter;

    ArrayList<String> listItemsNoms;
    ArrayList<String> listItemsDates;
    ArrayList<String> listItemsInfos;

    private boolean first = true;
    //items to show in listview

    String[] userWithDiscussion;



    private String TAG;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);


        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        listView = (ListView)findViewById(R.id.liste_de_discussion);
        boutonRecherche = (FloatingActionButton) findViewById(R.id.bouton_recherche);
        context = this;



        boutonRecherche.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user != null) {
                    Intent intent = new Intent(DiscussionActivity.this, CriteresMusiciens.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(DiscussionActivity.this, ShouldLogin.class);
                    startActivity(intent);
                }
            }
        });


        //Fonction pour initialiser la list de recherche
        init();

        //Méthode lorsque l'on clique sur un item recherché

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){


                String userName = adapterView.getItemAtPosition(position).toString();
                Intent intent = new Intent(DiscussionActivity.this, Chat.class);
                intent.putExtra("nom",userName);
                startActivity(intent);


                /*
                //ListView Clicked item index
                int itemPosition = position+1;
                //ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                //Show Toast
                Toast.makeText(getApplicationContext(),
                        "Position de l'item :" +itemPosition+" Element cliqué : " +itemValue, Toast.LENGTH_LONG)
                        .show();
                        */
            }
        });


    }

    public void init(){
        //recuperer data base reference
        //Mettre les éléments dont j'ai besoin de récupérer (Utilisateur avec une discussions avec l'utilisateur courant)
        ///child puis getvalue

        // Uid, Nom, prénom, photo, ville, instrument
        mDatabase.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter=0;
                DataSnapshot ref;

                DataSnapshot userSnapshot = dataSnapshot.child("Users");
                Iterator<DataSnapshot> it = userSnapshot.getChildren().iterator();
                userWithDiscussion = new String[(int) userSnapshot.getChildrenCount()];
                while (it.hasNext()) {
                    ref = it.next();
                    userWithDiscussion[counter] = String.valueOf(ref.child("prenom").getValue());
                    userWithDiscussion[counter] += " " + String.valueOf(ref.child("nom").getValue());
                    userWithDiscussion[counter] += " \n Ville : " + String.valueOf(ref.child("ville").getValue());
                    userWithDiscussion[counter] += " \n Dernier message : "; //+ String.valueOf(ref.child("message").getValue());
                    //userWithDiscussion[counter] += " " + String.valueOf(ref.child("instrus").getValue());
                    counter = counter + 1;
                }


                listItems = new ArrayList<>(Arrays.asList(userWithDiscussion));
                //make an array of the objects according to a layout design
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, userWithDiscussion);
                //set the adapter for listview
                listView.setAdapter(adapter);

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }
}
