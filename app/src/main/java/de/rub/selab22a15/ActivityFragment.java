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

import android.os.SystemClock;
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
    private TextInputEditText oteActivity;

    private Chronometer cmtActivity;

    private MaterialButton btnStartActivity;
    private MaterialButton btnPauseActivity;
    private MaterialButton btnResumeActivity;
    private MaterialButton btnStopActivity;

    private Activity activity;
    private long pauseOffset;

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

        btnStartActivity.setOnClickListener(view1 -> {
            // todo don't start activity recording when text edit is empty
            this.activity = new Activity(System.currentTimeMillis(), oteActivity.getText().toString());

            cmtActivity.setBase(SystemClock.elapsedRealtime());
            cmtActivity.start();
            startAccelerometerRecording();

            disableButton(btnStartActivity);
            enableButton(btnPauseActivity);
            btnStopActivity.setClickable(true);
        });

        btnPauseActivity.setOnClickListener(view1 -> {
            cmtActivity.stop();
            pauseOffset = SystemClock.elapsedRealtime() - cmtActivity.getBase();
            stopAccelerometerRecording();


            disableButton(btnPauseActivity);
            enableButton(btnResumeActivity);
        });

        btnResumeActivity.setOnClickListener(view1 -> {
            cmtActivity.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            cmtActivity.start();
            startAccelerometerRecording();

            disableButton(btnResumeActivity);
            enableButton(btnPauseActivity);
        });

        btnStopActivity.setOnClickListener(view1 -> {
            cmtActivity.stop();
            cmtActivity.setBase(SystemClock.elapsedRealtime());
            stopAccelerometerRecording();

            disableButton(btnPauseActivity);
            disableButton(btnResumeActivity);
            enableButton(btnStartActivity);
            btnStopActivity.setClickable(false);

            alertSave();
        });
    }

    private void disableButton(MaterialButton button) {
        button.setEnabled(false);
        button.setVisibility(View.GONE);
    }

    private void enableButton(MaterialButton button) {
        button.setEnabled(true);
        button.setVisibility(View.VISIBLE);
    }

    private void reset() {
        oteActivity.setText("");
        // ToDo() Disable swGPSEnabled
    }

    private void discard() {
        new AccelerometerRepository(requireActivity().getApplication())
                .delete(activity.getTimestamp());
        reset();
    }

    private void save() {
        this.activity.setActivity(oteActivity.getText().toString());

        new ActivityRepository(requireActivity().getApplication())
                .insert(this.activity);

        reset();
    }

    private void startAccelerometerRecording() {
        Intent intent = new Intent(getActivity(), ActivityRecordService.class);
        intent.putExtra("activity_timestamp", activity.getTimestamp());
        ContextCompat.startForegroundService(requireActivity(), intent);
    }

    private void stopAccelerometerRecording() {
        Intent intent = new Intent(getActivity(), ActivityRecordService.class);
        requireActivity().stopService(intent);

    }

    private void alertSave() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.alertSaveActivityMessage)
                .setNegativeButton(R.string.alertSaveActivityNegative, (dialogInterface, i) -> {
                    discard();
                })
                .setPositiveButton(R.string.alertSaveActivityPositive, ((dialogInterface, i) -> {
                    save();
                }))
                .show();
    }
}