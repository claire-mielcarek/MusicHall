package com.projet.musichall.group;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.projet.musichall.group.calendar.GroupCalendarFragment;
import com.projet.musichall.group.chat.GroupChatFragment;
import com.projet.musichall.group.wall.GroupWallFragment;
import com.projet.musichall.R;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String tabTitles[];
    private Context context;
    private String groupId;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context, String groupId) {
        super(fm);
        this.context = context;
        this.groupId = groupId;
        tabTitles = new String[]{
                context.getResources().getString(R.string.group_page1),
                context.getResources().getString(R.string.group_page2),
                context.getResources().getString(R.string.group_page3)
        };
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args =  new Bundle();
        args.putString("groupId", this.groupId);
        Fragment f;
        if(position == 0){
            f = new GroupWallFragment();
            f.setArguments(args);
            return f;
        }
        else if (position == 1){
            f = new GroupChatFragment();
            f.setArguments(args);
            return f;
        }
        else {
            f =new GroupCalendarFragment();
            f.setArguments(args);
            return f;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
