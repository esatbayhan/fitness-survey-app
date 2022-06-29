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

import de.rub.selab22a15.R;

public class SurveyEventAppraisalFragment extends Fragment {
    private static final int THUMB_RADIUS_OPAQUE = 20;
    private static final float NEGATIVE_THRESHOLD = 0.35f;
    private static final float POSITIVE_THRESHOLD = 0.65f;

    private Slider sliderSurveyEventAppraisalNegative;
    private Slider sliderSurveyEventAppraisalPositive;

    public Integer getNegativeValue() {
        return getValue(sliderSurveyEventAppraisalNegative);
    }

    public Integer getPositiveValue() {
        return getValue(sliderSurveyEventAppraisalPositive);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_event_appraisal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = requireActivity();
        sliderSurveyEventAppraisalNegative = activity.findViewById(R.id.sliderSurveyEventAppraisalNegative);
        sliderSurveyEventAppraisalPositive = activity.findViewById(R.id.sliderSurveyEventAppraisalPositive);

        sliderSurveyEventAppraisalNegative.addOnChangeListener(new CustomOnChangeListener());
        sliderSurveyEventAppraisalNegative.setLabelFormatter(new CustomLabelFormatter(
                R.string.stringSurveyEventAppraisalLowIntenseText,
                R.string.stringSurveyEventAppraisalHighIntenseText
        ));
        sliderSurveyEventAppraisalNegative.setTrackActiveTintList(
                sliderSurveyEventAppraisalNegative.getTrackInactiveTintList()
        );

        sliderSurveyEventAppraisalPositive.addOnChangeListener(new CustomOnChangeListener());
        sliderSurveyEventAppraisalPositive.setLabelFormatter(new CustomLabelFormatter(
                R.string.stringSurveyEventAppraisalLowIntenseText,
                R.string.stringSurveyEventAppraisalHighIntenseText
        ));
        sliderSurveyEventAppraisalPositive.setTrackActiveTintList(
                sliderSurveyEventAppraisalPositive.getTrackInactiveTintList()
        );
    }

    private Integer getValue(Slider slider) {
        if (slider.getThumbRadius() != THUMB_RADIUS_OPAQUE) {
            return null;
        }

        return (int) (slider.getValue() * 100);
    }

    private class CustomOnChangeListener implements Slider.OnChangeListener {
        private boolean isFirstUsage = true;

        @Override
        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
            if (isFirstUsage) {
                isFirstUsage = false;
                slider.setThumbRadius(THUMB_RADIUS_OPAQUE);
            }
        }
    }

    private class CustomLabelFormatter implements LabelFormatter {
        private final String negativeText;
        private final String positiveText;
        private final String neutralText;

        public CustomLabelFormatter(@StringRes int negativeTextId, @StringRes int positiveTextId) {
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