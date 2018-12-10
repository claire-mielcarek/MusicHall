package com.projet.musichall.profil;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.MainActivity;
import com.projet.musichall.R;




public class PrivateProfil extends Fragment {
    private View root;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference database;
    private TextView prenom;
    private TextView nom;
    private EditText mail;
    private TextView date_naissance;
    private TextView date_inscription;



    public static PrivateProfil newInstance() {
        return new PrivateProfil();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.private_profil, container, false);
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
        mail = root.findViewById(R.id.editmail);
        date_naissance = root.findViewById(R.id.datenaissance);
        date_inscription = root.findViewById(R.id.dateinscription);

        // implemente listener pour recuperer les donnees de firebase a chaque changement
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get user node
                DataSnapshot dataUser = dataSnapshot.child("Users").child(user.getUid());

                // get user data
                String valprenom = (String) dataUser.child("prenom").getValue();
                String valnom = (String) dataUser.child("nom").getValue();
                String valmail = (String) dataUser.child("mail").getValue();
                String valnaissance = (String) dataUser.child("dateNaissance").getValue();
                String valinscription = (String) dataUser.child("dateMembre").getValue();

                // set data on views
                if (valprenom != null)
                    prenom.setText(valprenom);
                if (valnom != null)
                    nom.setText(valnom);
                if (valmail != null)
                    mail.setText(valmail);
                if (valnaissance != null)
                    date_naissance.setText(valnaissance);
                if (valinscription != null)
                    date_inscription.setText(valinscription);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DatabaseChange", "Failed to read values.", error.toException());
            }

        });

        Button deconnexion = root.findViewById(R.id.deco);
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });
    }

}
