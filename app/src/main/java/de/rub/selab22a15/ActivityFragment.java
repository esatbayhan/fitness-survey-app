package de.rub.selab22a15;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Debug;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.rub.selab22a15.db.Accelerometer;
import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.db.Activity;
import de.rub.selab22a15.db.ActivityRepository;
import de.rub.selab22a15.services.ActivityRecordService;

public class ActivityFragment extends Fragment {
    private static final String LOG_ACTIVITY = "ACTIVITY";
    private static long pauseOffset;

    private TextInputEditText oteActivity;

    private Chronometer cmtActivity;

    private MaterialButton btnStartActivity;
    private MaterialButton btnPauseActivity;
    private MaterialButton btnResumeActivity;
    private MaterialButton btnStopActivity;

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

        oteActivity = fragmentActivity.findViewById(R.id.oteActivity);

        cmtActivity = fragmentActivity.findViewById(R.id.cmtActivity);

        btnStartActivity = fragmentActivity.findViewById(R.id.btnStartActivity);
        btnPauseActivity = fragmentActivity.findViewById(R.id.btnPauseActivity);
        btnResumeActivity = fragmentActivity.findViewById(R.id.btnResumeActivity);
        btnStopActivity = fragmentActivity.findViewById(R.id.btnStopActivity);

        btnStartActivity.setOnClickListener(view1 -> start());
        btnPauseActivity.setOnClickListener(view1 -> pause());
        btnResumeActivity.setOnClickListener(view1 -> resume());
        btnStopActivity.setOnClickListener(view1 -> stop());

        if (ActivityRecordService.isRunning()) {
            resumeIntent();
        }
        else {
            pauseOffset = 0;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (ActivityRecordService.isRunning()) {
            pauseOffset = SystemClock.elapsedRealtime() - cmtActivity.getBase();
        }
        else {
            pauseOffset = 0;
        }
    }

    private void resumeIntent() {
        activity = ActivityRecordService.getActivity();

        resumeClock();

        oteActivity.setText(activity.getActivity());
        oteActivity.setInputType(InputType.TYPE_NULL);

        enableButton(btnPauseActivity);
        disableButton(btnStartActivity);
        disableButton(btnResumeActivity);
        btnStopActivity.setClickable(true);
    }

    /**
     * Starts the activity recording if an activity name is entered.
     */
    private void start() {
        String activityName = Objects.requireNonNull(oteActivity.getText()).toString();
        if (activityName.isEmpty()) return;

        activity = new Activity(System.currentTimeMillis(), activityName);

        oteActivity.setInputType(InputType.TYPE_NULL);

        enableButton(btnStartActivity);
        disableButton(btnPauseActivity);
        disableButton(btnResumeActivity);
        btnStopActivity.setClickable(true);

        cmtActivity.setBase(SystemClock.elapsedRealtime());
        cmtActivity.start();

        startAccelerometerRecording();
    }

    private void pause() {
        pauseClock();
        stopAccelerometerRecording();

        disableButton(btnPauseActivity);
        enableButton(btnResumeActivity);
    }

    private void resume() {
        resumeClock();
        startAccelerometerRecording();

        disableButton(btnResumeActivity);
        disableButton(btnStartActivity);
        enableButton(btnPauseActivity);
        btnStopActivity.setClickable(true);
    }

    private void stop() {
        pauseClock();
        stopAccelerometerRecording();
        alertSave();
        reset();
    }

    private void reset() {
        oteActivity.setText("");
        oteActivity.setInputType(InputType.TYPE_CLASS_TEXT);

        enableButton(btnStartActivity);
        disableButton(btnPauseActivity);
        disableButton(btnResumeActivity);
        btnStopActivity.setClickable(false);

        cmtActivity.setBase(SystemClock.elapsedRealtime());

        activity = null;
        pauseOffset = 0;
    }

    private void discard() {
        if (activity == null) {
            Log.w(LOG_ACTIVITY, "Warning: this.activity is null");
            return;
        }

        new AccelerometerRepository(requireActivity().getApplication())
                .delete(activity.getTimestamp());
    }

    private void save() {
        if (activity == null) {
            Log.w(LOG_ACTIVITY, "Warning: this.activity is null");
            return;
        }

        new ActivityRepository(requireActivity().getApplication()).insert(activity);
    }

    private void resumeClock() {
        cmtActivity.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        cmtActivity.start();
    }

    private void pauseClock() {
        cmtActivity.stop();
        pauseOffset = SystemClock.elapsedRealtime() - cmtActivity.getBase();
    }

    private void enableButton(MaterialButton button) {
        button.setEnabled(true);
        button.setVisibility(View.VISIBLE);
    }

    private void disableButton(MaterialButton button) {
        button.setEnabled(false);
        button.setVisibility(View.GONE);
    }

    private void startAccelerometerRecording() {
        ActivityRecordService.setActivity(activity);
        Intent activityRecordService = new Intent(getActivity(), ActivityRecordService.class);
        activityRecordService.putExtra("activity_timestamp", activity.getTimestamp());
        ContextCompat.startForegroundService(requireActivity(), activityRecordService);
    }

    private void stopAccelerometerRecording() {
        requireActivity().stopService(new Intent(getActivity(), ActivityRecordService.class));
    }

    private void alertSave() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.alertSaveActivityMessage)
                .setNegativeButton(R.string.alertSaveActivityNegative, (dialogInterface, i) -> discard())
                .setPositiveButton(R.string.alertSaveActivityPositive, (dialogInterface, i) -> save())
                .show();
    }
}