package de.rub.selab22a15.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.Arrays;
import java.util.List;

import de.rub.selab22a15.R;

public class SurveyImpulsivenessFragment extends Fragment {
    private RadioGroup radioGroupSurveyImpulsivenessActedOnImpulse;
    private RadioGroup radioGroupSurveyImpulsivenessActedAggressive;

    public Integer getActedOnImpulseValue() {
        @IdRes int checkRadioButtonId = radioGroupSurveyImpulsivenessActedOnImpulse.getCheckedRadioButtonId();
        return getValue(
                Arrays.asList(
                        R.id.radioButtonSurveyImpulsivenessActedOnImpulse0,
                        R.id.radioButtonSurveyImpulsivenessActedOnImpulse1,
                        R.id.radioButtonSurveyImpulsivenessActedOnImpulse2,
                        R.id.radioButtonSurveyImpulsivenessActedOnImpulse3,
                        R.id.radioButtonSurveyImpulsivenessActedOnImpulse4,
                        R.id.radioButtonSurveyImpulsivenessActedOnImpulse5,
                        R.id.radioButtonSurveyImpulsivenessActedOnImpulse6
                ),
                checkRadioButtonId);
    }

    public Integer getActedAggressiveValue() {
        @IdRes int checkRadioButtonId = radioGroupSurveyImpulsivenessActedAggressive.getCheckedRadioButtonId();
        return getValue(
                Arrays.asList(
                        R.id.radioButtonSurveyImpulsivenessActedAggressive0,
                        R.id.radioButtonSurveyImpulsivenessActedAggressive1,
                        R.id.radioButtonSurveyImpulsivenessActedAggressive2,
                        R.id.radioButtonSurveyImpulsivenessActedAggressive3,
                        R.id.radioButtonSurveyImpulsivenessActedAggressive4,
                        R.id.radioButtonSurveyImpulsivenessActedAggressive5,
                        R.id.radioButtonSurveyImpulsivenessActedAggressive6
                ),
                checkRadioButtonId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_impulsiveness, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = requireActivity();
        radioGroupSurveyImpulsivenessActedOnImpulse = activity.findViewById(R.id.radioGroupSurveyImpulsivenessActedOnImpulse);
        radioGroupSurveyImpulsivenessActedAggressive = activity.findViewById(R.id.radioGroupSurveyImpulsivenessActedAggressive);
    }

    private Integer getValue(List<Integer> ids, @IdRes int checkRadioButtonId) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == checkRadioButtonId) {
                return i;
            }
        }

        return null;
    }
}