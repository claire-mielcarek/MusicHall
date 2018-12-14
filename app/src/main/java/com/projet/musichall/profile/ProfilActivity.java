package com.projet.musichall.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.projet.musichall.login.Connexion;


public class ProfilActivity extends BaseActivity {
    // Firebase reference
    private FirebaseAuth auth;
    private FirebaseUser fireUser;
    private DatabaseReference database;
    private StorageReference storage;

    private ViewPager pager;
    private PagerAdapter adapter;
    private User user;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waiting);
        actionBar.setTitle(R.string.profile);

        // connect to firebase
        auth = FirebaseAuth.getInstance();
        fireUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();

        adapter = null;
        pager = null;

        if (fireUser != null) {
            // instantiate a new user
            user = User.InstantiateUser(User.Auth.MAIL);

            user.attachUserToFirebase(true, new IResultConnectUser() {
                @Override
                public void OnSuccess(){  // if operation is a success so show user's informations
                    Log.d("CRASH", "Bonjour " + fireUser);
                    // define the real view for the activity
                    setContentView(R.layout.activity_profil);

                    // get viewpager and set its adapter
                    pager = findViewById(R.id.viewpager);
                    adapter = new FragmentSlideAdapter(getSupportFragmentManager());
                    pager.setAdapter(adapter);
                }

                @Override
                public void OnFailed() {
                    Log.w("DatabaseChange", "Failed to read values.");
                    // TODO message + return to connection
                }
             });
        }else{
            // if not connected we redirect to connexion
            startActivity(new Intent(this, Connexion.class));
        }

        // get all user's data
        /*database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DatabaseChange", "Failed to read values.", databaseError.toException());
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

