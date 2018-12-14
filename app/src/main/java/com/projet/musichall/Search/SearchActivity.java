package com.projet.musichall.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;
import com.projet.musichall.ShouldLogin;

public class SearchActivity extends BaseActivity {
    FirebaseUser user;
    TextView textView;
    Button musiciens;
    Button groupes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        textView = (TextView) findViewById(R.id.text_recherche);
        musiciens = (Button) findViewById(R.id.buttonMusiciens);
        groupes = (Button) findViewById(R.id.buttonGroupes);
        user = FirebaseAuth.getInstance().getCurrentUser();


        musiciens.setOnClickListener(new View.OnClickListener() {

            @Override
           public void onClick(View v) {
                if (user != null) {
                    Intent intent = new Intent(SearchActivity.this, CriteresMusiciens.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SearchActivity.this, ShouldLogin.class);
                    startActivity(intent);
                }
            }
        });

        groupes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user != null){
                    Intent intent = new Intent(SearchActivity.this, CriteresGroupe.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SearchActivity.this, ShouldLogin.class);
                    startActivity(intent);
                }
            }
        });
    }

}
