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

public class BaseActivity extends AppCompatActivity {

    ActionBar actionBar;

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



        Log.d("[ TEST MENU ]", "BaseActivity onCreated done");
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_menu:
                Intent i = new Intent(BaseActivity.this, com.projet.musichall.Menu.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    public ActionBar getMyActionBar() {
        return actionBar;
    }
}