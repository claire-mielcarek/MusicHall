package com.projet.musichall.discussion;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.projet.musichall.profile.IChangeUserData;
import com.projet.musichall.profile.IResultConnectUser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OtherUser {
    // admin data
    private String uid;
    private boolean loadingFinished;
    private boolean first;

    // Firebase reference
    private DatabaseReference database;
    private StorageReference storage;

    // public data
    private String nom;
    private String prenom;
    private Bitmap avatar;
    private String ville;
    private String genre;
    private int niveau;
    private String nameNiveau;
    private String motivation;

    // List item
    private List<String> instruments;
    private List<String> listened_styles;
    private List<String> played_styles;

    // portfolio
    private List<String> pathImages;
    private List<Bitmap> images;
    private List<String> pathVideos;
    //private List<Bitmap> videos;
    private List<String> pathSounds;
    //private List<Bitmap> sounds;


    // private constructor only one user in memory
    public OtherUser(String uid){

        // connect to firebase
        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();

        // init data
        this.uid = uid;
        loadingFinished = false;
        first = true;
        niveau = 0;

        // init list
        instruments = new ArrayList<>();
        listened_styles = new ArrayList<>();
        played_styles = new ArrayList<>();
        pathImages = new ArrayList<>();
        images = new ArrayList<>();
        pathVideos = new ArrayList<>();
        // videos = new ArrayList<>();
        pathSounds = new ArrayList<>();
        //sounds = new ArrayList<>();
    }

    // attach User to Firebase which fills data on runtime
    public void attachUserToFirebase(final boolean one_time, final IResultConnectUser interfaceForResult){

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get user node
                if (!one_time||first) {
                    DataSnapshot dataUser = dataSnapshot.child("Users").child(uid);
                    // get public user data
                    fillPublicDataFromFirebase(dataUser);
                    // get instruments list
                    instruments = fillDataListFromFirebase(dataUser, 1);
                    Log.d("GET DATA USER", "List intruments OK");
                    // get played style list
                    listened_styles = fillDataListFromFirebase(dataUser, 2);
                    Log.d("GET DATA USER", "List listened style OK");
                    // get listened style list
                    played_styles = fillDataListFromFirebase(dataUser, 3);
                    Log.d("GET DATA USER", "List played style OK");
                    // get user's portfolio
                    fillPortfolioImagesFromFirebase(dataUser, interfaceForResult);

                    // this is no more the first time
                    first = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DatabaseChange", "Failed to read values.", databaseError.toException());
            }
        });
    }

    // get public data from firebase
    private void fillPublicDataFromFirebase(DataSnapshot dataUser){
        String valprenom = (String) dataUser.child("prenom").getValue();
        String valnom = (String) dataUser.child("nom").getValue();
        String valville = (String) dataUser.child("ville").getValue();
        String valgenre = (String) dataUser.child("genre").getValue();
        String valniveau = (String) dataUser.child("niveau").getValue();
        String valmotivation = (String) dataUser.child("motivation").getValue();
        String pathAvatar = (String) dataUser.child("avatar").getValue();

        // set data on views
        if (valprenom != null)
            prenom = valprenom;
        if (valnom != null)
            nom = valnom;
        if (valville != null)
            ville = valville;
        if (valgenre != null)
            this.genre = valgenre;

        if (valniveau != null) {
            nameNiveau = valniveau;

            switch (valniveau) {
                case "Debutant":
                    niveau = 10;
                    break;
                case "Intermediaire":
                    niveau = 30;
                    break;
                case "Avancee":
                    niveau = 50;
                    break;
                case "Expert":
                    niveau = 70;
                    break;
                case "Maitre":
                    niveau = 100;
                    break;
            }
        }

        if (valmotivation != null)
            motivation = valmotivation;

        if (dataUser.child("avatar").exists())
            DownloadAvatar(pathAvatar);

        Log.d("GET DATA USER", "Public profile OK");
    }

    // get public list from firebase
    private List<String> fillDataListFromFirebase(DataSnapshot dataUser, int type_liste){
        DataSnapshot ref = null;
        List<String> newlist = new ArrayList<>();
        Iterator<DataSnapshot> it;

        if (type_liste == 1)
            ref = dataUser.child("instrus");
        else if (type_liste == 2)
            ref = dataUser.child("genreEcoute");
        else if (type_liste == 3)
            ref = dataUser.child("genreJoue");

        if (ref != null && ref.exists()) {
            it = ref.getChildren().iterator();
            //keys = new ArrayList<String>();
            while (it.hasNext()){
                ref = it.next();
                newlist.add((String) ref.getValue());
                //keys.add(ref.getKey());
            }
        }

        return newlist;
    }

    // fill images for the portfolio
    private void fillPortfolioImagesFromFirebase(DataSnapshot dataUser, IResultConnectUser interfaceForResult){
        DataSnapshot img;
        Iterator<DataSnapshot> it = dataUser.child("portfolio").child("images").getChildren().iterator();

        // get images for the portfolio from firebase
        // get paths
        while (it.hasNext()) {
            img = it.next();
            pathImages.add(String.valueOf(img.getValue()));
        }

        Log.d("GET DATA USER", "Path image portfolio OK");

        // get Bitmap
        for (int i = 0; i<pathImages.size(); i++) {
            DownloadPortfolioImages(images, pathImages.get(i), (i == pathImages.size()-1), interfaceForResult);
        }

        Log.d("GET DATA USER", "Image portfolios OK");

        // get videos for the portfolio from firebase

        if (pathImages.size() == 0)
            interfaceForResult.OnSuccess();

    }

    // download image to user's portfolio
    private void DownloadPortfolioImages(final List<Bitmap> imagesToShow, String pictName, final boolean last, final IResultConnectUser interfaceForResult){    // get the images of portfolio
        final File localFile;
        StorageReference ref = storage.child("images/portfolio/"+uid+"/"+pictName);

        try {
            localFile = File.createTempFile("images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("GET PORTFOLIO IMAGE", "Get portfolio image : success");
                    Bitmap bmp = BitmapFactory.decodeFile(localFile.getPath());
                    imagesToShow.add(bmp);
                    loadingFinished = last;  // notify that Firebase finished to load main data
                    if (last)
                        interfaceForResult.OnSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("GET PORTFOLIO IMAGE", "Get portfolio image : failed "+exception.getMessage());
                    loadingFinished = last; // notify that Firebase finished to load main data
                    interfaceForResult.OnFailed();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // download user's avatar from firebase
    private void DownloadAvatar(String nameAvatar){
        final File localFile;
        StorageReference ref = storage.child("images/avatar/"+nameAvatar);
        try {
            localFile = File.createTempFile("images", "jpg");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    avatar = BitmapFactory.decodeFile(localFile.getPath());
                    Log.d("GET AVATAR", "Get avatar : success");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("GET AVATAR", "Get avatar : failed "+exception.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // getters
    public boolean isLoadingFinished() {
        return loadingFinished;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getVille() {
        return ville;
    }

    public String getGenre() {
        return genre;
    }

    public int getNiveau() {
        return niveau;
    }

    public String getNameNiveau() {
        return nameNiveau;
    }

    public String getMotivation() {
        return motivation;
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public List<String> getListened_styles() {
        return listened_styles;
    }

    public List<String> getPlayed_styles() {
        return played_styles;
    }

    public List<String> getPathImages() {
        return pathImages;
    }

    public List<Bitmap> getImages() {
        return images;
    }

    public List<String> getPathVideos() {
        return pathVideos;
    }

    public List<String> getPathSounds() {
        return pathSounds;
    }


    // setters
    public void setLoadingFinished(boolean loadingFinished) {
        this.loadingFinished = loadingFinished;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void setNameNiveau(String nameNiveau) {
        this.nameNiveau = nameNiveau;
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
    }

    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
    }

    public void setListened_styles(List<String> listened_styles) {
        this.listened_styles = listened_styles;
    }

    public void setPlayed_styles(List<String> played_styles) {
        this.played_styles = played_styles;
    }

    public void setPathImages(List<String> pathImages) {
        this.pathImages = pathImages;
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
    }

    public void setPathVideos(List<String> pathVideos) {
        this.pathVideos = pathVideos;
    }

    public void setPathSounds(List<String> pathSounds) {
        this.pathSounds = pathSounds;
    }
}
