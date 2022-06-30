package de.rub.selab22a15.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import de.rub.selab22a15.R;
import de.rub.selab22a15.db.Survey;
import de.rub.selab22a15.db.SurveyRepository;
import de.rub.selab22a15.fragments.SurveyEventAppraisalFragment;
import de.rub.selab22a15.fragments.SurveyImpulsivenessFragment;
import de.rub.selab22a15.fragments.SurveyMoodFragment;
import de.rub.selab22a15.fragments.SurveyNoteFragment;
import de.rub.selab22a15.fragments.SurveySelfWorthFragment;
import de.rub.selab22a15.fragments.SurveySocialContextFragment;

public class SurveyActivity extends AppCompatActivity {
    private static final String LOG_TAG = "SURVEY";

    Integer satisfied, calm, well, relaxed, energetic, awake;

    Integer negativeEvent, positiveEvent;

    Integer negativeSelfWorth, positiveSelfWorth;

    Integer actedOnImpulse, actedAggressive;

    String location, surrounded;
    Boolean isAlone;
    Integer surroundedLike;

    String note;

    private SurveyMoodFragment moodFragment;
    private SurveyEventAppraisalFragment eventAppraisalFragment;
    private SurveySelfWorthFragment selfWorthFragment;
    private SurveyImpulsivenessFragment impulsivenessFragment;
    private SurveySocialContextFragment socialContextFragment;
    private SurveyNoteFragment noteFragment;

    private MaterialButton buttonSurveyBack;
    private MaterialButton buttonSurveyNext;
    private MaterialButton buttonSurveySave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        buttonSurveyBack = findViewById(R.id.buttonSurveyBack);
        buttonSurveyNext = findViewById(R.id.buttonSurveyNext);
        buttonSurveySave = findViewById(R.id.buttonSurveySave);

        moodFragment = new SurveyMoodFragment();
        eventAppraisalFragment = new SurveyEventAppraisalFragment();
        selfWorthFragment = new SurveySelfWorthFragment();
        impulsivenessFragment = new SurveyImpulsivenessFragment();
        socialContextFragment = new SurveySocialContextFragment();
        noteFragment = new SurveyNoteFragment();

        setMoodFragment();
    }

    private void setMoodFragment() {
        replaceFragment(moodFragment);

        toggleButton(buttonSurveyBack, false);
        buttonSurveyNext.setOnClickListener(v -> {
            satisfied = moodFragment.getSatisfiedValue();
            calm = moodFragment.getCalmValue();
            well = moodFragment.getWellValue();
            relaxed = moodFragment.getRelaxedValue();
            energetic = moodFragment.getEnergeticValue();
            awake = moodFragment.getAwakeValue();

            toggleButton(buttonSurveyBack, true);

            setEventAppraisalFragment();
        });
    }

    private void setEventAppraisalFragment() {
        replaceFragment(eventAppraisalFragment);

        buttonSurveyBack.setOnClickListener(v -> setMoodFragment());
        buttonSurveyNext.setOnClickListener(v -> {
            negativeEvent = eventAppraisalFragment.getNegativeValue();
            positiveEvent = eventAppraisalFragment.getPositiveValue();

            setSelfWorthFragment();
        });
    }

    private void setSelfWorthFragment() {
        replaceFragment(selfWorthFragment);

        buttonSurveyBack.setOnClickListener(v -> setEventAppraisalFragment());
        buttonSurveyNext.setOnClickListener(v -> {
            negativeSelfWorth = selfWorthFragment.getNegativeQuestionValue();
            positiveSelfWorth = selfWorthFragment.getPositiveQuestionValue();

            setImpulsivenessFragment();
        });
    }

    private void setImpulsivenessFragment() {
        replaceFragment(impulsivenessFragment);

        buttonSurveyBack.setOnClickListener(v -> setSelfWorthFragment());
        buttonSurveyNext.setOnClickListener(v -> {
            actedOnImpulse = impulsivenessFragment.getActedOnImpulseValue();
            actedAggressive = impulsivenessFragment.getActedAggressiveValue();

            setSocialContextFragment();
        });
    }

    private void setSocialContextFragment() {
        replaceFragment(socialContextFragment);

        buttonSurveyBack.setOnClickListener(v -> setImpulsivenessFragment());
        buttonSurveyNext.setOnClickListener(v -> {
            location = socialContextFragment.getLocation();
            isAlone = socialContextFragment.isAlone();
            surrounded = socialContextFragment.getSurrounded();
            surroundedLike = socialContextFragment.getSurroundedLike();

            setNoteFragment();
        });
    }

    private void setNoteFragment() {
        replaceFragment(noteFragment);

        toggleButton(buttonSurveyNext, false);
        toggleButton(buttonSurveySave, true);
        buttonSurveyBack.setOnClickListener(v -> {
            toggleButton(buttonSurveyNext, true);
            toggleButton(buttonSurveySave, false);

            setSocialContextFragment();
        });
        buttonSurveySave.setOnClickListener(v -> exitDialog());
    }

    private void exitDialog() {
        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.alertSurveySaveDialog)
                .setNeutralButton(R.string.stringDecline, (dialog, which) -> {
                })
                .setNegativeButton(R.string.alertSaveActivityNegative, (dialogInterface, i) -> exitSurvey())
                .setPositiveButton(R.string.alertSaveActivityPositive, (dialogInterface, i) -> {
                    save();
                    exitSurvey();
                })
                .show();
    }

    private void save() {
        Survey survey = new Survey(
                System.currentTimeMillis(),
                null,
                satisfied,
                calm,
                well,
                relaxed,
                energetic,
                awake,
                negativeEvent,
                positiveEvent,
                negativeSelfWorth,
                positiveSelfWorth,
                actedOnImpulse,
                actedAggressive,
                location,
                surrounded,
                isAlone,
                surroundedLike,
                note);
        new SurveyRepository(getApplication()).insert(survey);
    }

    private void exitSurvey() {
        finish();
    }

    private void toggleButton(MaterialButton button, boolean enable) {
        int visibility = View.INVISIBLE;
        boolean enabled = false;

        if (enable) {
            visibility = View.VISIBLE;
            enabled = true;
        }

        button.setVisibility(visibility);
        button.setEnabled(enabled);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutSurveyActivity, fragment)
                .commit();
    }
}