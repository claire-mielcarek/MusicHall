package com.projet.musichall.discussion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.projet.musichall.Utils;
import com.projet.musichall.profile.IResultConnectUser;
import com.projet.musichall.profile.ImageWatcher;
import com.projet.musichall.profile.MyGridAdapter;
import com.projet.musichall.profile.MyListAdapter;

import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class ProfilDiscussion extends BaseActivity {
    private DatabaseReference data;
    private FirebaseUser user;
    private LinearLayout parentInstru;
    private LinearLayout parentLstyles;
    private LinearLayout parentPstyles;
    private CircularImageView avatar;
    private TextView prenom;
    private TextView nom;
    private TextView ville;
    private TextView genre;
    private TextView niveau;
    private TextView motivation;
    private GridView portfolio;
    private Button lancerDiscussion;

    private Context context;
    private String nomS;
    private String prenomS;
    private String idS;

    private OtherUser otherUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waiting);
        Intent intent;

        user = FirebaseAuth.getInstance().getCurrentUser();
        context = this;
        intent = getIntent();
        idS = intent.getStringExtra("id");

        if (savedInstanceState != null) {
            otherUser = (OtherUser) savedInstanceState.getSerializable("otherUser");
            setContentView(R.layout.discussion_profil);
            getAllView();
            fillPage();
        }else{
            // get user data
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
                    // show message and return to research activity
                    Utils.MyMessageButton("Failed to load data of user.", context);
                }
            });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void fillPage(){
        MyGridAdapter adapter_portfolio;
        //Log.d("TEST DISCUSSION", idS + "  " + otherUser.getPrenom() + " " + otherUser.getNom());

        // Informations
        if (otherUser.getAvatar() != null)
            avatar.setImageBitmap(otherUser.getAvatar());
        nom.setText(otherUser.getNom());
        //Récupération du nom pour l'intent
        nomS = otherUser.getNom();

        prenom.setText(otherUser.getPrenom());
        //Récuparation du prénom
        prenomS = otherUser.getPrenom();
        Log.d("on a son prenom", prenomS);
        ville.setText(otherUser.getVille());
        genre.setText(otherUser.getGenre());
        niveau.setText(otherUser.getNameNiveau());
        motivation.setText(otherUser.getMotivation());

        // Lists
        displayList(parentInstru, otherUser.getInstruments());
        displayList(parentLstyles, otherUser.getListened_styles());
        displayList(parentPstyles, otherUser.getPlayed_styles());

        // Portfolio
        adapter_portfolio = new MyGridAdapter(this, otherUser.getImages());
        portfolio.setAdapter(adapter_portfolio);
        portfolio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                watchImage(otherUser.getPathImages().get(position));
            }
        });
    }

    private void displayList(LinearLayout parent, List<String> list){
        TextView newchild;

        for (int i = 0; i<list.size(); i++){
            newchild = new TextView(context);
            newchild.setText(list.get(i));

            parent.addView(newchild);
        }
    }


    private void watchImage(String name){
        String path = "images/portfolio/"+idS+"/"+name;
        Intent i = new Intent(context, ImageWatcher.class);
        i.putExtra("path", path);

        startActivity(i);
    }

    private void getAllView(){
        // Linear layout
        parentInstru = findViewById(R.id.pinstru);
        parentLstyles = findViewById(R.id.plstyle);
        parentPstyles = findViewById(R.id.ppstyle);
        // text view and avatar
        avatar = findViewById(R.id.photo);
        prenom = (TextView) findViewById(R.id.prenom);
        nom = (TextView) findViewById(R.id.nom);
        ville = findViewById(R.id.ville);
        genre = findViewById(R.id.genre);
        niveau = findViewById(R.id.niveau);
        motivation = findViewById(R.id.motivation);
        // grid view
        portfolio = findViewById(R.id.grid);

        lancerDiscussion = (Button) findViewById(R.id.lancerdiscussion);
        lancerDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    data = FirebaseDatabase.getInstance().getReference();
                    // Ajout de la notion de conversation de l'user avec la personne recherchée
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("otherUser", otherUser);
    }
}

