package com.projet.musichall.group.chat;

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

public class GroupChatFragment extends Fragment {ArrayList<Post> listItems = new ArrayList<>();
    ChatAdapter adapter;
    ListView list;
    View current_view;
    String currentgroupId;
    
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
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Occideretur pretioso pretioso oblato nec ferebatur contactus Honoratum Clematius inter.",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));
        listItems.add(new Post("Claire", "Vel saepe sermonem vel consul.",dateFormat.format(date)));
        listItems.add(new Post("Bob", "Quidem amicis iis sapientia si quidquid de vel de quidquid numero velint velimus statuerimus sunt.",dateFormat.format(date)));
        listItems.add(new Post("Bastien", "Mon instrument c'est la batterie et je cherche quelqu'un avec qui jouer",dateFormat.format(date)));
        listItems.add(new Post("Arthur", "Contingit nec et nec cautela et in lacertos quae pecudum rem contingit contingit grassatores terna rem montium nec circumspecta quae.",dateFormat.format(date)));

        list = current_view.findViewById(R.id.group_chat);
        adapter = new ChatAdapter(getActivity(), listItems, "Claire");
        list.setAdapter(adapter);

    }

    public void setCurrentgroupId(String currentgroupId) {
        this.currentgroupId = currentgroupId;
    }
}
