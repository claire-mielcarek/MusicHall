package com.projet.musichall.profile;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;


public class FragmentSlideAdapter  extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;

    public FragmentSlideAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PrivateProfil.newInstance();
            case 1:
                return PublicProfil.newInstance();
            case 2:
                return FPortfolio.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String name = "Privée";
        if (position == 1)
            name = "Public";
        else if (position == 2)
            name = "Portfolio";
        return name;
    }
}
