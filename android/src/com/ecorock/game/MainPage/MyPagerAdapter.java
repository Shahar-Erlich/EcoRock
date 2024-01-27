package com.ecorock.game.MainPage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        // Return the Fragment associated with the specified position
        // You need to implement this method based on your fragment setup
        // Example:
        switch (position) {
            case 0:
                return new ShopFragment();
            case 1:
                return new MainFragment();
            case 2:
                return new AccountFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Return the number of fragments
        return 3; // Assuming you have 3 fragments
    }
}