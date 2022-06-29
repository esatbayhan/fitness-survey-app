package de.rub.selab22a15.fragments;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.rub.selab22a15.R;

public class SurveyMoodFragment extends Fragment {
    private static final int TRANSPARENT = 0;
    private static final int OPAQUE = 255;
    private static final int NEGATIVE_THRESHOLD = 40;
    private static final int POSITIVE_THRESHOLD = 60;

    private SeekBar seekBarSurveyMoodSatisfied;
    private SeekBar seekBarSurveyMoodCalm;
    private SeekBar seekBarSurveyMoodWell;
    private SeekBar seekBarSurveyMoodEnergetic;
    private SeekBar seekBarSurveyMoodRelaxed;
    private SeekBar seekBarSurveyMoodAwake;

    public Integer getSatisfied() {
        return getProgress(seekBarSurveyMoodSatisfied);
    }

    public Integer getCalm() {
        return getProgress(seekBarSurveyMoodCalm);
    }

    public Integer getWell() {
        return getProgress(seekBarSurveyMoodWell);
    }

    public Integer getEnergetic() {
        return getProgress(seekBarSurveyMoodEnergetic);
    }

    public Integer getRelaxed() {
        return getProgress(seekBarSurveyMoodRelaxed);
    }

    public Integer getAwake() {
        return getProgress(seekBarSurveyMoodAwake);
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
        seekBarSurveyMoodSatisfied = activity.findViewById(R.id.seekBarSurveyMoodSatisfied);
        seekBarSurveyMoodCalm = activity.findViewById(R.id.seekBarSurveyMoodCalm);
        seekBarSurveyMoodWell = activity.findViewById(R.id.seekBarSurveyMoodWell);
        seekBarSurveyMoodEnergetic = activity.findViewById(R.id.seekBarSurveyMoodEnergetic);
        seekBarSurveyMoodRelaxed = activity.findViewById(R.id.seekBarSurveyMoodRelaxed);
        seekBarSurveyMoodAwake = activity.findViewById(R.id.seekBarSurveyMoodAwake);

        List<SeekBar> seekBars = Arrays.asList(
                seekBarSurveyMoodSatisfied,
                seekBarSurveyMoodCalm,
                seekBarSurveyMoodWell,
                seekBarSurveyMoodEnergetic,
                seekBarSurveyMoodRelaxed,
                seekBarSurveyMoodAwake);

        for (SeekBar seekBar : seekBars) {
            seekBar.getThumb().setAlpha(TRANSPARENT);
        }

        seekBarSurveyMoodSatisfied.setOnSeekBarChangeListener(new customOnSeekBarChangeListener(
                R.id.textViewSurveyMoodSatisfiedResult,
                R.string.textViewSurveyMoodSatisfiedNegativeText,
                R.string.textViewSurveyMoodSatisfiedPositiveText
        ));
        seekBarSurveyMoodCalm.setOnSeekBarChangeListener(new customOnSeekBarChangeListener(
                R.id.textViewSurveyMoodCalmResult,
                R.string.textViewSurveyMoodCalmNegativeText,
                R.string.textViewSurveyMoodCalmPositiveText
        ));
        seekBarSurveyMoodWell.setOnSeekBarChangeListener(new customOnSeekBarChangeListener(
                R.id.textViewSurveyMoodWellResult,
                R.string.textViewSurveyMoodWellNegativeText,
                R.string.textViewSurveyMoodWellPositiveText
        ));
        seekBarSurveyMoodEnergetic.setOnSeekBarChangeListener(new customOnSeekBarChangeListener(
                R.id.textViewSurveyMoodEnergeticResult,
                R.string.textViewSurveyMoodEnergeticNegativeText,
                R.string.textViewSurveyMoodEnergeticPositiveText
        ));
        seekBarSurveyMoodRelaxed.setOnSeekBarChangeListener(new customOnSeekBarChangeListener(
                R.id.textViewSurveyMoodRelaxedResult,
                R.string.textViewSurveyMoodRelaxedNegativeText,
                R.string.textViewSurveyMoodRelaxedPositiveText
        ));
        seekBarSurveyMoodAwake.setOnSeekBarChangeListener(new customOnSeekBarChangeListener(
                R.id.textViewSurveyMoodAwakeResult,
                R.string.textViewSurveyMoodAwakeNegativeText,
                R.string.textViewSurveyMoodAwakePositiveText
        ));
    }

    private Integer getProgress(SeekBar seekBar) {
        if (seekBar.getThumb().getAlpha() != OPAQUE) {
            return null;
        }

        return seekBar.getProgress();
    }

    private class customOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @StringRes private final int negativeTextId;
        @StringRes private final int positiveTextId;
        private final TextView textViewResult;

        public customOnSeekBarChangeListener(@IdRes int textViewResultId, @StringRes int negativeTextId, @StringRes int positiveTextId) {
            this.textViewResult = requireActivity().findViewById(textViewResultId);
            this.negativeTextId = negativeTextId;
            this.positiveTextId = positiveTextId;
        }

        /* I think it is better to not show the progress, because i believe it would bias the user
        private void setText(String text, int progress) {
            textViewResult.setText(String.format("%s %d%%", text, progress));
        }*/

        private void setText(int id) {
            textViewResult.setText(id);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            seekBar.getThumb().setAlpha(255);
            seekBar.setProgress(progress);

            if (progress < NEGATIVE_THRESHOLD) {
                setText(negativeTextId);
            }
            else if (progress > POSITIVE_THRESHOLD) {
                setText(positiveTextId);
            }
            else {
                setText(R.string.stringSurveyMoodResultNeutral);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}