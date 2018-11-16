package com.projet.musichall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;




public class Inscription extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference database;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inscription);

        // recupere l'objet firebase pour l'authantification et une reference vers la database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        context = this;

        Log.d("Database", "Database: "+database);
    }



    public void try_inscription(View v){
        final EditText mail = findViewById(R.id.mail);
        final EditText mdp = findViewById(R.id.mdp);
        final EditText mdp_confirm = findViewById(R.id.mdp_confirm);

        String mail_value = mail.getText().toString();
        String mdp_value = mdp.getText().toString();
        String mdp_confirm_value = mdp_confirm.getText().toString();

        // valeurs definies durant l'inscription
        final String prenom = ((EditText) findViewById(R.id.prenom)).getText().toString();
        final String nom = ((EditText) findViewById(R.id.nom)).getText().toString();
        final String naissance = ((EditText) findViewById(R.id.dateNaissance)).getText().toString();
        final String genre = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.editgenre)).getCheckedRadioButtonId())).getText().toString();
        final String niveau = ((TextView) findViewById(R.id.editniveau)).getText().toString();
        final String motivation = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.choixmotivation)).getCheckedRadioButtonId())).getText().toString();
        final String date_membre = new Date().toString();
        final String ville ="Chicoutimi";

        if (!mail_value.equals("") && !mdp_value.equals("") && !mdp_confirm_value.equals("") && mdp_value.equals(mdp_confirm_value)){
            auth.createUserWithEmailAndPassword(mail_value, mdp_value).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser user;

                    if (task.isSuccessful()){
                        // enregistrer les informations dans la base de donnees
                        user = auth.getCurrentUser();

                        if (user != null) {
                            if (!date_membre.equals(""))
                                database.child("Users").child(user.getUid()).child("dateMembre").setValue(date_membre);
                            if (!naissance.equals(""))
                                database.child("Users").child(user.getUid()).child("dateNaissance").setValue(naissance);
                            database.child("Users").child(user.getUid()).child("genre").setValue(genre);
                            database.child("Users").child(user.getUid()).child("mail").setValue(user.getEmail());
                            database.child("Users").child(user.getUid()).child("motivation").setValue(motivation);
                            database.child("Users").child(user.getUid()).child("niveau").setValue(niveau);
                            if (!nom.equals(""))
                                database.child("Users").child(user.getUid()).child("nom").setValue(nom);
                            if (!prenom.equals(""))
                                database.child("Users").child(user.getUid()).child("prenom").setValue(prenom);
                            database.child("Users").child(user.getUid()).child("ville").setValue(ville);  // TODO utiliser la localisation

                            // envoie vers l'application
                            startActivity(new Intent(context, ProfilActivity.class));
                        }
                    }else{
                        Log.d("ERROOOOOOOOOOR", String.valueOf(task.getException()));// probleme lors de la creation du compte
                        /*mail.setBackgroundResource(R.drawable.error_edit_text_bg);
                        mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
                        mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);*/
                    }
                }
            });

        }else{
            // champ non rempli ou non conforme
            mail.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);

            mail.getBackground().setAlpha(25);
            mdp.getBackground().setAlpha(25);
            mdp_confirm.getBackground().setAlpha(25);
        }


    }


}
