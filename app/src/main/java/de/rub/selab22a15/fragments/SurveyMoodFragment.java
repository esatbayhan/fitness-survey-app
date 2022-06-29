package de.rub.selab22a15.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.Arrays;
import java.util.List;

import de.rub.selab22a15.R;

public class SurveyMoodFragment extends Fragment {
    private static final int THUMB_RADIUS_OPAQUE = 20;
    private static final float NEGATIVE_THRESHOLD = 0.4f;
    private static final float POSITIVE_THRESHOLD = 0.6f;

    private Slider sliderSurveyMoodSatisfied;
    private Slider sliderSurveyMoodCalm;
    private Slider sliderSurveyMoodWell;
    private Slider sliderSurveyMoodEnergetic;
    private Slider sliderSurveyMoodRelaxed;
    private Slider sliderSurveyMoodAwake;

    public Integer getSatisfied() {
        return getProgress(sliderSurveyMoodSatisfied);
    }

    public Integer getCalm() {
        return getProgress(sliderSurveyMoodCalm);
    }

    public Integer getWell() {
        return getProgress(sliderSurveyMoodWell);
    }

    public Integer getEnergetic() {
        return getProgress(sliderSurveyMoodEnergetic);
    }

    public Integer getRelaxed() {
        return getProgress(sliderSurveyMoodRelaxed);
    }

    public Integer getAwake() {
        return getProgress(sliderSurveyMoodAwake);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_mood, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = requireActivity();
        sliderSurveyMoodSatisfied = activity.findViewById(R.id.sliderSurveyMoodSatisfied);
        sliderSurveyMoodCalm = activity.findViewById(R.id.sliderSurveyMoodCalm);
        sliderSurveyMoodWell = activity.findViewById(R.id.sliderSurveyMoodWell);
        sliderSurveyMoodEnergetic = activity.findViewById(R.id.sliderSurveyMoodEnergetic);
        sliderSurveyMoodRelaxed = activity.findViewById(R.id.sliderSurveyMoodRelaxed);
        sliderSurveyMoodAwake = activity.findViewById(R.id.sliderSurveyMoodAwake);

        List<Slider> sliders = Arrays.asList(
                sliderSurveyMoodSatisfied,
                sliderSurveyMoodCalm,
                sliderSurveyMoodWell,
                sliderSurveyMoodEnergetic,
                sliderSurveyMoodRelaxed,
                sliderSurveyMoodAwake
        );

        for (Slider slider : sliders) {
            slider.addOnChangeListener(new customSliderOnChangeListener());
            slider.setTrackActiveTintList(
                    slider.getTrackInactiveTintList()
            );
        }

        sliderSurveyMoodSatisfied.setLabelFormatter(new customLabelFormatter(
                R.string.textViewSurveyMoodSatisfiedNegativeText,
                R.string.textViewSurveyMoodSatisfiedPositiveText
        ));
        sliderSurveyMoodCalm.setLabelFormatter(new customLabelFormatter(
                R.string.textViewSurveyMoodCalmNegativeText,
                R.string.textViewSurveyMoodCalmPositiveText
        ));
        sliderSurveyMoodWell.setLabelFormatter(new customLabelFormatter(
                R.string.textViewSurveyMoodWellNegativeText,
                R.string.textViewSurveyMoodWellPositiveText
        ));
        sliderSurveyMoodEnergetic.setLabelFormatter(new customLabelFormatter(
                R.string.textViewSurveyMoodEnergeticNegativeText,
                R.string.textViewSurveyMoodEnergeticPositiveText
        ));
        sliderSurveyMoodRelaxed.setLabelFormatter(new customLabelFormatter(
                R.string.textViewSurveyMoodRelaxedNegativeText,
                R.string.textViewSurveyMoodRelaxedPositiveText
        ));
        sliderSurveyMoodAwake.setLabelFormatter(new customLabelFormatter(
                R.string.textViewSurveyMoodAwakeNegativeText,
                R.string.textViewSurveyMoodAwakePositiveText
        ));
    }

    private Integer getProgress(Slider slider) {
        if (slider.getThumbRadius() != THUMB_RADIUS_OPAQUE) {
            return null;
        }

        return (int) (slider.getValue() * 100);
    }

    private class customSliderOnChangeListener implements Slider.OnChangeListener {
        private boolean isFirstUsage = true;

        @Override
        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
            if (isFirstUsage) {
                isFirstUsage = false;
                slider.setThumbRadius(THUMB_RADIUS_OPAQUE);
            }
        }
    }

    private class customLabelFormatter implements LabelFormatter {
        private final String negativeText;
        private final String positiveText;
        private final String neutralText;

        public customLabelFormatter(@StringRes int negativeTextId, @StringRes int positiveTextId) {
            negativeText = getString(negativeTextId);
            positiveText = getString(positiveTextId);
            neutralText = getString(R.string.stringSurveyMoodResultNeutral);
        }

        @NonNull
        @Override
        public String getFormattedValue(float value) {
            if (value < NEGATIVE_THRESHOLD) {
                return negativeText;
            } else if (value > POSITIVE_THRESHOLD) {
                return positiveText;
            }

            return neutralText;
        }
    }
}