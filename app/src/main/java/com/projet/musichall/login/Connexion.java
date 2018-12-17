package com.projet.musichall.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.projet.musichall.Utils;
import com.projet.musichall.profile.IResultConnectUser;
import com.projet.musichall.profile.ProfilActivity;
import com.projet.musichall.profile.User;




public class Connexion extends BaseActivity {
    private static final int RC_SIGN_IN = 10;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private boolean changeData;
    private boolean mail;
    private boolean first = true;
    private String newData;
    private LoginButton loginButton;
    private SignInButton signButton;
    private CallbackManager callbackManager;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private Context context;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the context
        changeData = getIntent().getBooleanExtra("ChangeData", false);
        mail = getIntent().getBooleanExtra("Mail", false);
        if (changeData)
            newData = getIntent().getStringExtra("Value");

        // permet de connaitre l'utilisateur connecte
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users");
        context = this;
        actionBar.setTitle(R.string.signin);

        FacebookSdk.sdkInitialize(context);
        setContentView(R.layout.connexion);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button inscription = findViewById(R.id.inscription);
        first = true;

        // hide the sign up button
        if (changeData)
            inscription.setVisibility(View.INVISIBLE);

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
                logInFirebase(credential, User.Auth.FACEBOOK);
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

    private void logInFirebase(AuthCredential credential, final Enum<User.Auth> type){

        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
             if (task.isSuccessful()) {
                 // Sign in success, update UI with the signed-in firebaseUser's information
                 Log.d("Connexion Credential", "signInWithCredential:success");
                 firebaseUser = auth.getCurrentUser();
                 LoadDataAndStartActivity(type);
             } else {
                 // If sign in fails, display a message to the firebaseUser.
                 Log.w("Connexion Credential", "signInWithCredential:failure", task.getException());
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
                 firebaseUser = auth.getCurrentUser();
                 Log.d("Connexion", "signInWithMail:success : "+ firebaseUser.getUid());
                 LoadDataAndStartActivity(User.Auth.MAIL);
             }else{
                 // debug
                 Log.d("Connexion", "signInWithMail:failed : "+task.getException());
                 // changer la couleur des editText en rouge
                 edit_mail.setBackgroundResource(R.drawable.error_edit_text_bg);
                 edit_mdp.setBackgroundResource(R.drawable.error_edit_text_bg);

                 edit_mail.getBackground().setAlpha(50);
                 edit_mdp.getBackground().setAlpha(50);

                 Utils.MyMessageButton("La connection a échoué. Vérifier vos informations.", context);
             }
            }
        });
    }

    public void changeDataFunction(final String value){
        if (mail) {
            firebaseUser.updateEmail(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        database.child(firebaseUser.getUid()).child("mail").setValue(value);
                        Log.d("CHANGE DATA", "User email address updated.");
                    }else{
                        Log.d("CHANGE DATA", "User email address update failed.");
                    }
                }
            });
        }else{
            firebaseUser.updatePassword(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("CHANGE DATA", "User password updated.");
                    }else{
                        Log.d("CHANGE DATA", "User password update failed.");
                    }
                }
            });
        }
    }

    public void connexionWithTwitter(View v){

    }

    public void inscription(View v){
        // rediriger vers l'activité d'inscription
        startActivity(new Intent(this, Inscription.class));
        finish();
    }

    public void LoadDataAndStartActivity(Enum<User.Auth> type){
        final User user;
        if (changeData)
            changeDataFunction(newData);
        user = User.InstantiateUser(type);

        if (type == User.Auth.GOOGLE || type == User.Auth.FACEBOOK){
            database.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (first){
                        String email = firebaseUser.getEmail();
                        String displayName = firebaseUser.getDisplayName();
                        String nom = (String) dataSnapshot.child("nom").getValue();
                        String prenom = (String) dataSnapshot.child("prenom").getValue();
                        String genre = (String) dataSnapshot.child("genre").getValue();
                        String motivation = (String) dataSnapshot.child("motivation").getValue();
                        String date_membre = android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString();

                        // fill personal data
                        if (nom == null || prenom == null || nom.isEmpty() || prenom.isEmpty() || genre == null || motivation == null || genre.isEmpty() || motivation.isEmpty()){
                            Log.d("CO GOOGLE FACEBOOK", "Je suis passe par la!  "+dataSnapshot.getValue());
                            database.child(firebaseUser.getUid()).child("nom").setValue(displayName.split(" ")[0]);
                            database.child(firebaseUser.getUid()).child("prenom").setValue(displayName.split(" ")[1]);
                            database.child(firebaseUser.getUid()).child("mail").setValue(email);
                            database.child(firebaseUser.getUid()).child("genre").setValue("Femme");
                            database.child(firebaseUser.getUid()).child("dateMembre").setValue(date_membre);
                            database.child(firebaseUser.getUid()).child("motivation").setValue("Amateur");
                            database.child(firebaseUser.getUid()).child("niveau").setValue("Debutant");
                            database.child(firebaseUser.getUid()).child("ville").setValue("Chicoutimi");
                        }

                        first = false;

                        // fill user object
                        setContentView(R.layout.waiting);
                        user.attachUserToFirebase(true, new IResultConnectUser() {
                            @Override
                            public void OnSuccess(){  // if operation is a success so show firebaseUser's informations
                                // start Profile Activity
                                startActivity(new Intent(context, ProfilActivity.class));
                                finish();
                            }

                            @Override
                            public void OnFailed() {
                                Log.w("DatabaseChange", "Failed to read values");

                                // show message for firebaseUser then reload connection
                                Utils.MyMessageButton("Read personal value has failed. Retry later please", context);
                                startActivity(new Intent(context, Connexion.class));
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            // fill user object
            setContentView(R.layout.waiting);
            user.attachUserToFirebase(true, new IResultConnectUser() {
                @Override
                public void OnSuccess(){  // if operation is a success so show firebaseUser's informations
                    // start Profile Activity
                    startActivity(new Intent(context, ProfilActivity.class));
                    finish();
                }

                @Override
                public void OnFailed() {
                    Log.w("DatabaseChange", "Failed to read values");

                    // show message for firebaseUser then reload connection
                    Utils.MyMessageButton("Read personal value has failed. Retry later please", context);
                    startActivity(new Intent(context, Connexion.class));
                }
            });
        }
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
                logInFirebase(credential, User.Auth.GOOGLE);
            }catch (ApiException e){
                Log.w("Connexion", "Connexion Google failed", e);
            }
        }
    }
}
