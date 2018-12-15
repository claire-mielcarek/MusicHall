package com.projet.musichall.discussion;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.MainActivity;
import com.projet.musichall.R;

public class Chat extends BaseActivity {

    TextView nom_interlocuteur;
    TextView prenom_interlocuteur;
    ListView listViewMessages;
    EditText messageEnvoyer;
    String userId;
    String destId;

    FloatingActionButton envoyer;



    String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_chat);

        listViewMessages = (ListView)findViewById(R.id.list_of_message);
        messageEnvoyer = (EditText) findViewById(R.id.input);
        envoyer = findViewById(R.id.fab);
        nom_interlocuteur = findViewById(R.id.nom_interlocuteur);
        prenom_interlocuteur = findViewById(R.id.prenom_interlocuteur);

        Intent intent = getIntent();

        // Récupérer les informations de la personne avec qui on communique
        if (intent!=null){
            String str = "";
            if (intent.hasExtra("nom")){
                nom_interlocuteur.setText(userName = intent.getExtras().get("nom").toString());
            }
            if(intent.hasExtra("prenom")){
                prenom_interlocuteur.setText (intent.getExtras().get("prenom").toString());
            }

        }


        Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();

        //Fin de la récupération

        //Récupérer le nom du user
        userId = intent.getStringExtra("userId");
        destId= intent.getStringExtra("groupId");


        envoyer.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       EditText input = (EditText)findViewById(R.id.input);

                                   }
                               });

        //Extrait pour modifier firebase

        /*
        if (!mail_value.equals("") && !mdp_value.equals("") && !mdp_confirm_value.equals("") && !prenom_value.equals("") && !nom_value.equals("")
                && !naissance_value.equals("") && mdp_value.equals(mdp_confirm_value)){
            auth.createUserWithEmailAndPassword(mail_value, mdp_value).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser user;

                    if (task.isSuccessful()){
                        // enregistrer les informations dans la base de donnees
                        user = auth.getCurrentUser();

                        if (user != null) {
                            database.child("Users").child(user.getUid()).child("dateMembre").setValue(date_membre);
                            database.child("Users").child(user.getUid()).child("dateNaissance").setValue(naissance_value);
                            database.child("Users").child(user.getUid()).child("genre").setValue(genre);
                            database.child("Users").child(user.getUid()).child("mail").setValue(user.getEmail());
                            database.child("Users").child(user.getUid()).child("motivation").setValue(motivation);
                            database.child("Users").child(user.getUid()).child("niveau").setValue(niveau);
                            database.child("Users").child(user.getUid()).child("nom").setValue(nom_value);
                            database.child("Users").child(user.getUid()).child("prenom").setValue(prenom_value);
                            database.child("Users").child(user.getUid()).child("ville").setValue(ville);  // TODO utiliser la localisation

                            // envoie vers l'application
                            startActivity(new Intent(context, MainActivity.class));
                        }
                    }else{
                        Log.d("InscriptionMail", String.valueOf(task.getException()));// probleme lors de la creation du compte
                        ErrorDetected(0);
                    }
                }
            });
        */

        //fin extrait

        //Chargement de la discussion



        displayChatMessage();


    }

    private void displayChatMessage(){

        listViewMessages = (ListView)findViewById(R.id.list_of_message);


            }





}
