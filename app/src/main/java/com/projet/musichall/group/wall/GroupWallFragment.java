package com.projet.musichall.group.wall;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.projet.musichall.R;
import com.projet.musichall.group.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupWallFragment extends Fragment {
    ArrayList<Post> listItems = new ArrayList<>();
    PostAdapter adapter;
    ListView list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.group_wall, container, false);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        listItems.add(new Post("Claire", "Je joue de la flûte",dateFormat.format(date)));
        listItems.add(new Post("Fabien", "Je veux juste écouter de la musique et passer du bon temps",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Laurine", "Je suis fan de Metal, tu fais des concerts dans mon bar et je te paye une bière gratuite (une bonne bière du nord hein !)",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Je joue de la flûte",dateFormat.format(date)));
        listItems.add(new Post("Fabien", "Je veux juste écouter de la musique et passer du bon temps",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Laurine", "Je suis fan de Metal, tu fais des concerts dans mon bar et je te paye une bière gratuite (une bonne bière du nord hein !)",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Je joue de la flûte",dateFormat.format(date)));
        listItems.add(new Post("Fabien", "Je veux juste écouter de la musique et passer du bon temps",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Laurine", "Je suis fan de Metal, tu fais des concerts dans mon bar et je te paye une bière gratuite (une bonne bière du nord hein !)",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Je joue de la flûte",dateFormat.format(date)));
        listItems.add(new Post("Fabien", "Je veux juste écouter de la musique et passer du bon temps",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Laurine", "Je suis fan de Metal, tu fais des concerts dans mon bar et je te paye une bière gratuite (une bonne bière du nord hein !)",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Je joue de la flûte",dateFormat.format(date)));
        listItems.add(new Post("Fabien", "Je veux juste écouter de la musique et passer du bon temps",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Laurine", "Je suis fan de Metal, tu fais des concerts dans mon bar et je te paye une bière gratuite (une bonne bière du nord hein !)",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Je joue de la flûte",dateFormat.format(date)));
        listItems.add(new Post("Fabien", "Je veux juste écouter de la musique et passer du bon temps",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Laurine", "Je suis fan de Metal, tu fais des concerts dans mon bar et je te paye une bière gratuite (une bonne bière du nord hein !)",dateFormat.format(date)));

        list = v.findViewById(R.id.group_publications);
        adapter = new PostAdapter(getActivity(), listItems);
        list.setAdapter(adapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
