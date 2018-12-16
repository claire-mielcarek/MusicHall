package com.projet.musichall.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projet.musichall.R;
import com.projet.musichall.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;



public class FPortfolio extends Fragment implements IChangeUserData {
    public static final int RC_IMAGE = 50;
    public static final int RC_VIDEO = 51;
    public static final int RC_SON = 52;
    private User user;
    private View root;
    private ImageButton add_multimedia;
    private GridView portfolio;
    private MyGridAdapter adapter_portfolio;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private StorageReference storageReference;


    public static FPortfolio newInstance() {
        return new FPortfolio();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.portfolio, container, false);

        // meme chose avec le recycler view avec un layout manager en plus
        portfolio = (GridView) rootView.findViewById(R.id.grid);

        root = rootView;
        user = User.getCurrentUser();
        user.addCallback(this, 0);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        add_multimedia = root.findViewById(R.id.add_mult);
        add_multimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyButtonAdd("Quel type de contenu voulez-vous ajouter?");
            }
        });

        // get firebase reference
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        // fill data for portfolio
        fillDataFromUser();

        // implement to watch an image
        portfolio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                watchImage(user.getPathImages().get(position));
            }
        });

        // implement to delete an image
        portfolio.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MyRemoveButton(position);
                return true;
            }
        });
    }

    private void fillDataFromUser(){
        // set a new adapter
        adapter_portfolio = new MyGridAdapter(getContext(), user.getImages()/*, videos, sons*/);
        portfolio.setAdapter(adapter_portfolio);
    }


    private void watchImage(String name){
        String path = "images/portfolio/"+firebaseUser.getUid()+"/"+name;
        Intent i = new Intent(getContext(), ImageWatcher.class);
        i.putExtra("path", path);

        startActivity(i);
    }

    public void MyButtonAdd(String titre) {
        final String[] choix = new String[]{"Image", "Vidéo", "Son"};
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        dlgAlert.setTitle(titre);
        dlgAlert.setIcon(android.R.drawable.ic_menu_add);

        dlgAlert.setItems(choix, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i;
                switch (which) {
                    case 0:
                        i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RC_IMAGE);
                        break;
                    case 1:
                        i = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RC_VIDEO);
                        break;
                    case 2:
                        i = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);  // Audio.Media.EXTERNAL_CONTENT_URI
                        startActivityForResult(i, RC_SON);
                        break;
                }
            }
        });

        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void MyRemoveButton(final int position) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        dlgAlert.setTitle("Do you really want to delete this image?");
        dlgAlert.setIcon(android.R.drawable.ic_dialog_alert);

        dlgAlert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tmp_name;
                List<String> paths;
                final DatabaseReference ref;
                StorageReference store;

                // change local data
                ((MyGridAdapter) portfolio.getAdapter()).getImages().remove(position);
                ((BaseAdapter) portfolio.getAdapter()).notifyDataSetChanged();

                // change data on firebase
                ref = database.child("Users").child(firebaseUser.getUid()).child("portfolio").child("images");
                store = storageReference.child("images/portfolio/").child(firebaseUser.getUid());
                if (ref.getKey() != null) {
                    tmp_name = user.getPathImages().get(position);
                    ref.child(user.getKeyImages().get(position)).removeValue();
                    user.getKeyImages().remove(position);
                    user.getPathImages().remove(position);
                    if (!user.getPathImages().contains(tmp_name)){
                        store.child(tmp_name).delete();
                    }
                }
            }
        });

        dlgAlert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dlgAlert.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_IMAGE && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try{
                Bitmap result = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                UploadPortfolioImages(result, new File(uri.getPath()).getName());   // send file to Firebase
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }/*else if (requestCode == RC_VIDEO && resultCode == Activity.RESULT_OK){

        }*/
    }

    private void UploadPortfolioImages(Bitmap bmp, final String name){    // add image to user's portfolio
        byte[] data;
        final Bitmap bmp_tmp;
        ByteArrayOutputStream arrayData;
        StorageReference ref;

        ref = storageReference.child("images/portfolio/"+firebaseUser.getUid()+"/"+name);

        bmp_tmp = Bitmap.createScaledBitmap(bmp, User.WIDTH_IMAGE_HIGH, User.HEIGHT_IMAGE_HIGH, false);
        arrayData = new ByteArrayOutputStream();
        bmp_tmp.compress(Bitmap.CompressFormat.JPEG, 100, arrayData);
        data = arrayData.toByteArray();
        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Bitmap imageToShow;
                database.child("Users").child(firebaseUser.getUid()).child("portfolio").child("images").push().setValue(name);
                imageToShow = Bitmap.createScaledBitmap(bmp_tmp, User.WIDTH_IMAGE_LOW, User.HEIGHT_IMAGE_LOW, false);
                user.getImages().add(imageToShow);    // add image to user data and grid view at the same time
                //((MyGridAdapter) portfolio.getAdapter()).getImages().add(bmp_tmp);
                ((MyGridAdapter) portfolio.getAdapter()).notifyDataSetChanged();   // refresh the adapter
                user.getPathImages().add(name);
                Log.d("SET PORTFOLIO IMAGE", "Set image portfolio : success  nombre bytes : "+taskSnapshot.getTotalByteCount());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w("SET PORTFOLIO IMAGE", "Set image portfolio : failed");
            }
        });
    }

    @Override
    public void ChangeData() {

    }
}

