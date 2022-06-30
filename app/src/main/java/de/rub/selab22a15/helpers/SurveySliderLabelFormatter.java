package de.rub.selab22a15.helpers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.slider.LabelFormatter;

public class SurveySliderLabelFormatter implements LabelFormatter {
    private final String negativeText;
    private final String positiveText;
    private final String neutralText;
    private final float negativeThreshold;
    private final float positiveThreshold;

    public SurveySliderLabelFormatter(
            Context context,
            @StringRes Integer negativeTextId,
            @StringRes Integer positiveTextId,
            @StringRes Integer neutralTextId,
            float negativeThreshold,
            float positiveThreshold) {

        if (negativeTextId == null) {
            negativeText = null;
        }
        else {
            negativeText = context.getString(negativeTextId);
        }

        if (positiveTextId == null) {
            positiveText = null;
        }
        else {
            positiveText = context.getString(positiveTextId);
        }

        if (neutralTextId == null) {
            neutralText = null;
        }
        else {
            neutralText = context.getString(neutralTextId);
        }

        this.negativeThreshold = negativeThreshold;
        this.positiveThreshold = positiveThreshold;
    }

    @NonNull
    @Override
    public String getFormattedValue(float value) {
        if (value < negativeThreshold) {
            return negativeText;
        } else if (value > positiveThreshold) {
            return positiveText;
        }

        if (neutralText == null) {
            return String.valueOf((int) value);
        }
         return neutralText;
    }
}
