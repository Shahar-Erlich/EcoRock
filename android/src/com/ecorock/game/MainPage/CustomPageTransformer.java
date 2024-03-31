package com.ecorock.game.MainPage;


import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class CustomPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        int pageWidth = page.getWidth();

        if (position < -1) { // Page is off to the left
            page.setAlpha(0);
        } else if (position <= 1) { // Page is visible on screen
            // Modify scale based on position
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float verticalMargin = page.getHeight() * (1 - scaleFactor) / 2;
            float horizontalMargin = page.getWidth() * (1 - scaleFactor) / 2;

            if (position < 0) { // Page is to the left of center
                page.setTranslationX(horizontalMargin - verticalMargin / 2);
            } else { // Page is to the right of center
                page.setTranslationX(-horizontalMargin + verticalMargin / 2);
            }

            // Scale the page
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            // Fade out pages that are not fully visible
            page.setAlpha(MIN_SCALE +
                    (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_SCALE));
        } else { // Page is off to the right
            page.setAlpha(0);
        }
    }
}