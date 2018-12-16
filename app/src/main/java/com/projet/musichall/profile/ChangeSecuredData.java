package com.projet.musichall.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.projet.musichall.login.Connexion;


public class ChangeSecuredData extends BaseActivity {
    private boolean mail = false;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference userData;
    private TextView titre;
    private Button valid;
    private EditText mdp;
    private EditText mdp_confirm;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_secured_data);
        actionBar.setTitle("Edit data");

        mail = getIntent().getBooleanExtra("Mail", true);
    }


    @Override
    protected void onStart() {
        super.onStart();

        valid = findViewById(R.id.validate);
        titre = findViewById(R.id.titre);
        mdp = findViewById(R.id.mdp);
        mdp_confirm = findViewById(R.id.mdp_confirm);
        userData = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        if (mail) {
            titre.setText(R.string.changerdeMail);
            mdp.setHint("Nouvelle adresse mail");
            mdp.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            mdp_confirm.setHint("Confirmation de l'adresse");
            mdp_confirm.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }else{
            titre.setText(R.string.changerdeMdp);
            mdp.setHint("Nouveau mot de passe");
            mdp_confirm.setHint("Confirmation du mot de passe");
        }


        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = mdp.getText().toString();
                final String text_confirm = mdp_confirm.getText().toString();
                Intent i;

                if (user!= null && !text.isEmpty() && !text_confirm.isEmpty() && text.equals(text_confirm)) {
                    Log.d("VALIDATION", "Mdp : " + text + " Mdp_confirm : " + text_confirm);
                    mdp.setBackgroundResource(R.drawable.success_edit_text_bg);
                    mdp.getBackground().setAlpha(50);
                    mdp_confirm.setBackgroundResource(R.drawable.success_edit_text_bg);
                    mdp_confirm.getBackground().setAlpha(50);

                    i = new Intent(getApplicationContext(), Connexion.class);
                    i.putExtra("ChangeData", true);
                    i.putExtra("Value", text);
                    i.putExtra("Mail", mail);

                    auth.signOut();
                    startActivity(i);
                }else{
                    mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
                    mdp.getBackground().setAlpha(50);
                    mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
                    mdp_confirm.getBackground().setAlpha(50);

                    if (text.isEmpty() || text_confirm.isEmpty())
                        MyMessageButton("Tous les champs doivent être remplis");
                    else if (!text.equals(text_confirm))
                        if (mail)
                            MyMessageButton("Les adresses mail ne correspondent pas");
                        else MyMessageButton("Les mot de passes ne correspondent pas");
                    else MyMessageButton("Une erreur inconnue s'est produite. Veuillez réessayer ultérieurement");
                }
            }
        });

    }


    public void MyMessageButton(String message) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setTitle(message);
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);

        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dlgAlert.create().show();
    }
}
