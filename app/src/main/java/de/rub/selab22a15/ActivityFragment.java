package de.rub.selab22a15;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import de.rub.selab22a15.db.GPSRepository;
import de.rub.selab22a15.services.ActivityRecordService;
import de.rub.selab22a15.services.LocationRecordService;

public class ActivityFragment extends Fragment {
    private static final String LOG_ACTIVITY = "ACTIVITY";

    private ActivityResultLauncher<String> requestPermissionLauncher;

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

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted) {
                        switchActivityRecordGPS.setChecked(false);
                    }
                });

        FragmentActivity fragmentActivity = requireActivity();

        textEditActivityRecord = fragmentActivity.findViewById(R.id.textEditActivityRecordActiveRecording);
        switchActivityRecordGPS = fragmentActivity.findViewById(R.id.switchActivityRecordActiveRecordingLocation);
        cmtActivity = fragmentActivity.findViewById(R.id.chronometerActivityRecordActiveRecording);
        buttonStartActivityRecord = fragmentActivity.findViewById(R.id.buttonActivityRecordActiveRecordingStart);
        buttonStopActivityRecord = fragmentActivity.findViewById(R.id.buttonActivityRecordActiveRecordingStop);

        switchActivityRecordGPS.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                return;
            }

            if (ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                // In this case just continue
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_FINE_LOCATION)) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle("GPS")
                        .setMessage(R.string.stringActivityRecordPermissionText)
                        .setNegativeButton(R.string.stringCancel, (dialog, which) -> switchActivityRecordGPS.setChecked(false))
                        .setPositiveButton(R.string.stringOkay, (dialog, which) -> requestPermissionLauncher.launch(ACCESS_FINE_LOCATION))
                        .show();
            } else {
                requestPermissionLauncher.launch(ACCESS_FINE_LOCATION);
            }
        });
        buttonStartActivityRecord.setOnClickListener(v -> startRecord());
        buttonStopActivityRecord.setOnClickListener(v -> stopRecord());

        resetUI();

        Log.d("ARF", "inside on view created");

        if (ActivityRecordService.isRunning()) {
            resumeIntent();
            Log.d("ARF", "inside is running");
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

        textEditActivityRecord.setText(activity.getActivity());
        textEditActivityRecord.setInputType(InputType.TYPE_NULL);

        if (LocationRecordService.isRunning()) {
            switchActivityRecordGPS.setChecked(true);
        }
        switchActivityRecordGPS.setEnabled(false);

        cmtActivity.setBase(ActivityRecordService.getTimeElapsedRealtimeStarted());
        cmtActivity.start();

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
        if (switchActivityRecordGPS.isChecked()) {
            startLocationRecording();
        }
    }

    private void stopRecord() {
        stopAccelerometerRecording();
        if (LocationRecordService.isRunning()) {
            stopLocationRecording();
        }

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
        if (LocationRecordService.isRunning()) {
            new GPSRepository(requireActivity().getApplication())
                    .delete(activity.getTimestamp());
        }

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

    private void startLocationRecording() {
        LocationRecordService.setActivity(activity);
        Intent locationRecordService = new Intent(requireActivity(), LocationRecordService.class);
        ContextCompat.startForegroundService(requireContext(), locationRecordService);
    }

    private void stopLocationRecording() {
        requireActivity().stopService(new Intent(requireActivity(), LocationRecordService.class));
    }
}