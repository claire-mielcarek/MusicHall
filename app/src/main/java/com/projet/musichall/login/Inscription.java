package com.projet.musichall.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.MainActivity;
import com.projet.musichall.R;
import com.projet.musichall.Utils;
import com.projet.musichall.profile.IResultConnectUser;
import com.projet.musichall.profile.ProfilActivity;
import com.projet.musichall.profile.User;




public class Inscription extends BaseActivity {
    private FirebaseAuth auth;
    private DatabaseReference database;
    private Context context;

    private EditText mail;
    private EditText mdp;
    private EditText mdp_confirm;
    private EditText prenom;
    private EditText nom;
    private EditText naissance;
    private boolean probleme;
    private boolean first;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inscription);

        // recupere l'objet firebase pour l'authantification et une reference vers la database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        context = this;
        actionBar.setTitle(R.string.signup);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final TextView niveau = findViewById(R.id.editniveau);

        SeekBar seek = findViewById(R.id.editniveauseek);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 0 && progress <= 20){
                    niveau.setText(R.string.débutant);
                }else if (progress >= 21 && progress <= 40){
                    niveau.setText(R.string.intermédiaire);
                }else if (progress >= 41 && progress <= 60){
                    niveau.setText(R.string.avancé);
                }else if (progress >= 61 && progress <= 80){
                    niveau.setText(R.string.expert);
                }else if (progress >= 81 && progress <= 100){
                    niveau.setText(R.string.maitre);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void try_inscription(View v){
        int code = -1;
        first = true;
        probleme = false;

        mail = findViewById(R.id.mail);
        mdp = findViewById(R.id.mdp);
        mdp_confirm = findViewById(R.id.mdp_confirm);
        prenom = findViewById(R.id.prenom);
        nom = findViewById(R.id.nom);
        naissance = findViewById(R.id.dateNaissance);

        final String mail_value = mail.getText().toString();
        final String mdp_value = mdp.getText().toString();
        String mdp_confirm_value = mdp_confirm.getText().toString();
        final String prenom_value = prenom.getText().toString();
        final String nom_value = nom.getText().toString();
        final String naissance_value = naissance.getText().toString();

        // valeurs definies durant l'inscription
        final String genre = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.editgenre)).getCheckedRadioButtonId())).getText().toString();
        final String niveau = ((TextView) findViewById(R.id.editniveau)).getText().toString();
        final String motivation = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.choixmotivation)).getCheckedRadioButtonId())).getText().toString();
        final String date_membre = android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString();
        final String ville = "Chicoutimi";

        if (!mail_value.equals("") && !mdp_value.equals("") && !mdp_confirm_value.equals("") && !prenom_value.equals("") && !nom_value.equals("")
                && !naissance_value.equals("") && mdp_value.equals(mdp_confirm_value)){

            // check if first name and last name already exist
            database.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (first) {
                        for (DataSnapshot firebaseUser : dataSnapshot.getChildren()) {
                            if (prenom_value.equals(firebaseUser.child("prenom").getValue()) && nom_value.equals(firebaseUser.child("nom").getValue())) {
                                ErrorDetected(8);
                                probleme = true;
                                break;
                            }
                        }

                        if (!probleme)
                            completeSignIn(mail_value, mdp_value, date_membre, naissance_value, genre, motivation, niveau, nom_value, prenom_value, ville);

                        first = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    ErrorDetected(-1);
                }
            });

        }else{
            // definie le code d'erreur
            if (mail_value.equals(""))
                code = 1;
            else if (mdp_value.equals(""))
                code  = 2;
            else if (mdp_confirm_value.equals(""))
                code = 3;
            else if (prenom_value.equals(""))
                code = 4;
            else if (nom_value.equals(""))
                code = 5;
            else if (naissance_value.equals(""))
                code  = 6;
            else if (!mdp_value.equals(mdp_confirm_value))
                code = 7;

            ErrorDetected(code);
        }

    }


    private void completeSignIn(String mail, String mdp, final String date_membre, final String naissance_value,
                                final String genre, final String motivation, final String niveau, final String nom,
                                final String prenom, final String ville){

        auth.createUserWithEmailAndPassword(mail, mdp).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser firebaseUser;

                if (task.isSuccessful()){
                    // enregistrer les informations dans la base de donnees
                    firebaseUser = auth.getCurrentUser();

                    if (firebaseUser != null) {
                        database.child("Users").child(firebaseUser.getUid()).child("dateMembre").setValue(date_membre);
                        database.child("Users").child(firebaseUser.getUid()).child("dateNaissance").setValue(naissance_value);
                        database.child("Users").child(firebaseUser.getUid()).child("genre").setValue(genre);
                        database.child("Users").child(firebaseUser.getUid()).child("mail").setValue(firebaseUser.getEmail());
                        database.child("Users").child(firebaseUser.getUid()).child("motivation").setValue(motivation);
                        database.child("Users").child(firebaseUser.getUid()).child("niveau").setValue(niveau);
                        database.child("Users").child(firebaseUser.getUid()).child("nom").setValue(nom);
                        database.child("Users").child(firebaseUser.getUid()).child("prenom").setValue(prenom);
                        database.child("Users").child(firebaseUser.getUid()).child("ville").setValue(ville);

                        setContentView(R.layout.waiting);

                        // get data of user from firebase
                        User user = User.InstantiateUser(User.Auth.MAIL);
                        user.attachUserToFirebase(true, new IResultConnectUser() {
                            @Override
                            public void OnSuccess() {
                                // send to profile
                                startActivity(new Intent(context, ProfilActivity.class));
                                finish();
                            }

                            @Override
                            public void OnFailed() {
                                //Utils.MyMessageButton("Problems to read data", context);
                                finish();
                            }
                        });
                    }
                }else{
                    Log.d("InscriptionMail", String.valueOf(task.getException()));    // probleme lors de la creation du compte
                    ErrorDetected(0);
                }
            }
        });
    }

    public void ErrorDetected(int code){
        String message;

        // definie un message suivant le code recu
        if (code == 0){
            message = "Votre inscription a echoué. L'adresse mail est peut-être déjà utilisé.";
            mail.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mail.getBackground().setAlpha(50);
            mdp.getBackground().setAlpha(50);
            mdp_confirm.getBackground().setAlpha(50);
        }else if (code == 1) {
            message = "L'adresse mail est obligatoire pour s'inscrire.";
            mail.setBackgroundResource(R.drawable.error_edit_text_bg);
            mail.getBackground().setAlpha(50);
        }else if (code == 2) {
            message = "Le mot de passe est obligatoire pour s'inscrire.";
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.getBackground().setAlpha(50);
        }else if (code == 3) {
            message = "La confirmation du mot de passe est obligatoire pour s'inscrire.";
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.getBackground().setAlpha(50);
        }else if (code == 4) {
            message = "Le prenom est obligatoire pour s'inscrire.";
            prenom.setBackgroundResource(R.drawable.error_edit_text_bg);
            prenom.getBackground().setAlpha(50);
        }else if (code == 5) {
            message = "Le nom est obligatoire pour s'inscrire.";
            nom.setBackgroundResource(R.drawable.error_edit_text_bg);
            nom.getBackground().setAlpha(50);
        }else if (code == 6) {
            message = "La date de naissance est obligatoire pour s'inscrire.";
            naissance.setBackgroundResource(R.drawable.error_edit_text_bg);
            naissance.getBackground().setAlpha(50);
        }else if (code == 7) {
            message = "Les mots de passes ne sont pas identiques. Veuillez réessayer.";
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.getBackground().setAlpha(50);
            mdp_confirm.getBackground().setAlpha(50);
        }else if (code == 8){
            message = "Le nom et prenom existe déjà.";
            prenom.setBackgroundResource(R.drawable.error_edit_text_bg);
            nom.setBackgroundResource(R.drawable.error_edit_text_bg);
            prenom.getBackground().setAlpha(50);
            nom.getBackground().setAlpha(50);
        }else{
            message = "Une erreur inconnue a été rencontré. Veuillez réessayer.";
        }

        Utils.MyMessageButton(message, context);
    }

}
