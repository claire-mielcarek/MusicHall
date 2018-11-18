package com.projet.musichall.group.chat;

import android.content.Context;
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
import com.projet.musichall.group.wall.PostAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupChatFragment extends Fragment {ArrayList<Post> listItems = new ArrayList<>();
    ChatAdapter adapter;
    ListView list;
    View current_view;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        current_view = inflater.inflate(R.layout.group_chat, container, false);
        return current_view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        listItems.add(new Post("Claire", "Je joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûteJe joue de la flûte",dateFormat.format(date)));
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

        list = current_view.findViewById(R.id.group_chat);
        adapter = new ChatAdapter(getActivity(), listItems, "Claire");
        list.setAdapter(adapter);

    }
}
