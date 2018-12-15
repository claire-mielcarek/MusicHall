package com.projet.musichall.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.projet.musichall.R;

import java.util.List;




public class MyGridAdapter extends BaseAdapter/*RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>*/ {
    private Context context;
    private List<Bitmap> images;
    /*private List<MediaStore.Video> videos;
    private List<MediaActionSound> sons;*/


    public MyGridAdapter(Context context, List<Bitmap> images/*, List<MediaStore.Video> videos, List<MediaActionSound> sons*/){
        super();

        this.images = images;
        /*this.sons = sons;
        this.videos = videos;*/

        this.context = context;
    }

    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View multimedia;
        ImageView imageView;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            multimedia = new View(context);

            // get multimedia container
            multimedia = inflater.inflate(R.layout.container_file_image, null);

            if (multimedia != null) {
                // set image or video or sound
                imageView = multimedia.findViewById(R.id.image);
                imageView.setImageBitmap(images.get(position));
                imageView.setVisibility(View.VISIBLE);
            }
        } else {
            multimedia = convertView;
        }

        return multimedia;
    }

    public List<Bitmap> getImages() {
        return images;
    }

    /*public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        /*public VideoView video;
        public MediaController son;

        public MyViewHolder(View v) {
            super(v);

            this.image = v.findViewById(R.id.image);
            /*this.video = v.findViewById(R.id.video);
            this.son = v.findViewById(R.id.son);
        }
    }


    public MyRecyclerAdapter(List<Bitmap> images/*, List<MediaStore.Video> videos, List<MediaActionSound> sons){
        super();

        this.images = images;
        /*this.sons = sons;
        this.videos = videos;
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
        return images.size()/*+videos.size()+sons.size();
    }*/

}
