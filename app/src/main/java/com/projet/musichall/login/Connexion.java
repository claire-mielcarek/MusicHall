package com.projet.musichall.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.projet.musichall.profil.ProfilActivity;




public class Connexion extends BaseActivity {
    private static final int RC_SIGN_IN = 10;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private LoginButton loginButton;
    private SignInButton signButton;
    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Context context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // permet de connaitre l'utilisateur connecte
        auth = FirebaseAuth.getInstance();
        context = this;
        actionBar.setTitle(R.string.signin);

        setContentView(R.layout.connexion);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // initialisation button connexion facebook
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(EMAIL, PUBLIC_PROFILE);

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Connexion Facebook", loginResult.getAccessToken().getUserId());
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                logInFirebase(credential);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        // initialisation du button google
        signButton = findViewById(R.id.sign_google);
        //signButton.setSize(SignInButton.SIZE_STANDARD);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Connexion","C'est un bon debut!");
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
                Log.d("Connexion","Option google creee!");
                GoogleSignInClient client = GoogleSignIn.getClient(context, gso);
                Log.d("Connexion","Client creer!");
                Intent signInIntent = client.getSignInIntent();
                Log.d("Connexion","Intent recuperer!");
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private void logInFirebase(AuthCredential credential){

        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Connexion Facebook", "signInWithCredentialFacebook:success");
                    user = auth.getCurrentUser();
                    startActivity(new Intent(context, ProfilActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Connexion Facebook", "signInWithCredentialFacebook:failure", task.getException());
                    Toast.makeText(context, "Authentication failed.",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                    Log.d("Connexion", "signInWithMail:success : "+user.getUid());
                    startActivity(new Intent(context, ProfilActivity.class));
                }else{
                    // debug
                    Log.d("Connexion", "signInWithMail:failed : "+task.getException());
                    // changer la couleur des editText en rouge
                    edit_mail.setBackgroundResource(R.drawable.error_edit_text_bg);
                    edit_mdp.setBackgroundResource(R.drawable.error_edit_text_bg);

                    edit_mail.getBackground().setAlpha(50);
                    edit_mdp.getBackground().setAlpha(50);

                    MyMessageButton("La connection a échoué. Vérifier vos informations.");
                }
            }
        });
    }

    // affiche une boite de dialogue
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

    public void connexionWithGoogle(View v){  // Configure Google Sign In

    }

    public void connexionWithFacebook(View v){

    }

    public void connexionWithTwitter(View v){

    }

    public void inscription(View v){
        // rediriger vers l'activité d'inscription
        startActivity(new Intent(this, Inscription.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.d("Connexion","Debut resultat Intent!");

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Connexion","Recuperation du compte google");

                assert account != null;
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                Log.d("Connexion","Connexion a firebase avec un credential");
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Connexion", "signInWithCredentialGoogle:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    startActivity(new Intent(context, ProfilActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Connexion", "signInWithCredentialGoogle:failure", task.getException());
                                    Toast.makeText(context, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }catch (ApiException e){
                Log.w("Connexion", "Connexion Google failed", e);
            }
        }
    }
}
