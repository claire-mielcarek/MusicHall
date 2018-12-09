package com.projet.musichall.group.wall;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.PostAdapter;
import com.projet.musichall.R;
import com.projet.musichall.group.Post;
import com.projet.musichall.group.postAuthorListener;

import java.util.ArrayList;


public class GroupWallFragment extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        current_view = inflater.inflate(R.layout.group_wall, container, false);
        Log.d("[WALL_CREATE_VIEW]", "onCreate");
        isAlreadyInstantiated = false;
        postAlreadyHere = new ArrayList<>();
        listItems = new ArrayList<>();
        return current_view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAlreadyInstantiated = false;
        postAlreadyHere = new ArrayList<>();
        listItems = new ArrayList<>();
        Log.d("[WALL_CREATE]", "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("[WALL_FRAGMENT]", "onStart");

        if(!this.isAlreadyInstantiated) {
            this.isAlreadyInstantiated = true;

            if(this.currentGroupId == null) {
                Bundle args = getArguments();
                currentGroupId = args.getString("groupId");
                Log.d("[ WALL_FRAGMENT ]", "groupId : " + currentGroupId);
            }

            data = FirebaseDatabase.getInstance().getReference();


            list = current_view.findViewById(R.id.group_publications);
            adapter = new PostAdapter(getActivity(), listItems);

            publications = data.child("Groupes").child(currentGroupId).child("publications");
            addPostListener();

            list.setAdapter(adapter);
        }
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
