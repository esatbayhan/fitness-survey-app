package de.rub.selab22a15.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import de.rub.selab22a15.R;
import de.rub.selab22a15.db.Rumination;
import de.rub.selab22a15.db.RuminationRepository;
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
    public static final String EXTRA_ACTIVITY_TIMESTAMP = "activityTimestamp";

    private Long activityTimestamp;

    // Mood Fragment
    private Integer satisfied, calm, well, relaxed, energetic, awake;

    // Event Appraisal Fragment
    private Integer negativeEvent, positiveEvent;

    // Self Worth Fragment
    private Integer negativeSelfWorth, positiveSelfWorth;

    // Impulsiveness Fragment
    private Integer actedOnImpulse, actedAggressive;

    // Social Context Fragment
    private String location, surrounded;
    private Boolean isAlone;
    private Integer surroundedLike;

    // Note Fragment
    private String note;

    private int ruminationItemIndex = 3;

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

        if (getIntent().hasExtra(EXTRA_ACTIVITY_TIMESTAMP)) {
            activityTimestamp = getIntent().getLongExtra(EXTRA_ACTIVITY_TIMESTAMP, 0L);
        }

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

    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.stringSurveyDiscardTitle)
                .setNeutralButton(R.string.stringCancel, ((dialog, which) -> {
                }))
                .setPositiveButton(R.string.stringDiscard, ((dialog, which) -> {
                    Rumination rumination = new Rumination(
                            System.currentTimeMillis(),
                            activityTimestamp,
                            ruminationItemIndex);
                    new RuminationRepository(getApplication()).insert(rumination);
                    super.onBackPressed();
                }))
                .setSingleChoiceItems(R.array.surveyRumination, ruminationItemIndex,
                        (dialog, which) -> ruminationItemIndex = which)
                .show();
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
        buttonSurveySave.setOnClickListener(v -> {
            note = noteFragment.getNote();
            exitDialog();
        });
    }

    private void exitDialog() {
        new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.alertSurveySaveDialog)
                .setNeutralButton(R.string.stringDecline, (dialog, which) -> {
                })
                .setPositiveButton(R.string.alertSaveActivityPositive, (dialogInterface, i) -> {
                    save();
                    exitSurvey();
                })
                .show();
    }

    private void save() {
        Survey survey = new Survey(
                System.currentTimeMillis(),
                activityTimestamp,
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