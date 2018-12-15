package com.projet.musichall;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {

    public ActionBar actionBar;
    /*private FirebaseUser baseUser;
    private FirebaseAuth baseAuth;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar);
        actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.d("[ TEST MENU ]", "Ya rien dans actionBar");
        }
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setIcon(R.drawable.ic_menu);
        actionBar.setTitle(R.string.app_name);

        /*baseAuth = FirebaseAuth.getInstance();
        baseUser = baseAuth.getCurrentUser();*/

        Log.d("[ TEST MENU ]", "BaseActivity onCreated done");
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // shown a different icon if online or offline
        /*if (baseUser != null) {
            menu.getItem(1).setIcon(android.R.drawable.presence_online);
        }else{
            menu.getItem(1).setIcon(android.R.drawable.presence_offline);
        }*/

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_open_menu:
                i = new Intent(BaseActivity.this, com.projet.musichall.Menu.class);
                startActivity(i);
                break;
            /*case R.id.action_logout:
                if (baseUser != null){
                    baseAuth.signOut();
                    i = new Intent(BaseActivity.this, com.projet.musichall.MainActivity.class);
                    startActivity(i);
                }else{
                    i = new Intent(BaseActivity.this, com.projet.musichall.login.Connexion.class);
                    startActivity(i);
                }
                break;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    public ActionBar getMyActionBar() {
        return actionBar;
    }
}