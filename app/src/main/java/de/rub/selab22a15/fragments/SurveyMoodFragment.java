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

import java.util.Arrays;
import java.util.List;

import de.rub.selab22a15.R;
import de.rub.selab22a15.helpers.SurveySliderLabelFormatter;
import de.rub.selab22a15.helpers.SurveySliderOnChangeListener;

public class SurveyMoodFragment extends Fragment {
    private static final float NEGATIVE_THRESHOLD = 0.35f;
    private static final float POSITIVE_THRESHOLD = 0.65f;

    private Slider sliderSurveyMoodSatisfied;
    private Slider sliderSurveyMoodCalm;
    private Slider sliderSurveyMoodWell;
    private Slider sliderSurveyMoodEnergetic;
    private Slider sliderSurveyMoodRelaxed;
    private Slider sliderSurveyMoodAwake;

    public Integer getSatisfiedValue() {
        return getValue(sliderSurveyMoodSatisfied);
    }

    public Integer getCalmValue() {
        return getValue(sliderSurveyMoodCalm);
    }

    public Integer getWellValue() {
        return getValue(sliderSurveyMoodWell);
    }

    public Integer getEnergeticValue() {
        return getValue(sliderSurveyMoodEnergetic);
    }

    public Integer getRelaxedValue() {
        return getValue(sliderSurveyMoodRelaxed);
    }

    public Integer getAwakeValue() {
        return getValue(sliderSurveyMoodAwake);
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
            slider.addOnChangeListener(new SurveySliderOnChangeListener());
            slider.setTrackActiveTintList(
                    slider.getTrackInactiveTintList()
            );
        }

        sliderSurveyMoodSatisfied.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.textViewSurveyMoodSatisfiedNegativeText,
                R.string.textViewSurveyMoodSatisfiedPositiveText,
                R.string.stringSurveyResultNeutral,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
        sliderSurveyMoodCalm.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.textViewSurveyMoodCalmNegativeText,
                R.string.textViewSurveyMoodCalmPositiveText,
                R.string.stringSurveyResultNeutral,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
        sliderSurveyMoodWell.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.textViewSurveyMoodWellNegativeText,
                R.string.textViewSurveyMoodWellPositiveText,
                R.string.stringSurveyResultNeutral,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
        sliderSurveyMoodEnergetic.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.textViewSurveyMoodEnergeticNegativeText,
                R.string.textViewSurveyMoodEnergeticPositiveText,
                R.string.stringSurveyResultNeutral,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
        sliderSurveyMoodRelaxed.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.textViewSurveyMoodRelaxedNegativeText,
                R.string.textViewSurveyMoodRelaxedPositiveText,
                R.string.stringSurveyResultNeutral,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
        sliderSurveyMoodAwake.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.textViewSurveyMoodAwakeNegativeText,
                R.string.textViewSurveyMoodAwakePositiveText,
                R.string.stringSurveyResultNeutral,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
    }

    private Integer getValue(Slider slider) {
        if (slider.getThumbRadius() != THUMB_RADIUS_OPAQUE) {
            return null;
        }

        return (int) (slider.getValue() * 100);
    }
}