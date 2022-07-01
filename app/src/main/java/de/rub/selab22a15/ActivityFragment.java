package de.rub.selab22a15;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.rub.selab22a15.activities.SurveyActivity;
import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.db.Activity;
import de.rub.selab22a15.db.ActivityRepository;
import de.rub.selab22a15.services.ActivityRecordService;

public class ActivityFragment extends Fragment {
    private static final String LOG_ACTIVITY = "ACTIVITY";

    private TextInputEditText textEditActivityRecord;
    private SwitchMaterial switchActivityRecordGPS;
    private Chronometer cmtActivity;
    private MaterialButton buttonStartActivityRecord;
    private MaterialButton buttonStopActivityRecord;


    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity fragmentActivity = requireActivity();

        textEditActivityRecord = fragmentActivity.findViewById(R.id.textEditActivityRecord);
        switchActivityRecordGPS = fragmentActivity.findViewById(R.id.switchActivityRecordGPS);
        cmtActivity = fragmentActivity.findViewById(R.id.cmtActivity);
        buttonStartActivityRecord = fragmentActivity.findViewById(R.id.buttonStartActivityRecord);
        buttonStopActivityRecord = fragmentActivity.findViewById(R.id.buttonStopActivityRecord);

        buttonStartActivityRecord.setOnClickListener(view1 -> startRecord());
        buttonStopActivityRecord.setOnClickListener(view1 -> stopRecord());

        resetUI();

        if (ActivityRecordService.isRunning()) {
            resumeIntent();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ActivityRecordService.isRunning()) {
            resumeIntent();
        }
    }

    private void resumeIntent() {
        if (!ActivityRecordService.isRunning()) {
            Log.w(LOG_ACTIVITY, "resumeIntent() got called while no service is running");
            return;
        }

        activity = ActivityRecordService.getActivity();

        cmtActivity.setBase(ActivityRecordService.getTimeElapsedRealtimeStarted());
        cmtActivity.start();

        textEditActivityRecord.setText(activity.getActivity());
        textEditActivityRecord.setInputType(InputType.TYPE_NULL);

        buttonStartActivityRecord.setEnabled(false);
        buttonStopActivityRecord.setEnabled(true);
    }

    private void startRecord() {
        String activityName = Objects.requireNonNull(textEditActivityRecord.getText()).toString();
        if (activityName.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.toastActivityFragmentStartError), Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        activity = new Activity(System.currentTimeMillis(), activityName);
        blockUI();
        startAccelerometerRecording();
    }

    private void stopRecord() {
        stopAccelerometerRecording();

        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.alertSaveActivityMessage)
                .setNegativeButton(R.string.alertSaveActivityNegative, (dialogInterface, i) -> discard())
                .setPositiveButton(R.string.alertSaveActivityPositive, (dialogInterface, i) -> {
                    save();
                    startSurvey();
                })
                .show();

        resetUI();
    }

    private void startSurvey() {
        long activityTimestamp = activity.getTimestamp();
        Intent surveyIntent = new Intent(requireActivity(), SurveyActivity.class);
        surveyIntent.putExtra(SurveyActivity.EXTRA_ACTIVITY_TIMESTAMP, activityTimestamp);
        startActivity(surveyIntent);
    }

    private void resetUI() {
        textEditActivityRecord.setText("");
        textEditActivityRecord.setInputType(InputType.TYPE_CLASS_TEXT);
        switchActivityRecordGPS.setEnabled(true);
        switchActivityRecordGPS.setChecked(false);
        cmtActivity.setBase(SystemClock.elapsedRealtime());
        cmtActivity.stop();
        buttonStartActivityRecord.setEnabled(true);
        buttonStopActivityRecord.setEnabled(false);
    }

    private void blockUI() {
        textEditActivityRecord.setInputType(InputType.TYPE_NULL);
        switchActivityRecordGPS.setEnabled(false);
        cmtActivity.setBase(SystemClock.elapsedRealtime());
        cmtActivity.start();
        buttonStartActivityRecord.setEnabled(false);
        buttonStopActivityRecord.setEnabled(true);
    }

    private void discard() {
        if (activity == null) {
            Log.w(LOG_ACTIVITY, "Warning: this.activity is null");
            return;
        }

        new AccelerometerRepository(requireActivity().getApplication())
                .delete(activity.getTimestamp());
        activity = null;
    }

    private void save() {
        if (activity == null) {
            Log.w(LOG_ACTIVITY, "Warning: this.activity is null");
            return;
        }

        new ActivityRepository(requireActivity().getApplication()).insert(activity);
    }

    private void startAccelerometerRecording() {
        ActivityRecordService.setActivity(activity);
        Intent activityRecordService = new Intent(getActivity(), ActivityRecordService.class);
        ContextCompat.startForegroundService(requireActivity(), activityRecordService);
    }

    private void stopAccelerometerRecording() {
        requireActivity().stopService(new Intent(getActivity(), ActivityRecordService.class));
    }
}