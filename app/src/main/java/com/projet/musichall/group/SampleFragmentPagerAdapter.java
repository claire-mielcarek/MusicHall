package com.projet.musichall.group;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.projet.musichall.group.chat.GroupChatFragment;
import com.projet.musichall.group.wall.GroupWallFragment;
import com.projet.musichall.R;

public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[];
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[]
                {
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
        if(position == 0){
            return new GroupWallFragment();
        }
        else if (position == 1){
            return new GroupChatFragment();
        }
        else {
            return new GroupCalendarFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
