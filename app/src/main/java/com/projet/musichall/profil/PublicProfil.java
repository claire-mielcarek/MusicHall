package com.projet.musichall.profil;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.MainActivity;
import com.projet.musichall.R;

import java.util.ArrayList;




public class PublicProfil extends Fragment {
    private View root;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;
    private TextView prenom;
    private TextView nom;
    private EditText ville;
    private RadioGroup genre;
    private TextView niveau;
    private SeekBar seek;
    private RadioGroup motivation;
    private int valseek = 0;


    public static PublicProfil newInstance() {
        return new PublicProfil();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.public_profil, container, false);
        root = rootView;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // database init
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        // get views
        prenom = root.findViewById(R.id.prenom);
        nom = root.findViewById(R.id.nom);
        ville = root.findViewById(R.id.editville);
        genre = root.findViewById(R.id.editgenre);
        niveau = root.findViewById(R.id.editniveau);
        seek = root.findViewById(R.id.editniveauseek);
        motivation = root.findViewById(R.id.choixmotivation);

        // implemente listener pour recuperer les donnees de firebase a chaque changement
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get user node
                DataSnapshot dataUser = dataSnapshot.child("Users").child(user.getUid());

                // get user data
                String valprenom = (String) dataUser.child("prenom").getValue();
                String valnom = (String) dataUser.child("nom").getValue();
                String valville = (String) dataUser.child("ville").getValue();
                String valgenre = (String) dataUser.child("genre").getValue();
                String valniveau = (String) dataUser.child("niveau").getValue();
                String valmotivation = (String) dataUser.child("motivation").getValue();

                // set data on views
                if (valprenom != null)
                    prenom.setText(valprenom);
                if (valnom != null)
                    nom.setText(valnom);
                if (valville != null)
                    ville.setText(valville);
                if (valgenre != null) {
                    switch (valgenre) {
                        case "Femme":
                            genre.clearCheck();
                            genre.check(R.id.radiof);
                            break;
                        case "Inconnu":
                            genre.clearCheck();
                            genre.check(R.id.radioi);
                            break;
                        case "Homme":
                            genre.clearCheck();
                            genre.check(R.id.radioh);
                            break;
                    }
                }
                if (valniveau != null) {
                    niveau.setText(valniveau);
                    switch (valniveau) {
                        case "Debutant":
                            valseek = 10;
                            break;
                        case "Intermediaire":
                            valseek = 30;
                            break;
                        case "Avancee":
                            valseek = 50;
                            break;
                        case "Expert":
                            valseek = 70;
                            break;
                        case "Maitre":
                            valseek = 100;
                            break;
                    }
                }
                if (valmotivation != null) {
                    switch (valmotivation) {
                        case "Amateur":
                            motivation.clearCheck();
                            motivation.check(R.id.radioa);
                            break;
                        case "Professionel":
                            motivation.clearCheck();
                            motivation.check(R.id.radiop);
                            break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DatabaseChange", "Failed to read values.", error.toException());
            }

        });

        // bind seekbar and textview <niveau>
        seek.setProgress(valseek);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 0 && progress <= 20){
                    niveau.setText("Débutant");
                }else if (progress >= 21 && progress <= 40){
                    niveau.setText("Intermédiaire");
                }else if (progress >= 41 && progress <= 60){
                    niveau.setText("Avancé");
                }else if (progress >= 61 && progress <= 80){
                    niveau.setText("Expert");
                }else if (progress >= 81 && progress <= 100){
                    niveau.setText("Maitre");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seek.refreshDrawableState();

        Button deconnexion = root.findViewById(R.id.deco);
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });


        // definition onclick pour button vers list view
        Button instru = root.findViewById(R.id.instru);
        instru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), ListProfilActivity.class);
                i.putExtra("type_liste", 1);
                startActivity(i);
            }
        });

        Button ecouter = root.findViewById(R.id.ecouter);
        ecouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), ListProfilActivity.class);
                i.putExtra("type_liste", 2);
                startActivity(i);
            }
        });

        Button jouer = root.findViewById(R.id.jouer);
        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), ListProfilActivity.class);
                i.putExtra("type_liste", 3);
                startActivity(i);
            }
        });
    }

}
