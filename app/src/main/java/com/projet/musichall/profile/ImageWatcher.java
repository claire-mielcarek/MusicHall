package com.projet.musichall.profile;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.projet.musichall.BaseActivity;
import com.projet.musichall.R;

import java.io.File;
import java.io.IOException;




public class ImageWatcher extends BaseActivity{
    private Context context;
    private ImageView watcher;
    private int height, width;
    private boolean isLandscapeMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);
        context = this;
        isLandscapeMode = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        final File localFile;
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        Intent intent;
        final String path;

        Log.d("GET PORTFOLIO IMAGE", "Density : "+getResources().getDisplayMetrics().density);
        if (isLandscapeMode) {
            width = (int) (User.HEIGHT_IMAGE_HIGH * 1.3f * getResources().getDisplayMetrics().density);
            height = (int) (User.WIDTH_IMAGE_HIGH * getResources().getDisplayMetrics().density);
        }else {
            height = (int) (User.HEIGHT_IMAGE_HIGH * getResources().getDisplayMetrics().density);
            width = (int) (User.WIDTH_IMAGE_HIGH * getResources().getDisplayMetrics().density);
        }

        intent = getIntent();

        if (intent != null) {
            path = intent.getStringExtra("path");
            storage = storage.child(path);
            try {
                localFile = File.createTempFile("images", "jpg");
                storage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("GET PORTFOLIO IMAGE", "Get portfolio image : success");
                        Bitmap bmp = BitmapFactory.decodeFile(localFile.getPath());
                        bmp = Bitmap.createScaledBitmap(bmp, width, height, false);
                        // show the image
                        setContentView(R.layout.image_watcher);
                        watcher = findViewById(R.id.image_to_watch);
                        watcher.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("GET PORTFOLIO IMAGE", "Get portfolio image : failed " + exception.getMessage());
                        Log.d("GET PORTFOLIO IMAGE", "Path :  " + path);
                        finish();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                finish();
            }
        }else{
            finish();
        }

    }


}
