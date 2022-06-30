package de.rub.selab22a15.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.slider.Slider;

import de.rub.selab22a15.R;
import de.rub.selab22a15.helpers.SurveySliderLabelFormatter;
import de.rub.selab22a15.helpers.SurveySliderOnChangeListener;

public class SurveySelfWorthFragment extends Fragment {
    private static final float NEGATIVE_THRESHOLD = 2.0f;
    private static final float POSITIVE_THRESHOLD = 7.0f;

    private Slider sliderSurveySelfWorthNegative;
    private Slider sliderSurveySelfWorthPositive;

    public Integer getNegativeQuestionValue() {
        return (int) sliderSurveySelfWorthNegative.getValue();
    }

    public Integer getPositiveQuestionValue() {
        return (int) sliderSurveySelfWorthPositive.getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_self_worth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = requireActivity();
        sliderSurveySelfWorthNegative = activity.findViewById(R.id.sliderSurveySelfWorthNegative);
        sliderSurveySelfWorthPositive = activity.findViewById(R.id.sliderSurveySelfWorthPositive);

        sliderSurveySelfWorthNegative.addOnChangeListener(new SurveySliderOnChangeListener());
        sliderSurveySelfWorthNegative.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.textViewSurveySelfWorthNegativeLabelText,
                R.string.textViewSurveySelfWorthPositiveLabelText,
                null,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));

        sliderSurveySelfWorthPositive.addOnChangeListener(new SurveySliderOnChangeListener());
        sliderSurveySelfWorthPositive.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.textViewSurveySelfWorthNegativeLabelText,
                R.string.textViewSurveySelfWorthPositiveLabelText,
                null,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
    }
}