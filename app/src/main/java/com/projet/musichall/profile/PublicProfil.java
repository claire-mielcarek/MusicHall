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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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


public class PublicProfil extends Fragment implements IChangeUserData {
    private static final int RC_AVATAR = 100;
    private User user;
    private View root;
    private FirebaseAuth auth;
    private FirebaseUser firebaseuser;
    private DatabaseReference database;
    private StorageReference mStorageRef;
    private com.github.siyamed.shapeimageview.CircularImageView mAvatar;
    private TextView prenom;
    private TextView nom;
    private EditText ville;
    private RadioGroup genre;
    private TextView niveau;
    private SeekBar seek;
    private RadioGroup motivation;


    public static PublicProfil newInstance() {
        return new PublicProfil();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.public_profil, container, false);
        root = rootView;
        user = User.getCurrentUser();
        Log.d("TEST VILLE", "add new callback");
        user.addCallback(this, 0);  // 4*2+1

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // database init
        auth = FirebaseAuth.getInstance();
        firebaseuser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // get views
        prenom = root.findViewById(R.id.prenom);
        nom = root.findViewById(R.id.nom);
        ville = root.findViewById(R.id.editville);
        genre = root.findViewById(R.id.editgenre);
        niveau = root.findViewById(R.id.editniveau);
        seek = root.findViewById(R.id.editniveauseek);
        motivation = root.findViewById(R.id.choixmotivation);
        mAvatar = root.findViewById(R.id.photo);

        // fill data for public profile
        fillDataFromUser();

        // set onclick listener for avatar
        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RC_AVATAR);
            }
        });

        // bind seekbar and textview <niveau>
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 0 && progress <= 20){
                    niveau.setText("Débutant");
                }else if (progress >= 21 && progress <= 40){
                    niveau.setText("Intermédiaire");
                }else if (progress >= 41 && progress <= 60){
                    niveau.setText("Avancé");
                }else if (progress >= 61 && progress <= 80){
                    niveau.setText("Expert");
                }else if (progress >= 81 && progress <= 100){
                    niveau.setText("Maitre");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (firebaseuser != null) {
                    database.child("Users").child(firebaseuser.getUid()).child("niveau").setValue(niveau.getText());
                    user.setNiveau(seekBar.getProgress());
                    user.setNameNiveau(niveau.getText().toString());
                }
            }
        });
        seek.refreshDrawableState();

        ville.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (firebaseuser != null) {
                    database.child("Users").child(firebaseuser.getUid()).child("ville").setValue(s.toString());
                    Log.d("TEST VILLE", s.toString());
                    user.setVille(s.toString());
                }
            }
        });

        motivation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String select = checkedId == R.id.radioa?"Amateur":"Professionel";
                if (firebaseuser != null) {
                    database.child("Users").child(firebaseuser.getUid()).child("motivation").setValue(select);
                    Log.d("TEST CHECKED", select);
                    user.setMotivation(select);
                }
            }
        });

        genre.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String select = checkedId == R.id.radiof?"Femme":checkedId == R.id.radioi?"Inconnue":"Homme";
                if (firebaseuser != null) {
                    database.child("Users").child(firebaseuser.getUid()).child("genre").setValue(select);
                    Log.d("TEST CHECKED", select);
                    user.setGenre(select);
                }
            }
        });

        Button deconnexion = root.findViewById(R.id.deco);
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                user = User.deleteCurrentUser();  // delete data of current user
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });


        // definition onclick pour button vers list view
        Button instru = root.findViewById(R.id.instru);
        instru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ListProfilActivity.class);
                i.putExtra("type_liste", 1);
                startActivity(i);
            }
        });

        Button ecouter = root.findViewById(R.id.ecouter);
        ecouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), ListProfilActivity.class);
                i.putExtra("type_liste", 2);
                startActivity(i);
            }
        });

        Button jouer = root.findViewById(R.id.jouer);
        jouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), ListProfilActivity.class);
                i.putExtra("type_liste", 3);
                startActivity(i);
            }
        });

    }


    private void fillDataFromUser(){
        // display user's data
        prenom.setText(user.getPrenom());  // set prenom
        nom.setText(user.getNom());  // set nom
        ville.setText(user.getVille());  // set city
        switch (user.getGenre()) {   // set genre
            case "Femme":
                genre.clearCheck();
                genre.check(R.id.radiof);
                break;
            case "Inconnu":
                genre.clearCheck();
                genre.check(R.id.radioi);
                break;
            case "Homme":
                genre.clearCheck();
                genre.check(R.id.radioh);
                break;
            default:
                genre.clearCheck();
                genre.check(R.id.radiof);
        }

        niveau.setText(user.getNameNiveau());    // set niveau
        seek.setProgress(user.getNiveau());

        switch (user.getMotivation()) {    // set motivation
            case "Amateur":
                motivation.clearCheck();
                motivation.check(R.id.radioa);
                break;
            case "Professionel":
                motivation.clearCheck();
                motivation.check(R.id.radiop);
                break;
        }

        // get avatar's image
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
        final ByteArrayOutputStream arrayData;
        StorageReference ref;

        // define storage reference
        nameAvatar = "avatar:"+firebaseuser.getUid()+".jpg";
        ref = mStorageRef.child("images/avatar/"+nameAvatar);

        bmp_tmp = Bitmap.createScaledBitmap(bmp, 480, 360, false);
        arrayData = new ByteArrayOutputStream();
        bmp_tmp.compress(Bitmap.CompressFormat.JPEG, 100, arrayData);
        data = arrayData.toByteArray();
        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {     /*.putFile(uri)*/
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                database.child("Users").child(firebaseuser.getUid()).child("avatar").setValue(nameAvatar);
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
