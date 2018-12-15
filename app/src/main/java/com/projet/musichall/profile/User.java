package com.projet.musichall.profile;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;




public class User {
    // define enum
    public enum Auth{
        GOOGLE,FACEBOOK,MAIL
    }

    public enum Genre{
        FEMME,INCONNU,HOMME
    }

    public enum Motivation{
        AMATEUR,PROFESSIONEL
    }

    public enum NIVEAU{
        DEBUTANT,INTERMEDIAIRE,AVANCE,EXPERT,MAITRE
    }

    // the only instance of User
    private static User user;

    // admin data
    private String uid;
    private boolean loadingFinished;
    private boolean first;
    private int pass;  // prevent the callback recurrence
    private int call;
    private Enum<Auth> type_auth;

    // Firebase reference
    private FirebaseAuth auth;
    private FirebaseUser fireUser;
    private DatabaseReference database;
    private StorageReference storage;

    // implement the system to change data for the classes implementing IChangeUserData
    List<IChangeUserData> register;

    // private data
    private String mail;
    private String date_Inscription;
    private String date_naissance;

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


    // handle the instance of user
    public static User InstantiateUser(Enum<Auth> type){
        user = new User(type);
        return user;
    }

    public static User getCurrentUser(){
        return user;
    }

    public static User deleteCurrentUser(){
        user = null;
        return null;
    }

    // private constructor only one user in memory
    private User(Enum<Auth> type){

        // connect to firebase
        auth = FirebaseAuth.getInstance();
        fireUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();

        // init register
        register = new ArrayList<>();

        // init data
        this.uid = fireUser.getUid();
        loadingFinished = false;
        first = true;
        pass = 0;
        call = 1;  // must have one call to the callbacks
        type_auth = type;
        niveau = 0;
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

    // register callback function
    public void addCallback(IChangeUserData dataChange, int call){
        this.call += call;
        register.add(dataChange);
    }

    // attach User to Firebase which fills data on runtime
    public void attachUserToFirebase(/*DataSnapshot dataSnapshot, StorageReference storage, */final boolean one_time, final IResultConnectUser interfaceForResult){

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get user node
                if (!one_time||first) {
                    DataSnapshot dataUser = dataSnapshot.child("Users").child(uid);
                    // get private user data
                    fillPrivateDataFromFirebase(/*storage, */dataUser);
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
                    fillPortfolioImagesFromFirebase(/*storage, */dataUser, interfaceForResult);

                    // this no more the first time
                    first = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DatabaseChange", "Failed to read values.", databaseError.toException());
            }
        });
    }

    // get private data from firebase
    private void fillPrivateDataFromFirebase(/*StorageReference storage, */DataSnapshot dataUser){
        String valprenom = (String) dataUser.child("prenom").getValue();
        String valnom = (String) dataUser.child("nom").getValue();
        String valmail = (String) dataUser.child("mail").getValue();
        String valnaissance = (String) dataUser.child("dateNaissance").getValue();
        String valinscription = (String) dataUser.child("dateMembre").getValue();
        String pathAvatar = (String) dataUser.child("avatar").getValue();

        // set data on views
        if (valprenom != null)
            prenom = valprenom;
        if (valnom != null)
            nom = valnom;
        if (valmail != null)
            mail = valmail;
        if (valnaissance != null)
            date_naissance = valnaissance;
        if (valinscription != null)
            date_Inscription = valinscription;
        if (dataUser.child("avatar").exists())
            DownloadAvatar(/*storage, */pathAvatar);

        Log.d("GET DATA USER", "Private profile OK");
    }

    // get public data from firebase
    private void fillPublicDataFromFirebase(DataSnapshot dataUser){
        String valville = (String) dataUser.child("ville").getValue();
        String valgenre = (String) dataUser.child("genre").getValue();
        String valniveau = (String) dataUser.child("niveau").getValue();
        String valmotivation = (String) dataUser.child("motivation").getValue();

        // set data on views
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
    private void fillPortfolioImagesFromFirebase(/*StorageReference storage, */DataSnapshot dataUser, IResultConnectUser interfaceForResult){
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
            DownloadPortfolioImages(/*storage, */images, pathImages.get(i), (i == pathImages.size()-1), interfaceForResult);
        }

        Log.d("GET DATA USER", "Image portfolios OK");

        // get videos for the portfolio from firebase


    }

    // download image to user's portfolio
    private void DownloadPortfolioImages(/*StorageReference storage, */final List<Bitmap> imagesToShow, String pictName, final boolean last, final IResultConnectUser interfaceForResult){    // get the images of portfolio
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
    private void DownloadAvatar(/*StorageReference storage, */String nameAvatar){
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

    public String getMail() {
        return mail;
    }

    public String getDate_Inscription() {
        return date_Inscription;
    }

    public String getDate_naissance() {
        return date_naissance;
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
        executeCallBackFunction();
    }

    public void setMail(String mail) {
        this.mail = mail;
        executeCallBackFunction();
    }

    public void setDate_Inscription(String date_Inscription) {
        this.date_Inscription = date_Inscription;
        executeCallBackFunction();
    }

    public void setDate_naissance(String date_naissance) {
        this.date_naissance = date_naissance;
        executeCallBackFunction();
    }

    public void setNom(String nom) {
        this.nom = nom;
        executeCallBackFunction();
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
        executeCallBackFunction();
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
        executeCallBackFunction();
    }

    public void setVille(String ville) {
        this.ville = ville;
        executeCallBackFunction();
    }

    public void setGenre(String genre) {
        this.genre = genre;
        executeCallBackFunction();
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
        executeCallBackFunction();
    }

    public void setNameNiveau(String nameNiveau) {
        this.nameNiveau = nameNiveau;
        executeCallBackFunction();
    }

    public void setMotivation(String motivation) {
        this.motivation = motivation;
        executeCallBackFunction();
    }

    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
        executeCallBackFunction();
    }

    public void setListened_styles(List<String> listened_styles) {
        this.listened_styles = listened_styles;
        executeCallBackFunction();
    }

    public void setPlayed_styles(List<String> played_styles) {
        this.played_styles = played_styles;
        executeCallBackFunction();
    }

    public void setPathImages(List<String> pathImages) {
        this.pathImages = pathImages;
        executeCallBackFunction();
    }

    public void setImages(List<Bitmap> images) {
        this.images = images;
        executeCallBackFunction();
    }

    public void setPathVideos(List<String> pathVideos) {
        this.pathVideos = pathVideos;
        executeCallBackFunction();
    }

    public void setPathSounds(List<String> pathSounds) {
        this.pathSounds = pathSounds;
        executeCallBackFunction();
    }

    private void executeCallBackFunction(){
        Log.d("PROBLEME USER", "call : "+call+" pass : "+pass);
        if (pass == 0){   // prevent the callback recurrence
            pass = (pass+1)%call;
            for (int i = 0; i < register.size(); i++) {
                register.get(i).ChangeData();
            }
        }else{
            pass = (pass+1)%call;
        }
    }
}
