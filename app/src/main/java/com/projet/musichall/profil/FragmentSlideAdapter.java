package com.projet.musichall.profil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class FragmentSlideAdapter  extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 2;

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
        return name;
    }
}
