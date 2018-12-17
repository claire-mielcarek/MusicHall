package com.projet.musichall.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.projet.musichall.group.calendar.GroupCalendarFragment;
import com.projet.musichall.group.chat.GroupChatFragment;
import com.projet.musichall.group.wall.AddPost;
import com.projet.musichall.group.wall.GroupWallFragment;
import com.projet.musichall.profile.FragmentSlideAdapter;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class GroupActivity extends BaseActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ArrayList<String> groupsNames;
    private ArrayList<String> groupsIds;
    private String currentGroupId;
    private String currentGroupName;
    private DatabaseReference data;

    private ValueEventListener groupListener;
    private SampleFragmentPagerAdapter tabAdapter;
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
        setContentView(R.layout.you_have_to_be_logged);
        ActionBar myBar = getMyActionBar();
        myBar.setTitle(R.string.group);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            setContentView(R.layout.activity_group);

            tabId = WALL_ID;
            currentGroupId = null;
            groupsNames = new ArrayList<>();
            groupsIds = new ArrayList<>();

            viewPager = findViewById(R.id.viewpager);
            viewPager.setOffscreenPageLimit(3);

            data = FirebaseDatabase.getInstance().getReference();
            addGroupListener();

            // Get the ViewPager and set it's PagerAdapter so that it can display items
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
                if(this.currentGroupId != null) {
                    menu.add(Menu.NONE, WALL_ADD_POST, Menu.NONE, R.string.group_page1_add_post).setIcon(R.drawable.ic_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                }
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
                startActivityForResult(i, ADD_GROUP);
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
            case R.id.action_open_menu:
                break;
            default://Opening a group
                String tmpId = this.currentGroupId;
                String tmpName = this.currentGroupName;
                Log.d("[MENU]", "item id : " + item.getItemId());
                currentGroupId = this.groupsIds.remove(item.getItemId()-FIRST_GROUP_ID);
                currentGroupName = this.groupsNames.remove(item.getItemId()-FIRST_GROUP_ID);
                this.groupsIds.add(tmpId);
                this.groupsNames.add(tmpName);

                Log.d("[FRAGMENTS]", getSupportFragmentManager().getFragments().toString());
                for (Fragment f : getSupportFragmentManager().getFragments() ) {
                    if (f != null) {
                        if (f instanceof GroupWallFragment) {
                            Log.d("[FRAG]", "WALL");
                            GroupWallFragment wallF = (GroupWallFragment) f;
                            wallF.setCurrentGroupId(currentGroupId);
                        } else if (f instanceof GroupChatFragment) {
                            Log.d("[FRAG]", "CHAT");
                            GroupChatFragment chatF = (GroupChatFragment) f;
                            chatF.setCurrentgroupId(currentGroupId);
                        } else if(f instanceof GroupCalendarFragment){
                            Log.d("[FRAG]", "CALENDAR");
                            GroupCalendarFragment calF = (GroupCalendarFragment)f;
                            calF.setCurrentgroupId(currentGroupId);
                        }
                    }
                }
                Log.d("[CHANGE_GROUP]", " coucou ");
                getMyActionBar().setTitle(currentGroupName);
                invalidateOptionsMenu();
        }
        return true;
    }

    private void addGroupListener(){

        groupListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("[GROUP_LISTENER]", "data change");
                String userId = dataSnapshot.child("Users").child(user.getUid()).getKey();
                DataSnapshot groupsData = dataSnapshot.child("Groupes");
                boolean isUserGroup = false;
                for (DataSnapshot group: groupsData.getChildren()){
                    if (!groupsIds.contains(group.getKey()) && !group.getKey().equals(currentGroupId)) {
                        for (DataSnapshot member : group.child("membres").getChildren()) {
                            if (userId.equals(member.getValue())) {
                                isUserGroup = true;
                            }
                        }
                        if (isUserGroup) {
                            if(currentGroupId == null){
                                currentGroupId = group.getKey();
                                currentGroupName = (String) group.child("nom").getValue();
                                tabAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(),GroupActivity.this, currentGroupId);
                                viewPager.setAdapter(tabAdapter);
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
        };
        data.addValueEventListener(this.groupListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("[GROUP]", "onActivityResult");
        if (requestCode == ADD_GROUP){
            if(resultCode == Activity.RESULT_OK){
                String id = data.getStringExtra("newGroupId");
                String name = data.getStringExtra("newGroupName");
                groupsNames.add(name);
                groupsIds.add(id);
                Log.d("[GROUP_ADDED]", id);
                Log.d("[GROUP_ADDED]", name);
                invalidateOptionsMenu();
            }
        }
    }
}