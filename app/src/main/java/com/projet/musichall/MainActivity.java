package com.projet.musichall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.projet.musichall.login.Connexion;
import com.projet.musichall.profile.IResultConnectUser;
import com.projet.musichall.profile.ProfilActivity;
import com.projet.musichall.profile.User;

import com.projet.musichall.group.wall.AddPost;




public class MainActivity extends BaseActivity {
    Context context;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    ArrayList<Post> listItems = new ArrayList<>();
    PostAdapter adapter;
    ListView list;
    private DatabaseReference data;
    private String date;
    private String text;
    private ArrayList<String> postAlreadyHere = new ArrayList<>();
    private boolean isAlreadyInstantiated;
    DatabaseReference publications;
    final private int ADD_POST = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        Log.d("[WALL_CREATE_VIEW]", "onCreate");
        isAlreadyInstantiated = false;
        postAlreadyHere = new ArrayList<>();
        listItems = new ArrayList<>();

        actionBar.setTitle(R.string.home);

        // recupere l'objet permettant de gerer l'authantification d'un utilisateur
        auth = FirebaseAuth.getInstance();
        // recupere l'utilisateur pour savoir si celui-ci est connecte
        firebaseUser = auth.getCurrentUser();
        Log.d("[ TEST MENU ]", "MainActivity onCreated done");
    }

    @Override
    public void onStart() {
        super.onStart();
        User user;
        Log.d("[HOME_ACTIVITY]", "onStart");

        if(!this.isAlreadyInstantiated) {
            Log.d("[HOME_ACTIVITY]", "screen wasn't instantiated");
            this.isAlreadyInstantiated = true;

            data = FirebaseDatabase.getInstance().getReference();

            // load user data if connected
            if (firebaseUser != null) {
                // change view to inform user to wait
                setContentView(R.layout.waiting);

                // get user data
                user = User.InstantiateUser(User.Auth.MAIL);
                user.attachUserToFirebase(true, new IResultConnectUser() {
                    @Override
                    public void OnSuccess() {  // if operation is a success so show user's informations
                        // redefine good layout
                        setContentView(R.layout.activity_main);
                        list = findViewById(R.id.home_publications);
                        adapter = new PostAdapter(context, listItems);
                        list.setAdapter(adapter);
                        addPostListener();
                        ((PostAdapter) list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void OnFailed() {
                        Log.w("DatabaseChange", "Failed to read values.");

                        // show message for user then reload connection
                        Utils.MyMessageButton("Read personal value has failed.", context);
                    }
                });
            }else{
                setContentView(R.layout.activity_main);
                list = findViewById(R.id.home_publications);
                adapter = new PostAdapter(context, listItems);
                list.setAdapter(adapter);
                addPostListener();
                ((PostAdapter) list.getAdapter()).notifyDataSetChanged();
            }
        }
    }

    private void addPostListener() {
        Log.d("[HOME_ACTIVITY]", "add post listener");

        data.child("Publications").addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        DatabaseReference publication = data.child("Publications").child(dataSnapshot.getKey());
                        publication.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot post) {
                                Log.d("[HOME_ACTIVITY]", "read a publication");
                                Log.d("[HOME_ACTIVITY]", post.toString());
                                if (post.child("groupe").getValue().toString().equals("public") &&
                                        !postAlreadyHere.contains(post.getKey())){
                                    date = post.child("date").getValue().toString();
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
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(firebaseUser != null) {
            menu.add(Menu.FIRST, ADD_POST, android.view.Menu.NONE, R.string.add_post).setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent i;
        switch (item.getItemId()) {
            case ADD_POST:
                i = new Intent(this, AddPost.class);
                i.putExtra("userId", firebaseUser.getUid());
                i.putExtra("groupId","public");
                startActivity(i);
            break;
            case R.id.action_open_menu:
                break;
            default:
        }
        return true;
    }
}
