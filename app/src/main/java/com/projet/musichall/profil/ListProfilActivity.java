package com.projet.musichall.profil;

import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




public class ListProfilActivity extends BaseActivity {
    private String[] INSTRU;
    private String[] GENRE;
    private Context context;
    private DatabaseReference database;
    private FirebaseUser user;
    private int type_liste;
    private ListView list;
    private MyListAdapter adapter;
    private String select_element;
    private List<String> keys;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_profil);
        type_liste = getIntent().getIntExtra("type_liste",1);
        context = this;
        actionBar.setTitle(R.string.profile);
    }

    @Override
    protected void onStart() {
        super.onStart();
        INSTRU = getResources().getStringArray(R.array.instruments);
        GENRE = getResources().getStringArray(R.array.genres);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        list = findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // proposer la suppression de l'element
                MyDeleteButton(position);
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot ref = null;
                List<String> newlist = new ArrayList<>();
                Iterator<DataSnapshot> it;

                if (type_liste == 1)
                    ref = dataSnapshot.child("Users").child(user.getUid()).child("instrus");
                else if (type_liste == 2)
                    ref = dataSnapshot.child("Users").child(user.getUid()).child("genreEcoute");
                else if (type_liste == 3)
                    ref = dataSnapshot.child("Users").child(user.getUid()).child("genreJoue");

                if (ref != null && ref.exists()) {
                    it = ref.getChildren().iterator();
                    keys = new ArrayList<String>();
                    while (it.hasNext()){
                        ref = it.next();
                        newlist.add((String) ref.getValue());
                        keys.add(ref.getKey());
                    }

                    adapter = new MyListAdapter(context, newlist);
                    list.setAdapter(adapter);
                    ((MyListAdapter) list.getAdapter()).notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DatabaseChange", "Failed to read values.", databaseError.toException());
            }
        });

        // bouton permettant d'ajouter un element
        ImageButton add = findViewById(R.id.add_el);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = null;

                if (type_liste == 1)
                    ref = database.child("Users").child(user.getUid()).child("instrus");
                else if (type_liste == 2)
                    ref = database.child("Users").child(user.getUid()).child("genreEcoute");
                else if (type_liste == 3)
                    ref = database.child("Users").child(user.getUid()).child("genreJoue");

                if (ref != null) {
                    MyButtonAdd(ref);
                }
            }
        });
    }


    public void MyDeleteButton(final int position) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setTitle("Voulez-vous vraiment supprimer cet élément?");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);

        dlgAlert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference ref = null;

                if (type_liste == 1)
                    ref = database.child("Users").child(user.getUid()).child("instrus");
                else if (type_liste == 2)
                    ref = database.child("Users").child(user.getUid()).child("genreEcoute");
                else if (type_liste == 3)
                    ref = database.child("Users").child(user.getUid()).child("genreJoue");

                if (ref != null) {
                    ref.child(keys.get(position)).removeValue();
                }
            }
        });

        dlgAlert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dlgAlert.create().show();
    }


    public void MyButtonAdd(final DatabaseReference refData) {
        final AlertDialog box;
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setTitle("Liste des instruments");
        dlgAlert.setIcon(android.R.drawable.ic_menu_add);

        if (type_liste == 1) {
            select_element = INSTRU[0];
            dlgAlert.setSingleChoiceItems(INSTRU, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    select_element = INSTRU[which];
                }
            });
        }else{
            select_element = GENRE[0];
            dlgAlert.setSingleChoiceItems(GENRE, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    select_element = GENRE[which];
                }
            });
        }


        dlgAlert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                refData.push().setValue(select_element);
            }
        });
        dlgAlert.create().show();
    }

}
