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
import com.projet.musichall.R;
import com.projet.musichall.group.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class GroupWallFragment extends Fragment {
    ArrayList<Post> listItems = new ArrayList<>();
    PostAdapter adapter;
    ListView list;
    View current_view;
    private Map<String,Map<String,String>> posts;
    private String currentGroupId;
    private DatabaseReference data;

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

        data = FirebaseDatabase.getInstance().getReference();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Occideretur pretioso pretioso oblato nec ferebatur contactus Honoratum Clematius inter.",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));

        list = current_view.findViewById(R.id.group_publications);
        adapter = new PostAdapter(getActivity(), listItems);
        list.setAdapter(adapter);


        addPostListener();

    }

    private void addPostListener(){
        if(currentGroupId != null) {
            DatabaseReference publications = data.child("Groupes").child(currentGroupId).child("publications");
            publications.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.d("[ WALL_DISPLAY ]", dataSnapshot.toString());
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
