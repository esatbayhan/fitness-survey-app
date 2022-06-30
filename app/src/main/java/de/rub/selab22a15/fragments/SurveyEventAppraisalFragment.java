package de.rub.selab22a15.fragments;

import static de.rub.selab22a15.helpers.SurveySliderOnChangeListener.THUMB_RADIUS_OPAQUE;

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
import de.rub.selab22a15.helpers.SurveySliderLabelFormatter;
import de.rub.selab22a15.helpers.SurveySliderOnChangeListener;

public class SurveyEventAppraisalFragment extends Fragment {
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

        sliderSurveyEventAppraisalNegative.addOnChangeListener(new SurveySliderOnChangeListener());
        sliderSurveyEventAppraisalNegative.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.stringSurveyEventAppraisalLowIntenseText,
                R.string.stringSurveyEventAppraisalHighIntenseText,
                R.string.stringSurveyResultNeutral,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
        sliderSurveyEventAppraisalNegative.setTrackActiveTintList(
                sliderSurveyEventAppraisalNegative.getTrackInactiveTintList()
        );

        sliderSurveyEventAppraisalPositive.addOnChangeListener(new SurveySliderOnChangeListener());
        sliderSurveyEventAppraisalPositive.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.stringSurveyEventAppraisalLowIntenseText,
                R.string.stringSurveyEventAppraisalHighIntenseText,
                R.string.stringSurveyResultNeutral,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
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
}