package com.projet.musichall.discussion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
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
    private FirebaseUser user;
    private ListView listView;
    private FloatingActionButton boutonRecherche;
    private DatabaseReference mDatabase;

    // Array list for user info
    /*private ArrayAdapter adapter;
    private ArrayList<String> listItemsNoms;
    private ArrayList<String> listItemsDates;
    private ArrayList<String> listItemsInfos;*/

    private boolean first = true;
    //items to show in listview
    private String nomDiscussion;
    private String nomSender;
    private String prenomSender;
    private List<String> nameDiscussion;
    private String TAG;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.you_have_to_be_logged);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        context = this;

        if (user != null) {
            setContentView(R.layout.activity_discussion);
            ActionBar myBar = getMyActionBar();
            myBar.setTitle(R.string.discussions);
            listView = (ListView)findViewById(R.id.liste_de_discussion);
            boutonRecherche = (FloatingActionButton) findViewById(R.id.bouton_recherche);

            boutonRecherche.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (user != null) {
                    Intent intent = new Intent(DiscussionActivity.this, CriteresMusiciens.class);
                    startActivity(intent);
                    finish();
                }
            });


            //Fonction pour initialiser la list de recherche
            init();

            //Methode lorsque l'on clique sur un item recherche
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String userName = adapterView.getItemAtPosition(position).toString();
                    String userFirstName = userName.split(" ")[0];
                    String userLastName = userName.split(" ")[1];
                    Intent intent = new Intent(DiscussionActivity.this, Chat.class);
                    intent.putExtra("nom", userLastName);
                    intent.putExtra("prenom", userFirstName);
                    startActivity(intent);
                }
            });
        }else{
            Log.d("TEST", "DISCUSSION!");

        }


    }

    public void init(){
        //recuperer data base reference
        //Mettre les éléments dont j'ai besoin de récupérer (Utilisateur avec une discussions avec l'utilisateur courant)
        ///child puis getvalue

        // Uid, Nom, prénom, photo, ville, instrument
        mDatabase.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                String userId = user.getUid();

                DataSnapshot ref;
                DataSnapshot ref2;

                //Snapshot pour obtenir tous les users
                DataSnapshot userSnapshot = dataSnapshot.child("Users");

                //Snapshot pour obtenir les conversations de l'utilisateur
                DataSnapshot discussionOfUser = dataSnapshot.child("conversation");
                //DataSnapshot discussionOfUser = dataSnapshot.child("conversation").getKey()

                //TODO revoir la taille alloué pour les strings
                nameDiscussion = new ArrayList<>();
                nomSender = String.valueOf(userSnapshot.child(user.getUid()).child("nom").getValue());
                prenomSender = String.valueOf(userSnapshot.child(user.getUid()).child("prenom").getValue());


                // On récupère le nom des interlocuteur avec qui le user courant parle à l'aide de la clef de la discussion
                //Sous forme PrenomSender1_NomSender1-PrenomReceveur1_NomReceveur2
                for (DataSnapshot discussion : discussionOfUser.getChildren()) {
                    if (discussion.getKey().split("-")[0].equals(prenomSender + ("_") + nomSender)) {
                        nameDiscussion.add(discussion.getKey().split("-")[1].replace('_',' '));
                        Log.d("nom de l'interlocuteur", nameDiscussion.get(nameDiscussion.size()-1));
                    }
                    if(discussion.getKey().split("-")[1].equals(prenomSender + ("_") + nomSender)){
                        nameDiscussion.add(discussion.getKey().split("-")[0].replace('_',' '));
                    }
                }

                // Initialisation de la string contenant les informations des users (avec une discussion)
                //userWithDiscussion = new ArrayList<>();
                //int count = 0;
                //Testons maintenant les noms des interlocuteurs connu de l'utilisateur avec la table des users pour récupérer leurs infos
                //Tant qu'il y a encore des noms qui n'ont pas été vérifiés on test
                //while (finNameDiscussion != 0){
                    /*for (DataSnapshot user : userSnapshot.getChildren()) {
                        if ((String.valueOf(user.child("prenom").getValue()) + "_" + String.valueOf(user.child("nom").getValue()))
                                .equals(nameDiscussion.get(count))) {
                            //Log.d("son nom a été teste", String.valueOf(user.child("prenom").getValue()));
                            userWithDiscussion.add(String.valueOf(user.child("prenom").getValue()) + " " + String.valueOf(user.child("nom").getValue()));
                            count ++;
                            //TODO Voir les informations qu'on veut mettre plus tard
                            //userWithDiscussion[count] += "\n Habite à "+String.valueOf(user.child("ville").getValue());
                            //Log.d("On a récuperé ", userWithDiscussion[count]);
                            //finNameDiscussion = finNameDiscussion-1;

                        }
                    }*/
                //}

                Log.d("on a dans la liste", String.valueOf(nameDiscussion));

                //make an array of the objects according to a layout design
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1, nameDiscussion);
                //set the adapter for listview
                listView.setAdapter(adapter);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });


    }
}

