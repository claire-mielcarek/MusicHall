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

    String nomDiscussion;

    String nomSender;
    String prenomSender;

    String[] nameDiscussion;

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
                String userFirstName = userName.split(" ")[0];
                String userLastName = userName.split(" ")[1];
                Intent intent = new Intent(DiscussionActivity.this, Chat.class);
                intent.putExtra("nom",userLastName);
                intent.putExtra("prenom",userFirstName);
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

                int counter = 0;
                int count = 0;
                String userId = dataSnapshot.child("Users").child(user.getUid()).getKey();

                DataSnapshot ref;
                DataSnapshot ref2;

                //Snapshot pour obtenir tous les users
                DataSnapshot userSnapshot = dataSnapshot.child("Users");

                //Snapshot pour obtenir les conversations de l'utilisateur
                DataSnapshot discussionOfUser = dataSnapshot.child("conversation");
                //DataSnapshot discussionOfUser = dataSnapshot.child("conversation").getKey()



                //TODO revoir la taille alloué pour les strings
                nameDiscussion = new String[(int) discussionOfUser.getChildrenCount()*50];


                // On récupère le prénom et le nom du user courant
                for (DataSnapshot user : userSnapshot.getChildren()) {
                    //ref = it.next();

                    if (userId.equals(String.valueOf(user.getKey()))) {
                        nomSender = String.valueOf(user.child("nom").getValue());
                        prenomSender = String.valueOf(user.child("prenom").getValue());
                        //Log.d("utilisateur courant est", prenomSender + ("_") + nomSender);

                    }
                }


                // On récupère le nom des interlocuteur avec qui le user courant parle à l'aide de la clef
                for (DataSnapshot discussion : discussionOfUser.getChildren()) {

                    if (discussion.getKey().split("-")[0].equals(prenomSender + ("_") + nomSender)) {
                        nameDiscussion[count] = discussion.getKey().split("-")[1];
                        Log.d("nom de l'interlocuteur", nameDiscussion[count]);
                        count = count + 1;
                    }
                    if(discussion.getKey().split("-")[1].equals(prenomSender + ("_") + nomSender)){
                        nameDiscussion[count] = discussion.getKey().split("-")[0];
                        count = count + 1;
                    }


                }
                //Initialisation de la string contenant les informations des users(avec une discussion)
                userWithDiscussion = new String[count];
                int finNameDiscussion = count;
                count = 0;
                //Testons maintenant les noms des interlocuteurs connu de l'utilisateur avec la table des users pour récupérer leurs infos
                //Tant qu'il y a encore des noms qui n'ont pas été vérifiés on test
                while (finNameDiscussion != 0){
                    for (DataSnapshot user : userSnapshot.getChildren()) {
                        if ((String.valueOf(user.child("prenom").getValue()) + "_" + String.valueOf(user.child("nom").getValue()))
                                .equals(nameDiscussion[count])) {
                            Log.d("son nom a été teste", String.valueOf(user.child("prenom").getValue()));
                            userWithDiscussion[count] = String.valueOf(user.child("prenom").getValue());
                            userWithDiscussion[count] += " " + String.valueOf(user.child("nom").getValue());
                            //TODO Voir les informations qu'on veut mettre plus tard

                            //Log.d("On a récuperé ", userWithDiscussion[count]);

                            count = count + 1;
                            finNameDiscussion = finNameDiscussion-1;

                        }
                    }
                }



                // Pour chacun des noms d'interlocuteur de l'utilisateur inscrit dans son onglet discussion
                // Vérifier si le nom à afficher correspond



                //  (String.valueOf(ref2.child("interlocuteur").getValue()))) {
                // if(discussionTest.equals(discussionTest2)) {
                //if(String.valueOf(ref.child("nom").getValue()).equals(String.valueOf(mDatabase.child("Users")
                // .child(user.getUid()).child("conversation").getKey()))){

                //userWithDiscussion[counter] = String.valueOf(user.child("prenom").getValue());
                //userWithDiscussion[counter] += " " + String.valueOf(user.child("nom").getValue());
                //userWithDiscussion[counter] += " " + String.valueOf(ref2.getKey());

                //userWithDiscussion[counter] += " \n Ville : " + String.valueOf(ref.child("ville").getValue());
                //userWithDiscussion[counter] += " \n Dernier message : "; //+ String.valueOf(ref.child("message").getValue());
                //userWithDiscussion[counter] += " " + String.valueOf(ref.child("instrus").getValue());

                //}
                //}



                listItems = new ArrayList<>(Arrays.asList(userWithDiscussion));
                Log.d("on a dans la liste", String.valueOf(listItems));

                //make an array of the objects according to a layout design
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, android.R.id.text1, userWithDiscussion);
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

