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
import com.projet.musichall.R;
import com.projet.musichall.group.Post;
import com.projet.musichall.group.postAuthorListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/*
 * TODO Trier les publications par ordre décroissants de date
 * TODO Voir sur le post adapter comment on empêche de tout réafficher quand on add (sans doute override le notifyDataSetChanged)
 */


public class GroupWallFragment extends Fragment {
    ArrayList<Post> listItems = new ArrayList<>();
    PostAdapter adapter;
    ListView list;
    View current_view;
    private Map<String,Map<String,String>> posts;
    private String currentGroupId;
    private DatabaseReference data;
    private String authorName;
    private String date;
    private String text;
    private ArrayList<String> postAlreadyHere;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        current_view = inflater.inflate(R.layout.group_wall, container, false);
        return current_view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        currentGroupId = args.getString("groupId");
        Log.d("[ WALL_FRAGMENT ]", "groupId : "+currentGroupId);

        data = FirebaseDatabase.getInstance().getReference();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        list = current_view.findViewById(R.id.group_publications);
        adapter = new PostAdapter(getActivity(), listItems);

        postAlreadyHere = new ArrayList<>();


        addPostListener();

        list.setAdapter(adapter);

    }

    private void addPostListener(){
        if(currentGroupId != null) {
            DatabaseReference publications = data.child("Groupes").child(currentGroupId).child("publications");
            publications.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    DatabaseReference post =data.child("Publications").child(dataSnapshot.getValue().toString());
                    post.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot post) {
                            if (!postAlreadyHere.contains(post.getKey())){
                                postAlreadyHere.add(post.getKey());
                                //Log.d("[ WALL_DISPLAY_KEY ]", postAlreadyHere.toString());
                                date = (String) post.child("date").getValue();
                                String authorId = (String) post.child("author").getValue();
                                text = (String) post.child("contenu").getValue();
                                Log.d("[ WALL_ADAPTER ]", adapter.toString());
                                data.child("Users").child(authorId).addListenerForSingleValueEvent(new postAuthorListener(text, date, listItems, adapter));
                                //Log.d("[ WALL_DISPLAY ]", "date : " + date);
                                //Log.d("[ WALL_DISPLAY ]", "author : " + authorName);
                                //Log.d("[ WALL_DISPLAY ]", "text : " + text);
                                //Log.d("[ WALL_DISPLAY ]", "post : " + post );
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
    }
}
