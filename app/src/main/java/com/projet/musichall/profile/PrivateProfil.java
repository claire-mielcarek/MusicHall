package com.projet.musichall.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.projet.musichall.MainActivity;
import com.projet.musichall.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;


public class PrivateProfil extends Fragment implements IChangeUserData {
    private static final int RC_AVATAR = 100;
    private User user;
    private View root;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private StorageReference mStorageRef;
    private com.github.siyamed.shapeimageview.CircularImageView mAvatar;
    private TextView prenom;
    private TextView nom;
    private TextView mail;
    private TextView date_naissance;
    private TextView date_inscription;



    public static PrivateProfil newInstance() {
        return new PrivateProfil();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.private_profil, container, false);
        root = rootView;
        user = User.getCurrentUser();
        user.addCallback(this, 0);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // database init
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // get views
        prenom = root.findViewById(R.id.prenom);
        nom = root.findViewById(R.id.nom);
        mail = root.findViewById(R.id.editmail);
        date_naissance = root.findViewById(R.id.datenaissance);
        date_inscription = root.findViewById(R.id.dateinscription);
        mAvatar = root.findViewById(R.id.photo);

        // fill data for private profile
        fillDataFromUser();

        // set onclick listener for avatar
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(i, RC_AVATAR);
            }
        });

        Button deconnexion = root.findViewById(R.id.deco);
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                user = User.deleteCurrentUser();  // delete data of current user
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        ImageView editmail = root.findViewById(R.id.edbutm);
        editmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ChangeSecuredData.class);
                i.putExtra("Mail", true);
                startActivity(i);
                getActivity().finish();
            }
        });

        ImageView editpass = root.findViewById(R.id.edbutp);
        editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ChangeSecuredData.class);
                i.putExtra("Mail", false);
                startActivity(i);
                getActivity().finish();
            }
        });

    }

    private void fillDataFromUser(){
        // set data on views
        prenom.setText(user.getPrenom());   // set prenom
        nom.setText(user.getNom());    // set nom
        mail.setText(user.getMail());   // set mail
        date_naissance.setText(user.getDate_naissance());   // set date naissance
        date_inscription.setText(user.getDate_Inscription());   // set date inscription

        // get avatar's image
        if (user.getAvatar() != null)
            mAvatar.setImageBitmap(user.getAvatar());  // set avatar
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_AVATAR && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try{
                Bitmap result = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                UploadAvatar(result);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private void UploadAvatar(Bitmap bmp){
        final String nameAvatar;
        byte[] data;
        final Bitmap bmp_tmp;
        ByteArrayOutputStream arrayData;
        StorageReference ref;

        // define storage reference
        nameAvatar = "avatar:"+firebaseUser.getUid()+".jpg";
        ref = mStorageRef.child("images/avatar/"+nameAvatar);

        bmp_tmp = Bitmap.createScaledBitmap(bmp, 480, 360, false);
        arrayData = new ByteArrayOutputStream();
        bmp_tmp.compress(Bitmap.CompressFormat.JPEG, 100, arrayData);
        data = arrayData.toByteArray();
        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                database.child("Users").child(firebaseUser.getUid()).child("avatar").setValue(nameAvatar);
                user.setAvatar(bmp_tmp);
                mAvatar.setImageBitmap(bmp_tmp);
                Log.d("SET AVATAR", "Set avatar : success "+nameAvatar+" nombre bytes : "+taskSnapshot.getTotalByteCount());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.w("SET AVATAR", "Set avatar : failed");
            }
        });
    }

    @Override
    public void ChangeData() {
        mAvatar.setImageBitmap(user.getAvatar());
    }
}
