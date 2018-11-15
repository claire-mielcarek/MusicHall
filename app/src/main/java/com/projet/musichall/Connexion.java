package com.projet.musichall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;






public class Connexion extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // permet de connaitre l'utilisateur connecte
        auth = FirebaseAuth.getInstance();
        context = this;

        setContentView(R.layout.connexion);
    }


    public void connexionWithMail(View v){
        // recupere les edit text contenant les donnees
        final EditText edit_mail = ((EditText) findViewById(R.id.mail));
        final EditText edit_mdp = ((EditText) findViewById(R.id.mdp));

        //recupere les donnees de l'utilisateur
        String mail = edit_mail.getText().toString();
        String mdp = edit_mdp.getText().toString();

        // connexion a la base de donnee avec les donnees de l'utilisateur
        auth.signInWithEmailAndPassword(mail, mdp).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    user = auth.getCurrentUser();
                    startActivity(new Intent(context, ProfilActivity.class));
                }else{
                    // changer la couleur des editText en rouge
                    edit_mail.setBackgroundResource(R.drawable.error_edit_text_bg);
                    edit_mdp.setBackgroundResource(R.drawable.error_edit_text_bg);

                    edit_mail.getBackground().setAlpha(25);
                    edit_mdp.getBackground().setAlpha(25);
                }
            }
        });
    }

    public void connexionWithChrome(View v){

    }

    public void connexionWithFacebook(View v){

    }

    public void connexionWithTwitter(View v){

    }

    public void inscription(View v){
        // rediriger vers l'activité d'inscription
        startActivity(new Intent(this, Inscription.class));
    }


}
