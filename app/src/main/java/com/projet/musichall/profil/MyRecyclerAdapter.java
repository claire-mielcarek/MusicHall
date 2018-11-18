package com.projet.musichall.profil;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.projet.musichall.R;

import java.util.List;




public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    private List<Bitmap> images;
    /*private List<MediaStore.Video> videos;
    private List<MediaActionSound> sons;*/


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        /*public VideoView video;
        public MediaController son;*/

        public MyViewHolder(View v) {
            super(v);

            this.image = v.findViewById(R.id.image);
            /*this.video = v.findViewById(R.id.video);
            this.son = v.findViewById(R.id.son);*/
        }
    }


    public MyRecyclerAdapter(List<Bitmap> images/*, List<MediaStore.Video> videos, List<MediaActionSound> sons*/){
        super();

        this.images = images;
        /*this.sons = sons;
        this.videos = videos;*/
    }


    @NonNull
    @Override
    public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.container_file_image, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // choisir entre une image, une video ou une piste audio
        holder.image.setImageBitmap(images.get(position));
        holder.image.setVisibility(View.VISIBLE);
        //holder.video.
        //holder.son.
    }


    @Override
    public int getItemCount() {
        return images.size()/*+videos.size()+sons.size()*/;
    }

}
