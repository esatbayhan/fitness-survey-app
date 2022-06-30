package de.rub.selab22a15.fragments;

import static de.rub.selab22a15.helpers.SurveySliderOnChangeListener.THUMB_RADIUS_OPAQUE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import de.rub.selab22a15.R;
import de.rub.selab22a15.helpers.SurveySliderLabelFormatter;
import de.rub.selab22a15.helpers.SurveySliderOnChangeListener;


public class SurveySocialContextFragment extends Fragment {
    private static final float NEGATIVE_THRESHOLD = 20f;
    private static final float POSITIVE_THRESHOLD = 80f;

    private TextInputLayout exposedDropdownMenuSurveySocialContextLocation;
    private TextInputLayout exposedDropdownMenuSurveySocialContextSurrounded;
    private RadioGroup radioGroupSurveySocialContextAlone;
    private Slider sliderSurveySocialContextSurroundedLike;

    private TextView textViewSurveySocialContextSurroundedQuestion;

    private TextView textViewSurveySocialContextSurroundedLikeQuestion;
    private LinearLayout linearLayoutSurveySocialContextSurroundedLikeLabels;

    public String getLocation() {
        return getValue(exposedDropdownMenuSurveySocialContextLocation);
    }

    public Boolean isAlone() {
        int radioButtonId = radioGroupSurveySocialContextAlone.getCheckedRadioButtonId();

        if (radioButtonId == -1) {
            return null;
        }

        return radioButtonId == R.id.radioButtonSurveySocialContextAloneYes;
    }

    public String getSurrounded() {
        if (isAlone() == null || isAlone()) {
            return null;
        }

        return getValue(exposedDropdownMenuSurveySocialContextSurrounded);
    }

    public Integer getSurroundedLike() {
        if (isAlone() == null || isAlone()) {
            return null;
        }

        if (sliderSurveySocialContextSurroundedLike.getThumbRadius() != THUMB_RADIUS_OPAQUE) {
            return null;
        }

        return (int) sliderSurveySocialContextSurroundedLike.getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social_context, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = requireActivity();

        exposedDropdownMenuSurveySocialContextLocation = activity.findViewById(
                R.id.exposedDropdownMenuSurveySocialContextLocation);
        exposedDropdownMenuSurveySocialContextSurrounded = activity.findViewById(
                R.id.exposedDropdownMenuSurveySocialContextSurrounded);
        radioGroupSurveySocialContextAlone = activity.findViewById(
                R.id.radioGroupSurveySocialContextAlone);
        sliderSurveySocialContextSurroundedLike = activity.findViewById(
                R.id.sliderSurveySocialContextSurroundedLike);

        textViewSurveySocialContextSurroundedQuestion = activity.findViewById(
                R.id.textViewSurveySocialContextSurroundedQuestion);

        textViewSurveySocialContextSurroundedLikeQuestion = activity.findViewById(R.id.
                textViewSurveySocialContextSurroundedLikeQuestion);
        linearLayoutSurveySocialContextSurroundedLikeLabels = activity.findViewById(R.id.
                linearLayoutSurveySocialContextSurroundedLikeLabels);

        initAutoComplete();
        toggleConditionalViews(false);

        radioGroupSurveySocialContextAlone.setOnCheckedChangeListener((group, checkedId) ->
                toggleConditionalViews(checkedId == R.id.radioButtonSurveySocialContextAloneNo));

        sliderSurveySocialContextSurroundedLike.setTrackActiveTintList(
                sliderSurveySocialContextSurroundedLike.getTrackInactiveTintList());
        sliderSurveySocialContextSurroundedLike.addOnChangeListener(new SurveySliderOnChangeListener());
        sliderSurveySocialContextSurroundedLike.setLabelFormatter(new SurveySliderLabelFormatter(
                requireContext(),
                R.string.stringDoesNotApplyAtAll,
                R.string.stringIsCompletelyTrue,
                null,
                NEGATIVE_THRESHOLD,
                POSITIVE_THRESHOLD
        ));
    }

    private void toggleConditionalViews(boolean isVisible) {
        int visibility = (isVisible) ? View.VISIBLE : View.GONE;

        textViewSurveySocialContextSurroundedQuestion.setVisibility(visibility);
        exposedDropdownMenuSurveySocialContextSurrounded.setVisibility(visibility);
        textViewSurveySocialContextSurroundedLikeQuestion.setVisibility(visibility);
        linearLayoutSurveySocialContextSurroundedLikeLabels.setVisibility(visibility);
        sliderSurveySocialContextSurroundedLike.setVisibility(visibility);
    }

    private void initAutoComplete() {
        FragmentActivity activity = requireActivity();

        AutoCompleteTextView autoCompleteTextViewSurveySocialContextLocation =
                activity.findViewById(R.id.autoCompleteTextViewSurveySocialContextLocation);

        AutoCompleteTextView autoCompleteTextViewSurveySocialContextSurrounded =
                activity.findViewById(R.id.autoCompleteTextViewSurveySocialContextSurrounded);

        String[] locations = getResources().getStringArray(R.array.surveySocialContextLocationsItems);
        ArrayAdapter<String> arrayAdapterLocation = new ArrayAdapter<>(
                requireContext(),
                R.layout.exposed_dropdown_menu_survey_social_context_item,
                locations);
        autoCompleteTextViewSurveySocialContextLocation.setAdapter(arrayAdapterLocation);

        String[] surrounded = getResources().getStringArray(R.array.surveySocialContextSurroundedItems);
        ArrayAdapter<String> arrayAdapterSurrounded = new ArrayAdapter<>(
                requireContext(),
                R.layout.exposed_dropdown_menu_survey_social_context_item,
                surrounded
        );
        autoCompleteTextViewSurveySocialContextSurrounded.setAdapter(arrayAdapterSurrounded);
    }

    private String getValue(TextInputLayout exposedDropdownMenu) {
        // ToDo it returns language specific string. Fix it...
        String surrounded = Objects.requireNonNull(exposedDropdownMenu
                .getEditText()).getText().toString();

        if (surrounded.isEmpty()) {
            return null;
        }

        return surrounded;
    }
}