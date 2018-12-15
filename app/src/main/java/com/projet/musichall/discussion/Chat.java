package com.projet.musichall.discussion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.MainActivity;
import com.projet.musichall.R;
import com.projet.musichall.group.GroupActivity;
import com.projet.musichall.group.SampleFragmentPagerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class Chat extends BaseActivity {

    TextView nom_interlocuteur;
    TextView prenom_interlocuteur;
    ListView listViewMessages;
    EditText messageEnvoyer;


    FloatingActionButton envoyer;

    private DatabaseReference data;
    FirebaseUser user;


    //Array list
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private static int SIGN_IN_REQUEST_CODE = 1;
    //private FirebaseListAdapter<MessageChat> adapter;
    RelativeLayout activity_main;

    String nomUser;
    String nomInterlocuteur;
    String prenomInterlocuteur;
    String[] chat;
    Context context;

    //Pour récupérer la clef du User avec qui la personne parle
    String keyUser2;
    ArrayList testchat;
    String nomSender;
    String prenomSender;

    boolean testDiscussion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_chat);

        listViewMessages = (ListView)findViewById(R.id.list_of_message);
        messageEnvoyer = (EditText) findViewById(R.id.input);
        envoyer = findViewById(R.id.fab);

        activity_main = findViewById(R.id.discussion_chat);
        //Pour récupérer Firebase
        user = FirebaseAuth.getInstance().getCurrentUser();
        data = FirebaseDatabase.getInstance().getReference();
        // Pour récupérer l'information sur la personne avec qui le user parle
        nom_interlocuteur = findViewById(R.id.nom_interlocuteur);
        prenom_interlocuteur = findViewById(R.id.prenom_interlocuteur);

        context = this;

        Intent intent = getIntent();

        // Récupérer les informations de la personne avec qui on communique
        if (intent!=null){
            String str = "";
            if (intent.hasExtra("nom")){
                nomInterlocuteur= intent.getStringExtra("nom");
                nom_interlocuteur.setText(nomInterlocuteur);
            }
            if(intent.hasExtra("prenom")){
                prenomInterlocuteur = intent.getStringExtra("prenom");
                prenom_interlocuteur.setText(prenomInterlocuteur);
            }

        }


        Toast.makeText(this,"Vous êtes en discussion avec "+ prenomInterlocuteur, Toast.LENGTH_SHORT).show();

        //Fin de la récupération



        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                //Récupération de la clef de la personne avec qui l'user parle.
                String userId = dataSnapshot.child("Users").child(user.getUid()).getKey();
                DataSnapshot usersData = dataSnapshot.child("Users");
                for (DataSnapshot user: usersData.getChildren()){

                   //Log.d("nom User" , String.valueOf(user.child("nom").getValue()));
                   //Log.d("nom User" , user.child("nom").getValue());
                   //Log.d("Le nom interlocuteur" , nomInterlocuteur);

                    /*
                    if (nomInterlocuteur.equals(String.valueOf(user.child("nom").getValue()))){
                        keyUser2 = String.valueOf(user.getKey());
                        Log.d("Clef de l'interlocuteur" , keyUser2);
                        }
                        */
                        if(userId.equals(String.valueOf(user.getKey()))){
                        nomSender = String.valueOf(user.child("nom").getValue());
                        prenomSender = String.valueOf(user.child("prenom").getValue());
                        }
                }

                // On récupère les messages liés à la conversation
                int count = 0;
                DataSnapshot discussionData = dataSnapshot.child("conversation");

                for (DataSnapshot discussion : discussionData.getChildren()){
                    Log.d("on arrive là", discussion.getKey());
                    Log.d("et on test avec",prenomInterlocuteur +"_" + nomInterlocuteur + "-" +
                            prenomSender +"_"+ nomSender);
                    if(discussion.getKey().equals(prenomInterlocuteur +"_" + nomInterlocuteur + "-" +
                            prenomSender +"_"+ nomSender) || discussion.getKey().equals((prenomSender +"_" + nomSender + "-" +
                            prenomInterlocuteur +"_"+ nomInterlocuteur))){
                        // On récupère les données seulement de la bonne conversation parmis toutes celles de la table
                        chat = new String[(int)discussion.getChildrenCount()];
                        for(DataSnapshot messageDiscussion : discussion.getChildren()) {
                            chat[count] = String.valueOf(messageDiscussion.child("auteur").getValue());
                            //chat[count] = String.valueOf(messageDiscussion.child("tempsDuMessage").getValue());
                            chat[count] += " \n" + String.valueOf(messageDiscussion.child("message").getValue());
                            Log.d("on a récupére", chat[count]);
                            count = count + 1;
                        }
                    }
                }

                if(chat != null) {
                    listItems = new ArrayList<>(Arrays.asList(chat));
                    Log.d("on a ça dans la liste", String.valueOf(listItems));

                    //make an array of the objects according to a layout design
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                            android.R.layout.simple_list_item_1, android.R.id.text1, chat);
                    //set the adapter for listview

                    listViewMessages.setAdapter(adapter);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DatabaseChange", "Failed to read values.", error.toException());
            }



        });





        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                // Ajout de la notion de conversation de l'user avec la personne recherchée

                //String keyUser = data.child("Users").child(user.getUid()).getKey();

                //La clef de la conversation sera le nom interlocuteur + nom user

                if(data.child("conversation").child(prenomInterlocuteur +"_" + nomInterlocuteur + "-" +
                        prenomSender +"_"+ nomSender).getKey().equals(prenomInterlocuteur +"_" + nomInterlocuteur + "-" +
                        prenomSender +"_"+ nomSender) && data.child("conversation").child(prenomSender +"_" + nomSender + "-" +
                        prenomInterlocuteur +"_"+ nomInterlocuteur).getKey() == null ){
                    DatabaseReference newDiscussion = data.child("conversation").child(prenomInterlocuteur +"_" + nomInterlocuteur + "-" +
                            prenomSender +"_"+ nomSender).push();

                    //+String.valueOf(data.child("Users").child(user.getUid()).child("nom")));
                    //DatabaseReference userName = data.child("Users").child(user.getUid()).child("nom");
                    EditText input = (EditText)findViewById(R.id.input);


                    //Placer les éléments d'un message envoyé
                    newDiscussion.child("message").setValue(input.getText().toString());
                    newDiscussion.child("auteur").setValue(prenomSender + nomSender + " :");
                    newDiscussion.child("tempsDuMessage").setValue(new Date().getTime());

                    //newDiscussion.child("interlocuteur1").setValue(keyUser);
                    //newDiscussion.child("interlocuteur2").setValue(keyUser2);
                    input.setText("");
                }
                else {
                    DatabaseReference newDiscussion = data.child("conversation").child(prenomSender +"_" + nomSender + "-" +
                            prenomInterlocuteur +"_"+ nomInterlocuteur).push();

                    EditText input = (EditText)findViewById(R.id.input);


                    //Placer les éléments d'un message envoyé
                    newDiscussion.child("message").setValue(input.getText().toString());
                    newDiscussion.child("auteur").setValue(prenomSender +" "+ nomSender + " :");
                    newDiscussion.child("tempsDuMessage").setValue(new Date().getTime());

                    //newDiscussion.child("interlocuteur1").setValue(keyUser);
                    //newDiscussion.child("interlocuteur2").setValue(keyUser2);
                    input.setText("");
                }


            }
        });


        displayChatMessage();


    }

    private void displayChatMessage(){


        listViewMessages = (ListView)findViewById(R.id.list_of_message);

    /*
        Query query = FirebaseDatabase.getInstance().getReference().child("conversation").child("message");
        FirebaseListOptions<MessageChat> options = new FirebaseListOptions.Builder<MessageChat>()
                .setQuery(query,MessageChat.class)
                .setLayout(R.layout.discussion_item)
                .build();
        adapter = new FirebaseListAdapter<MessageChat>(options) {
            @Override
            protected void populateView(View v, MessageChat model, int position) {
                TextView messageText, nomSender, messageTime;
                messageText = (TextView)findViewById(R.id.message_text);
                nomSender = (TextView)findViewById(R.id.nom_user);
                messageTime = (TextView)findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                nomSender.setText(model.getNomSender());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));
                /*


*/

/*


            //}
            */
        listViewMessages.setOnItemClickListener (new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                //ListView Clicked item index
                int itemPosition = position+1;
                //ListView Clicked item value
                String itemValue = (String) listViewMessages.getItemAtPosition(position);
                //Show Toast
                Toast.makeText(getApplicationContext(),
                        "Position de l'item :" +itemPosition+" Element cliqué : " +itemValue, Toast.LENGTH_LONG)
                        .show();
            }
        });









    }

}
