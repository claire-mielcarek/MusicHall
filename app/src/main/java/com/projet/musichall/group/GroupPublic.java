package com.projet.musichall.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.PostAdapter;
import com.projet.musichall.R;

import java.util.ArrayList;

public class GroupPublic extends BaseActivity {
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
        setContentView(R.layout.group_wall);

        isAlreadyInstantiated = false;
        postAlreadyHere = new ArrayList<>();
        listItems = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent i = getIntent();
        String name = i.getStringExtra("nom");
        currentGroupId = i.getStringExtra("id");

        actionBar.setTitle(name);

        data = FirebaseDatabase.getInstance().getReference();

        list = findViewById(R.id.group_publications);
        adapter = new PostAdapter(this, listItems);

        publications = data.child("Groupes").child(currentGroupId).child("publications");
        addPostListener();

        list.setAdapter(adapter);
    }


    private void addPostListener(){
        Log.d("[ ADD_LISTENER ]", "listener added : normally this printed just once");
        if(currentGroupId != null) {
            publicationsListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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
            };
            publications.addChildEventListener(publicationsListener);
        }
    }

    public void setCurrentGroupId(String currentGroupId) {
        this.currentGroupId = currentGroupId;
        Log.d("[FRAGMENT_WALL]", "Id changed : "+this.currentGroupId);
        publications.removeEventListener(publicationsListener);
        publications = data.child("Groupes").child(currentGroupId).child("publications");
        postAlreadyHere.clear();
        listItems.clear();
        adapter.notifyDataSetChanged();
        addPostListener();
    }
}
