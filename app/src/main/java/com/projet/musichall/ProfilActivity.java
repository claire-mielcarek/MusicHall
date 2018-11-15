package com.projet.musichall;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;



public class ProfilActivity extends AppCompatActivity {
    /*private PrivateProfil public_profile;
    private PublicProfil private_profile;
    private PublicProfil portfolio;*/
    private boolean portfolio_affiche = true;
    private ViewPager pager;
    private PagerAdapter adapter;
    private RelativeLayout f_portfolio;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profil);

        // recupere le viewpager et lui attribut un nouvel adapteur
        pager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new FragmentSlideAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        // recupere le container du fragment
        f_portfolio = findViewById(R.id.frag_portfolio);
    }


    public void portfolio_manager(View v){

        if (portfolio_affiche){
            collapse(f_portfolio);
            ((ImageButton) v).setImageResource(android.R.drawable.ic_menu_more);
        }else{
            expand(f_portfolio);
            ((ImageButton) v).setImageResource(android.R.drawable.ic_input_add);
        }

        portfolio_affiche = !portfolio_affiche;
    }

    public void expand(final View v) {
        /*ObjectAnimator animation = ObjectAnimator.ofFloat(v, "translationY", 0);
        animation.setDuration(1000);
        animation.start();

        v.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        v.requestLayout();*/

        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1? ViewGroup.LayoutParams.WRAP_CONTENT: (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(2000/*(int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density)*/);
        v.startAnimation(a);
    }

    public void collapse(final View v) {
        /*View text = v.findViewById(R.id.titre_folio);
        int target = v.getMeasuredHeight() - text.getMeasuredHeight()- (int) (10*getResources().getDisplayMetrics().density+0.5f);

        ObjectAnimator animation = ObjectAnimator.ofFloat(v, "translationY", target);
        animation.setDuration(1000);
        animation.start();

        v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (40 * getResources().getDisplayMetrics().density + 0.5f)));
        v.requestLayout();*/

        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    //v.setVisibility(View.GONE);
                    v.getLayoutParams().height = 100;
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(2000);    // (int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density)
        v.startAnimation(a);
    }
}

