package com.projet.musichall.group;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.group.wall.PostAdapter;

import java.util.ArrayList;

public class postAuthorListener implements ValueEventListener {
    private String postText;
    private String postDate;
    private ArrayList<Post> posts;
    private PostAdapter postAdapter;

    public postAuthorListener(String postText, String postDate, ArrayList<Post> posts, PostAdapter postAdapter) {
        this.postText = postText;
        this.postDate = postDate;
        this.posts = posts;
        this.postAdapter = postAdapter;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String authorName = (dataSnapshot.child("prenom").getValue()) + " " + (dataSnapshot.child("nom").getValue());
        Post newPost = new Post(authorName, postText, postDate);
        Log.d("[ WALL_DISPLAY_POST ]", newPost.toString());
        posts.add(newPost);
        //Log.d("[ AUTHOR_LISTENER_ADAPTER ]", )
        postAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
