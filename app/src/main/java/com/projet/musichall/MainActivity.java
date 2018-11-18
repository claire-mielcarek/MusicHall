package com.projet.musichall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.projet.musichall.group.GroupActivity;

public class MainActivity extends BaseActivity {
    Context context;
    private FirebaseAuth auth;
    private FirebaseUser user;

    /*
    String arrayName []= {

            "accueil",
            "groupe",
            "recherche",
            "discussion",
            "profil"
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);


        actionBar.setTitle(R.string.home);

        // recupere l'objet permettant de gerer l'authantification d'un utilisateur
        auth = FirebaseAuth.getInstance();
        // recupere l'utilisateur pour savoir si celui-ci est connecte
        user = auth.getCurrentUser();
        Log.d("[ TEST MENU ]", "MainActivity onCreated done");

        /*
        CircleMenu circleMenu = (CircleMenu)findViewById(R.id.circle_menu);
        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_menu,R.drawable.ic_cancel)
                .addSubMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_accueil)
                .addSubMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_groupe)
                .addSubMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_recherche)
                .addSubMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_discussion)
                .addSubMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_profil)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        Handler handler = new Handler();
                        switch(arrayName[index]){
                            case "profil":
                                handler = new Handler();
                                if (user != null)
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(context, ProfilActivity.class);
                                            startActivity(i);
                                        }
                                    }, 1000);
                                else{
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(context, Connexion.class);
                                            startActivity(i);
                                        }
                                    }, 1000);
                                }
                                break;
                            case "groupe":
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(MainActivity.this, GroupActivity.class);
                                        startActivity(i);
                                    }
                                }, 1000);
                                break;

                            case "recherche":
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(MainActivity.this, RechercheActivity.class);
                                        startActivity(i);
                                    }
                                }, 1000);
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Tu as choisi "+arrayName[index], Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                */
    }
/*
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_menu:
                CircleMenu c =findViewById(R.id.circle_menu);
                if(c.getVisibility() == View.INVISIBLE) {
                    c.setVisibility(View.VISIBLE);
                }
                else{
                    c.setVisibility(View.INVISIBLE);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */
}
