package com.example.artisanforum.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.artisanforum.fragments.OwnerListFrag;
import com.example.artisanforum.fragments.YourPosts;


public class tabpagerAdapter extends FragmentStatePagerAdapter {
    private String[] tabarray = new String[]{"Owner List", "Your Posts"};

    public tabpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabarray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new OwnerListFrag();
            case 1:
                return new YourPosts();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
