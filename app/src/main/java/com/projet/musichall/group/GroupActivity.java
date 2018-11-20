package com.projet.musichall.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.projet.musichall.group.wall.AddPost;
import com.projet.musichall.profil.FragmentSlideAdapter;

import java.util.ArrayList;
import java.util.Map;

public class GroupActivity extends BaseActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ArrayList<String> groupsNames;
    private ArrayList<String> groupsIds;
    private String currentGroupId;
    private String currentGroupName;
    private DatabaseReference data;

    private PagerAdapter tabAdapter;
    private ViewPager viewPager;

    //Ids of the tab
    private int tabId;
    final private int WALL_ID = 0;
    final private int CHAT_ID = 1;
    final private int CALENDAR_ID = 2;

    //Ids of the menu items
    final private int ADD_GROUP = 3;
    final private int WALL_ADD_POST = 4;

    final private int FIRST_GROUP_ID = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ActionBar myBar = getMyActionBar();
        myBar.setTitle(R.string.group);

        tabId = WALL_ID;
        currentGroupId = null;
        groupsNames = new ArrayList<>();
        groupsIds = new ArrayList<>();


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        data = FirebaseDatabase.getInstance().getReference();
        addGroupListener();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = findViewById(R.id.viewpager);
        tabAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(),GroupActivity.this);
        viewPager.setAdapter(tabAdapter);
        //Detect when the current Fragment changed
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if(position != tabId){
                    tabId = position;
                    invalidateOptionsMenu();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int id =FIRST_GROUP_ID;
        for(String groupName : groupsNames){
            menu.add(Menu.NONE, id, Menu.NONE, groupName).setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            id++;
        }
        menu.add(Menu.NONE, ADD_GROUP, Menu.NONE, R.string.add_group).setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        switch(tabId){
            case WALL_ID:
                menu.add(Menu.NONE, WALL_ADD_POST, Menu.NONE, R.string.group_page1_add_post).setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                break;
            case CALENDAR_ID:
                break;
            case CHAT_ID:
                break;
            default:

        }
        //Log.d("[ ADD_MENU_ICON_FRAG ]", "menu updated");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent i;
        switch (item.getItemId()) {
            case ADD_GROUP: //Adding a group
                i = new Intent(GroupActivity.this, AddGroup.class);
                i.putExtra("userId", user.getUid());
                startActivity(i);
                break;
            case WALL_ADD_POST:
                if(currentGroupId != null) {
                    i = new Intent(GroupActivity.this, AddPost.class);
                    i.putExtra("userId", user.getUid());
                    i.putExtra("groupId", currentGroupId);
                    startActivity(i);
                }
                else{
                    Log.d("[ ADD_POST ]", "Failed : there is no group");
                }

                break;
            default: //Opening a group
        }
        return true;
    }

    private void addGroupListener(){

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String userId = dataSnapshot.child("Users").child(user.getUid()).getKey();
                DataSnapshot groupsData = dataSnapshot.child("Groupes");
                boolean isUserGroup = false;
                for (DataSnapshot group: groupsData.getChildren()){
                    if (!groupsIds.contains(group.getKey())) {
                        for (DataSnapshot member : group.child("membres").getChildren()) {
                            if (userId.equals(member.getValue())) {
                                isUserGroup = true;
                            }
                        }
                        if (isUserGroup) {
                            if(currentGroupId == null){
                                currentGroupId = group.getKey();
                                currentGroupName = (String) group.child("nom").getValue();
                                getMyActionBar().setTitle(currentGroupName);
                            }
                            else {
                                groupsNames.add((String) group.child("nom").getValue());
                                groupsIds.add(group.getKey());
                            }
                            isUserGroup = false;
                        }
                    }
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("[ DATABASE ]", "Failed to read value.", error.toException());
            }
        });
    }
}