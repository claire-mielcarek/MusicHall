package com.projet.musichall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;



public class MainActivity extends AppCompatActivity {
    Context context;
    private FirebaseAuth auth;
    private FirebaseUser user;

    String arrayName []= {
            "accueil",
            "groupe",
            "recherche",
            "discussion",
            "profil"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        // recupere l'objet permettant de gerer l'authantification d'un utilisateur
        auth = FirebaseAuth.getInstance();
        // recupere l'utilisateur pour savoir si celui-ci est connecte
        user = auth.getCurrentUser();

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
                        Toast.makeText(MainActivity.this, "Tu as choisi "+arrayName[index], Toast.LENGTH_SHORT).show();
                        switch(index){
                            case 4:
                                if (user != null)
                                    startActivity(new Intent(context, ProfilActivity.class));
                                else startActivity(new Intent(context, Connexion.class));
                                break;
                            default:
                                break;
                        }
                    }
                });
    }
}
