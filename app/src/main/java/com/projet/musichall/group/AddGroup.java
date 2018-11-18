package com.projet.musichall.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddGroup extends BaseActivity {
    String user;
    ArrayAdapter<String> listAdapter;
    ArrayList<String> listItems;
    ArrayList<String> listMembersIds;
    DatabaseReference data;
    String currentTarget;
    String currentName;
    String currentFirstName;
    EditText editText;
    String currentText;
    String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_add_group);

        getMyActionBar().setTitle(R.string.add_group);

        Intent intent = getIntent();
        user = intent.getStringExtra("userId");
        listItems = new ArrayList<>();
        listMembersIds = new ArrayList<>();
        ListView list = findViewById(R.id.add_group_list);
        listAdapter = new ArrayAdapter<>(this,
                R.layout.group_add_list_item,
                listItems);
        list.setAdapter(listAdapter);

        data = FirebaseDatabase.getInstance().getReference();

        addEditTextListener();
        addGroupNameListener();
        addMemberButtonOnClick();
        addCreateGroupButtonOnClick();
    }

    private void addEditTextListener() {
        editText = findViewById(R.id.add_group_edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentText = editText.getText().toString();
            }
        });
    }

    private void addGroupNameListener(){
        final EditText editTextName = findViewById(R.id.add_group_name);
        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                groupName = editTextName.getText().toString();
            }
        });
    }

    private void addMemberButtonOnClick(){
        Button addMember = findViewById(R.id.group_add_button);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] splitStr = currentText.split("\\s+");
                currentFirstName = splitStr[0];
                currentName = splitStr[1];

                addMember();
            }
        });
    }

    private void addMember() {
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DataSnapshot users = dataSnapshot.child("Users");
                String name;
                String firstName;
                for (DataSnapshot user : users.getChildren()) {
                    name = (String) user.child("nom").getValue();
                    firstName = (String)user.child("prenom").getValue();
                    if (name != null && firstName != null && !listMembersIds.contains(user.getKey())) {
                        Log.d("[ ADD_MEMBER_TO_GROUP ]", user.toString());
                        Log.d("[ ADD_MEMBER_TO_GROUP ]", name);
                        Log.d("[ ADD_MEMBER_TO_GROUP ]", firstName);
                        if (name.equals(currentName) && firstName.equals(currentFirstName)) {
                            currentTarget = user.getKey();
                            listItems.add(currentFirstName + " " + currentName);
                            listMembersIds.add(currentTarget);
                            listAdapter.notifyDataSetChanged();
                            Log.d("[ MEMBER_ADDED ]", listItems.toString());
                            Log.d("[ MEMBER_ADDED ]", listMembersIds.toString());
                        }
                    }
                }
                editText.setText("");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("[ GROUP_ADD ]", "Failed to read value.", error.toException());
            }
        });
    }

    private void addCreateGroupButtonOnClick(){
        Button createGroup = findViewById(R.id.group_add_button_create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listMembersIds.add(user);
                DatabaseReference newGroup = data.child("Groupes").push();
                Map<String, String> newMembers= new HashMap<>();
                DatabaseReference memberRef ;
                for(int i=0; i<listMembersIds.size(); i++){
                    //memberRef = data.child("Membres").push();
                    //memberRef.setValue(listMembersIds.get(i));
                    //newMembers.put("member"+i, listMembersIds.get(i));
                    newGroup.child("membres").push().setValue(listMembersIds.get(i));
                }

                //newGroup.child("membres").setValue(newMembers);
                newGroup.child("nom").setValue(groupName);
                AddGroup.this.finish();
            }
        });
    }
}
