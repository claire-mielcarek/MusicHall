package com.projet.musichall;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.group.Post;
import com.projet.musichall.group.postAuthorListener;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends BaseActivity {
    Context context;
    private FirebaseAuth auth;
    private FirebaseUser user;
    ArrayList<Post> listItems = new ArrayList<>();
    PostAdapter adapter;
    ListView list;
    View current_view;
    private String currentGroupId;
    private DatabaseReference data;
    private String date;
    private String text;
    private ArrayList<String> postAlreadyHere = new ArrayList<>();
    private boolean isAlreadyInstantiated;
    DatabaseReference publications;
    ChildEventListener publicationsListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.group_wall);
        Log.d("[WALL_CREATE_VIEW]", "onCreate");
        isAlreadyInstantiated = false;
        postAlreadyHere = new ArrayList<>();
        listItems = new ArrayList<>();


        actionBar.setTitle(R.string.home);

        // recupere l'objet permettant de gerer l'authantification d'un utilisateur
        auth = FirebaseAuth.getInstance();
        // recupere l'utilisateur pour savoir si celui-ci est connecte
        user = auth.getCurrentUser();
        Log.d("[ TEST MENU ]", "MainActivity onCreated done");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("[WALL_FRAGMENT]", "onStart");

        if(!this.isAlreadyInstantiated) {
            this.isAlreadyInstantiated = true;

            data = FirebaseDatabase.getInstance().getReference();


            list = findViewById(R.id.home_publications);
            adapter = new PostAdapter(this, listItems);


            addPostListener();

            //list.setAdapter(adapter);
        }
    }

    private void addPostListener(){
        /*
        data.child("Publications").orderByChild("date").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        DatabaseReference post =data.child("Publications").child(dataSnapshot.getValue().toString());
                        post.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot post) {
                                if (!postAlreadyHere.contains(post.getKey())){
                                    date = (String) post.child("date").getValue();
                                    String authorId = (String) post.child("author").getValue();
                                    text = (String) post.child("contenu").getValue();
                                    Log.d("[ WALL_DISPLAY ]", "nouveau post");
                                    data.child("Users").child(authorId).addValueEventListener(new postAuthorListener(post.getKey(), text, date, listItems, adapter, postAlreadyHere));
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        */
    }
}
