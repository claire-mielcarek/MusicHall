package com.projet.musichall.discussion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
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
import com.projet.musichall.Search.CriteresMusiciens;
import com.projet.musichall.ShouldLogin;

import java.util.Date;
import java.util.Iterator;

public class ProfilDiscussion extends BaseActivity {

    FirebaseUser user;
    TextView prenom;
    TextView nom;
    FloatingActionButton lancerDiscussion;
    private DatabaseReference data;

    Context context;
    String nomS;
    String prenomS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_profil);



        prenom = (TextView)findViewById(R.id.prenom);
        nom = (TextView)findViewById(R.id.nom);
        lancerDiscussion = (FloatingActionButton)findViewById(R.id.bouton_lancer_discussion);

        user = FirebaseAuth.getInstance().getCurrentUser();


        context = this;

        Intent intent = getIntent();
        if (intent!=null){
            if (intent.hasExtra("nom") && intent.getStringExtra("nom") != null){
                nomS =(intent.getExtras().get("nom").toString());
                Toast.makeText(context, nomS, Toast.LENGTH_SHORT).show();


            }
            if (intent.hasExtra("prenom") && intent.getStringExtra("prenom") != null ){
                prenomS =(intent.getExtras().get("prenom").toString());
                Toast.makeText(context, prenomS, Toast.LENGTH_SHORT).show();
                //prenom.setText(prenomS);
            }


        }

        lancerDiscussion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user != null) {
                    data = FirebaseDatabase.getInstance().getReference();
                    // Ajout de la notion de conversation de l'user avec la personne recherchée
                    DatabaseReference newDiscussion = data.child("Users").child(user.getUid()).child("conversation");
                    newDiscussion.push().setValue(nomS);
                    // Ajout de la notion de conversation pour la personne recherchée (avec celui qui a débuté la discussion)
                    //DatabaseReference  newDiscussion2 = data.child("Users").child()


                    Intent intent = new Intent(ProfilDiscussion.this, Chat.class);
                    intent.putExtra("nom", nomS);
                    intent.putExtra("prenom", prenomS);
                    startActivity(intent);
                }
            else{
                    Intent intent = new Intent(ProfilDiscussion.this, ShouldLogin.class);
                    startActivity(intent);
                }
            }
        });

    }
}

