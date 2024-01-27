package com.ecorock.game.MainPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.ecorock.game.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainPage extends AppCompatActivity {

    private ViewPager viewPager;
    private boolean LoggedIn = false;
    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        viewPager = findViewById(R.id.viewPager);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Attach the adapter to the ViewPager
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                // Set the starting fragment to be the middle one
                viewPager.setCurrentItem(1);
                return true;
            }
        });

        // Set up BottomNavigationView to handle item selection
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_fragment1) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.navigation_fragment2) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.navigation_fragment3) {
                    viewPager.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });

        // Set up ViewPager to sync with BottomNavigationView
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void navigateToFragment(int position) {
        viewPager.setCurrentItem(position);
    }
}