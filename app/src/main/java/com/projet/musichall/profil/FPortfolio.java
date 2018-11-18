package com.projet.musichall.profil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projet.musichall.R;

import java.util.ArrayList;



public class FPortfolio extends Fragment {
    private RecyclerView portfolio;
    private RecyclerView.Adapter adapter_portfolio;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ArrayList<Bitmap> images = new ArrayList<Bitmap>();
        images.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image1), 480, 360, false));
        images.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image2), 480, 360, false));
        images.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.image3), 480, 360, false));
        images.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mountain_landscape), 480, 360, false));

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.portfolio, container, false);

        // meme chose avec le recycler view avec un layout manager en plus
        portfolio = (RecyclerView) rootView.findViewById(R.id.recycler);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.canScrollHorizontally();
        portfolio.setLayoutManager(layoutManager);
        adapter_portfolio = new MyRecyclerAdapter(images/*, new ArrayList<MediaStore.Video>(), new ArrayList<MediaActionSound>()*/);
        portfolio.setAdapter(adapter_portfolio);

        return rootView;
    }

}
