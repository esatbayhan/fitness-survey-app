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
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.db.Activity;
import de.rub.selab22a15.db.ActivityRepository;
import de.rub.selab22a15.services.ActivityRecordService;

public class ActivityFragment extends Fragment {
    private static final String LOG_ACTIVITY = "ACTIVITY";

    private TextInputEditText oteActivity;

    private Chronometer cmtActivity;

    private MaterialButton btnStartActivity;
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
        btnStopActivity = fragmentActivity.findViewById(R.id.btnStopActivity);

        btnStartActivity.setOnClickListener(view1 -> startRecord());
        btnStopActivity.setOnClickListener(view1 -> stopRecord());

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

        activity =  ActivityRecordService.getActivity();

        cmtActivity.setBase(ActivityRecordService.getTimeElapsedRealtimeStarted());
        cmtActivity.start();

        oteActivity.setText(activity.getActivity());
        oteActivity.setInputType(InputType.TYPE_NULL);

        btnStartActivity.setClickable(false);
        btnStopActivity.setClickable(true);
    }

    /**
     * Starts the activity recording if an activity name is entered.
     */
    private void startRecord() {
        String activityName = Objects.requireNonNull(oteActivity.getText()).toString();
        if (activityName.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.toastActivityFragmentStartError),Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        activity = new Activity(System.currentTimeMillis(), activityName);

        oteActivity.setInputType(InputType.TYPE_NULL);

        btnStartActivity.setClickable(false);
        btnStopActivity.setClickable(true);

        cmtActivity.setBase(SystemClock.elapsedRealtime());
        cmtActivity.start();

        startAccelerometerRecording();
    }

    private void stopRecord() {
        stopAccelerometerRecording();

        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.alertSaveActivityMessage)
                .setNegativeButton(R.string.alertSaveActivityNegative, (dialogInterface, i) -> discard())
                .setPositiveButton(R.string.alertSaveActivityPositive, (dialogInterface, i) -> save())
                .show();

        resetUI();
    }

    private void resetUI() {
        oteActivity.setText("");
        oteActivity.setInputType(InputType.TYPE_CLASS_TEXT);

        btnStartActivity.setClickable(true);
        btnStopActivity.setClickable(false);

        cmtActivity.setBase(SystemClock.elapsedRealtime());
        cmtActivity.stop();
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
        activity = null;
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