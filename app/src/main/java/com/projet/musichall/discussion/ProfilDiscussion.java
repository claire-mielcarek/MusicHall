package com.projet.musichall.discussion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
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
import com.projet.musichall.profile.IResultConnectUser;
import com.projet.musichall.profile.MyGridAdapter;
import com.projet.musichall.profile.MyListAdapter;

import java.util.Date;
import java.util.Iterator;



public class ProfilDiscussion extends BaseActivity {
    private DatabaseReference data;
    FirebaseUser user;
    CircularImageView avatar;
    TextView prenom;
    TextView nom;
    TextView ville;
    TextView genre;
    TextView niveau;
    TextView motivation;
    ListView instrus;
    ListView lstyles;
    ListView pstyles;
    GridView portfolio;
    Button lancerDiscussion;

    private Intent intent;
    Context context;
    String nomS;
    String prenomS;
    String idS;

    private OtherUser otherUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        user = FirebaseAuth.getInstance().getCurrentUser();
        context = this;
        intent = getIntent();

        // get user data
        idS = intent.getStringExtra("id");
        otherUser = new OtherUser(idS);
        otherUser.attachUserToFirebase(true, new IResultConnectUser() {
            @Override
            public void OnSuccess() {
                setContentView(R.layout.discussion_profil);
                getAllView();
                fillPage();
            }

            @Override
            public void OnFailed() {
                // show message
            }
        });

    }

    private void fillPage(){
        MyListAdapter adapter_instru;
        MyListAdapter adapter_ecouter;
        MyListAdapter adapter_jouer;
        MyGridAdapter adapter_portfolio;
        //Log.d("TEST DISCUSSION", idS + "  " + otherUser.getPrenom() + " " + otherUser.getNom());

        // Informations
        if (otherUser.getAvatar() != null)
            avatar.setImageBitmap(otherUser.getAvatar());
        nom.setText(otherUser.getNom());
        prenom.setText(otherUser.getPrenom());
        ville.setText(otherUser.getVille());
        genre.setText(otherUser.getGenre());
        niveau.setText(otherUser.getNameNiveau());
        motivation.setText(otherUser.getMotivation());

        // Lists
        adapter_instru = new MyListAdapter(this, otherUser.getInstruments());
        instrus.setAdapter(adapter_instru);
        adapter_ecouter = new MyListAdapter(this, otherUser.getListened_styles());
        lstyles.setAdapter(adapter_ecouter);
        adapter_jouer = new MyListAdapter(this, otherUser.getPlayed_styles());
        pstyles.setAdapter(adapter_jouer);

        // Portfolio
        adapter_portfolio = new MyGridAdapter(this, otherUser.getImages());
        portfolio.setAdapter(adapter_portfolio);
    }

    private void getAllView(){
        avatar = findViewById(R.id.photo);
        prenom = (TextView) findViewById(R.id.prenom);
        nom = (TextView) findViewById(R.id.nom);
        ville = findViewById(R.id.ville);
        genre = findViewById(R.id.genre);
        niveau = findViewById(R.id.niveau);
        motivation = findViewById(R.id.motivation);
        instrus = findViewById(R.id.instru);
        lstyles = findViewById(R.id.ecouter);
        pstyles = findViewById(R.id.jouer);
        portfolio = findViewById(R.id.grid);

        lancerDiscussion = (Button) findViewById(R.id.lancerdiscussion);
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


                    Intent i = new Intent(ProfilDiscussion.this, Chat.class);
                    i.putExtra("nom", nomS);
                    i.putExtra("prenom", prenomS);
                    startActivity(i);
                }else{
                    Intent intent = new Intent(ProfilDiscussion.this, ShouldLogin.class);
                    startActivity(intent);
                }
            }
        });
    }

}

