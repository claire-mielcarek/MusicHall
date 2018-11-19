package com.projet.musichall.Search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;

public class SearchActivity extends BaseActivity {
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


        musiciens.setOnClickListener(new View.OnClickListener() {

            @Override
           public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CriteresMusiciens.class);
                startActivity(intent);
            }
        });

        groupes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, CriteresGroupe.class);
                startActivity(intent);
            }
        });


    }

}
