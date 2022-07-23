package de.rub.selab22a15;

import androidx.annotation.NonNull;

import com.google.android.material.slider.Slider;

public class userStatisticsSliderOnChangeListener implements Slider.OnChangeListener {
    public static final int THUMB_RADIUS_OPAQUE = 20;
    private static final float NEGATIVE_THRESHOLD = 0.35f;
    private static final float POSITIVE_THRESHOLD = 0.65f;

    private boolean isFirstUsage = true;

    @Override
    public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
        if (isFirstUsage) {
            isFirstUsage = false;
            slider.setThumbRadius(THUMB_RADIUS_OPAQUE);
        }
    }
}
