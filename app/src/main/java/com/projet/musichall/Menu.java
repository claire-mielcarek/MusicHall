package com.projet.musichall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;
import com.projet.musichall.Search.SearchActivity;
import com.projet.musichall.discussion.DiscussionActivity;
import com.projet.musichall.group.GroupActivity;
import com.projet.musichall.login.Connexion;
import com.projet.musichall.profile.ProfilActivity;


public class Menu extends AppCompatActivity {
    private final String arrayName[] = {
            "accueil",
            "groupe",
            "recherche",
            "discussion",
            "profil"
    };
    private FirebaseAuth auth;
    private FirebaseUser user;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }


    @Override
    public void onStart() {
        super.onStart();

    // recupere l'objet permettant de gerer l'authantification d'un utilisateur
        auth = FirebaseAuth.getInstance();
        // recupere l'utilisateur pour savoir si celui-ci est connecte
        user = auth.getCurrentUser();

        android.support.v7.app.ActionBar a = getSupportActionBar();
        a.hide();


        CircleMenu menu = findViewById(R.id.circle_menu);

        menu.openMenu();
        menu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {
            @Override
            public void onMenuOpened() {
            }

            @Override
            public void onMenuClosed() {
                Menu.this.finish();
            }
        });
        menu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_menu,R.drawable.ic_cancel)
                .addSubMenu(Color.parseColor("#6083DA"),R.drawable.ic_accueil)
                .addSubMenu(Color.parseColor("#FF5F8E38"),R.drawable.ic_groupe)
                .addSubMenu(Color.parseColor("#FFFFCC00"),R.drawable.ic_recherche)
                .addSubMenu(Color.parseColor("#855b3a"),R.drawable.ic_discussion)
                .addSubMenu(Color.parseColor("#ff70a0"),R.drawable.ic_profil)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        Handler handler;
                        switch(arrayName[index]) {
                            case "profil":
                                handler = new Handler();
                                if (user != null){
                                    //launch_sound(R.raw.drum);
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(Menu.this, ProfilActivity.class);
                                            Menu.this.finish();
                                            startActivity(i);
                                        }
                                    }, 1000);
                                }else{
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(Menu.this, Connexion.class);
                                            Menu.this.finish();
                                            startActivity(i);
                                        }
                                    }, 1000);
                                }
                                break;
                            case "groupe":
                                if (user != null) {
                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(Menu.this, GroupActivity.class);
                                            Menu.this.finish();
                                            startActivity(i);
                                        }
                                    }, 1000);
                                }
                                else{
                                    handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent i = new Intent(Menu.this, ShouldLogin.class);
                                            getIntent().putExtra("title", R.string.group);
                                            Menu.this.finish();
                                            startActivity(i);
                                        }
                                    }, 1000);

                                }
                                break;
                            case "accueil":
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(Menu.this, MainActivity.class);
                                        Menu.this.finish();
                                        startActivity(i);
                                    }
                                }, 1000);
                                break;
                            case "recherche":
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(Menu.this, SearchActivity.class);
                                        Menu.this.finish();
                                        startActivity(i);
                                    }
                                }, 1000);
                                break;
                            case "discussion":
                                handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(Menu.this, DiscussionActivity.class);
                                        Menu.this.finish();
                                        startActivity(i);
                                    }
                                }, 1000);
                                break;
                            default:
                                //Toast.makeText(Menu.this, "Tu as choisi "+arrayName[index], Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
    }

    public void launch_sound(int idRes){
        SoundPool soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        soundPool.load(this, idRes, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1.f, 1.f, 1, 0, 1.0f);
            }
        });
    }
}
