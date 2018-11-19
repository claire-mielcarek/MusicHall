package com.projet.musichall.group.wall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;

import java.util.Date;

public class AddPost extends BaseActivity {
    String userId;
    String groupId;
    EditText editText;
    String currentText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_wall_add_post);
        getMyActionBar().setTitle(R.string.group_page1_add_post);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        groupId = intent.getStringExtra("groupId");

        //Log.d("[ ADD_POST ]", "user : " + userId);
        //Log.d("[ ADD_POST ]", "group : " + groupId);

        addEditTextListener();
        addButtonOnClick();

    }

    private void addEditTextListener(){
        editText = findViewById(R.id.group_page1_add_post_edittext);
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

    private void addButtonOnClick(){
        Button addMember = findViewById(R.id.group_page1_add_post_create_post);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference data = FirebaseDatabase.getInstance().getReference();
                DatabaseReference newPublication = data.child("Publications").push();
                newPublication.child("contenu").setValue(currentText);
                newPublication.child("author").setValue(userId);
                newPublication.child("groupe").setValue(groupId);
                newPublication.child("type").setValue("text");
                newPublication.child("date").setValue(new Date().toString());

                DatabaseReference groupPublication = data.child("Groupes").child(groupId).child("publications").push();
                groupPublication.setValue(newPublication.getKey());

                Log.d("[ ADD_POST ]", "Post created");
                AddPost.this.finish();
            }
        });
    }
}
