package com.projet.musichall.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.google.firebase.database.DataSnapshot;
import com.projet.musichall.Search.ResultPresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddGroup extends BaseActivity {
    String userId;
    FirebaseUser user;
    DatabaseReference data;

    ArrayAdapter<String> listAdapter;
    ResultPresentation adapter;
    EditText editText;
    ListView peopleFoundList;

    String currentTarget;
    String groupName = "";
    Context context;

    ArrayList<String> listItems;
    ArrayList<String> listMembersIds;
    ArrayList<String> listItemsNoms;
    ArrayList<String> listItemsDates;
    ArrayList<String> listItemsInfos;
    ArrayList<String> ids;


    private boolean first = true;
    private boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_add_group);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMyActionBar().setTitle(R.string.add_group);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        listItems = new ArrayList<>();
        listMembersIds = new ArrayList<>();

        ListView membersList = findViewById(R.id.add_group_list);
        peopleFoundList = findViewById(R.id.listView);

        context = this;


        listAdapter = new ArrayAdapter<>(this,
                R.layout.group_add_list_item,
                listItems);
        membersList.setAdapter(listAdapter);

        // connexion a la base de donnee
        user = FirebaseAuth.getInstance().getCurrentUser();
        data = FirebaseDatabase.getInstance().getReference();

        addSearchMembersListener();
        addFoundMembersOnClickListener();
        addEditTextListener();
        addGroupNameListener();
        addCreateGroupButtonOnClick();
    }

    private void addFoundMembersOnClickListener() {
        peopleFoundList.setOnItemClickListener (new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            String key = adapter.getIds().get(position);
            String userName = adapter.getNoms().get(position);
            String userFirstName = userName.split(" ")[0];
            String userLastName = userName.split(" ")[1];

            currentTarget = key;
            listItems.add(userFirstName + " " + userLastName);
            listMembersIds.add(currentTarget);
            listAdapter.notifyDataSetChanged();
            editText.setText("");

            Toast.makeText(getApplicationContext(), userLastName,Toast.LENGTH_LONG).show();

        }
    });
    }

    /**
     * Recuperation des donnees de la bdd a chaque changement dans la base de donnee
     */
    private void addSearchMembersListener() {
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot users, info_current_user;
                if (user != null) {
                    // get first data to search users
                    info_current_user = dataSnapshot.child("Users").child(user.getUid()).child("info_recherche").child("utilisateur");
                    if (first) {
                        getDataFromFirebase(info_current_user);
                        first = false;
                    }

                    // set new adapter to result
                    listItemsNoms = new ArrayList<>();
                    listItemsDates = new ArrayList<>();
                    listItemsInfos = new ArrayList<>();
                    ids = new ArrayList<>();

                    // now apply search algorithm
                    users = dataSnapshot.child("Users");
                    getResultFromFirebase(users, info_current_user);

                    // make an array of the objects according to a layout design
                    adapter = new ResultPresentation(context, listItemsNoms, listItemsDates, listItemsInfos, ids);
                    // set the adapter for listview
                    peopleFoundList.setAdapter(adapter);

                    // allow to widget's listeners to modify firebase data
                    ok = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addEditTextListener() {
        editText = findViewById(R.id.add_group_edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (!first_entries[6]) {
                 **/if (user != null) {
                    data.child("Users").child(user.getUid()).child("info_recherche").child("utilisateur").child("nom").setValue(s.toString());
                }
                /*}
                first_entries[6] = false;*/
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

    private void addCreateGroupButtonOnClick(){
        Button createGroup = findViewById(R.id.group_add_button_create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groupName.equals("")) {
                    listMembersIds.add(userId);
                    DatabaseReference newGroup = data.child("Groupes").push();
                    Map<String, String> newMembers = new HashMap<>();
                    DatabaseReference memberRef;
                    for (int i = 0; i < listMembersIds.size(); i++) {
                        //memberRef = data.child("Membres").push();
                        //memberRef.setValue(listMembersIds.get(i));
                        //newMembers.put("member"+i, listMembersIds.get(i));
                        newGroup.child("membres").push().setValue(listMembersIds.get(i));
                    }

                    //newGroup.child("membres").setValue(newMembers);
                    newGroup.child("nom").setValue(groupName);

                    Intent result = new Intent();

                    result.putExtra("newGroupId", newGroup.getKey());
                    result.putExtra("newGroupName", groupName);
                    setResult(RESULT_OK, result);
                    AddGroup.this.finish();
                }
            }
        });
    }

    private void getDataFromFirebase(DataSnapshot current_user){
        if (current_user.child("nom").exists())
            editText.setText(String.valueOf(current_user.child("nom").getValue()));
    }

    private void getResultFromFirebase(DataSnapshot users, DataSnapshot current_user){
        List<String> sort_name = new ArrayList<>();   // sort results by priority
        DataSnapshot one_user;
        String sprenom1, snom1, sville1, smotivation1, sniveau1, resultat;
        boolean memeMusiqueJouee, memeMusiqueEcoutee, memeInstru;
        boolean cond_name, cond_with_spinner, cond_without_spinner;
        String sname, smotivation2, sniveau2, smusiqueEcoutee2, smusiqueJouee2, sinstru2;
        Iterator<DataSnapshot> it = users.getChildren().iterator();

        sname = String.valueOf(current_user.child("nom").getValue());
        smotivation2 = String.valueOf(current_user.child("motivation").getValue());
        sniveau2 = String.valueOf(current_user.child("niveau").getValue());
        smusiqueEcoutee2 = String.valueOf(current_user.child("musique_ecoutee").getValue());
        smusiqueJouee2 = String.valueOf(current_user.child("musique_jouee").getValue());
        sinstru2 = String.valueOf(current_user.child("instru").getValue());

        while (it.hasNext()) {
            one_user = it.next();

            // get user's data
            sprenom1 = String.valueOf(one_user.child("prenom").getValue());
            snom1 = String.valueOf(one_user.child("nom").getValue());
            sville1 = String.valueOf(one_user.child("ville").getValue());
            smotivation1 = String.valueOf(one_user.child("motivation").getValue());
            sniveau1 = String.valueOf(one_user.child("niveau").getValue());
            memeMusiqueEcoutee = (smusiqueEcoutee2.equals("Tout") || InChildrenValue(one_user.child("genreEcoute"), smusiqueEcoutee2));
            memeInstru = (sinstru2.equals("Tout") || InChildrenValue(one_user.child("instrus"), sinstru2));
            memeMusiqueJouee = (smusiqueJouee2.equals("Tout") || InChildrenValue(one_user.child("genreJoue"), smusiqueJouee2));

            cond_name = sprenom1.toLowerCase().matches(sname.toLowerCase()+"[a-zA-Z0-9]*") || snom1.toLowerCase().matches(sname.toLowerCase()+"[a-zA-Z0-9]*");
            cond_with_spinner = smotivation1.equals(smotivation2) && (sniveau2.equals("Tout") || sniveau1.equals(sniveau2)) && memeInstru && memeMusiqueEcoutee && memeMusiqueJouee;
            //cond_without_spinner = smotivation1.equals(smotivation2) && sniveau1.equals(sniveau2);

            // get user's information if match with search name
            if (cond_with_spinner && cond_name && !sname.isEmpty()) {
                ids.add(one_user.getKey());  // store the key to load selected user data later
                resultat = sprenom1 + " " + snom1;
                sort_name.add(resultat);
                resultat = "Membre depuis le " + String.valueOf(one_user.child("dateMembre").getValue());
                sort_name.add(resultat);
                resultat = smotivation1;
                resultat += " de niveau " + sniveau1;
                resultat += " habitant la ville de " + sville1;
                Log.d("INFO RECHERCHE", "Prenom : "+sprenom1+" Nom : "+snom1);
                sort_name.add(resultat);
            }
        }

        // get results by type
        for (int i = 0; i<sort_name.size(); i+=3) {
            listItemsNoms.add(sort_name.get(i));
            listItemsDates.add(sort_name.get(i+1));
            listItemsInfos.add(sort_name.get(i+2));
        }


        Log.d("INFO RECHERCHE", "Taille name : "+sort_name.size());
        Log.d("INFO RECHERCHE", "Taille name : "+listItemsNoms.size());
        Log.d("INFO RECHERCHE", "Taille dates: "+listItemsDates.size());
        Log.d("INFO RECHERCHE", "Taille infos: "+listItemsInfos.size());
    }

    // usefull function
    private boolean InChildrenValue(DataSnapshot listvalue, String value){
        Iterator<DataSnapshot> it = listvalue.getChildren().iterator();
        DataSnapshot ref;

        while (it.hasNext()) {
            ref = it.next();
            if (value.equals(ref.getValue()))
                return true;
        }

        return false;
    }


}
