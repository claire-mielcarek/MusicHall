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

        // get multimedia container
        multimedia = inflater.inflate(R.layout.container_file_image, null);

        if (multimedia != null) {
            // set image or video or sound
            imageView = multimedia.findViewById(R.id.image);
            imageView.setImageBitmap(images.get(position));
            imageView.setVisibility(View.VISIBLE);
        }

        return multimedia;
    }

    public List<Bitmap> getImages() {
        return images;
    }

}
