package com.projet.musichall.group;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.PostAdapter;

import java.util.ArrayList;

public class postAuthorListener implements ValueEventListener {
    private String postText;
    private String postDate;
    private ArrayList<Post> posts;
    private ArrayList<String> postIds;
    private PostAdapter postAdapter;
    private String postId;

    public postAuthorListener(String postId, String postText, String postDate, ArrayList<Post> posts, PostAdapter postAdapter, ArrayList<String> postIds) {
        this.postText = postText;
        this.postDate = postDate;
        this.posts = posts;
        this.postAdapter = postAdapter;
        this.postIds = postIds;
        this.postId = postId;
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (!postIds.contains(postId)){
            postIds.add(postId);
            String authorName = (dataSnapshot.child("prenom").getValue()) + " " + (dataSnapshot.child("nom").getValue());
            Post newPost = new Post(authorName, postText, postDate);
            //Log.d("[ WALL_DISPLAY_POST ]", newPost.toString());
            posts.add(0, newPost);
            Log.d("[AUTH_LISTENER_ADAPTER]", "dataChanged");
            postAdapter.notifyDataSetChanged();
            Log.d("[AUTH_LISTENER_ADAPTER]", "post number : " +postAdapter.getCount());
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
}
