package com.projet.musichall;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

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

                    }
                });
    }
}
