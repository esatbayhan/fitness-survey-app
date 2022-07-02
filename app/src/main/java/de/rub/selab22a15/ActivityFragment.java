package de.rub.selab22a15;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.DateFormat;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.rub.selab22a15.activities.SurveyActivity;
import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.db.Activity;
import de.rub.selab22a15.db.ActivityRepository;
import de.rub.selab22a15.db.GPSRepository;
import de.rub.selab22a15.services.ActivityRecordService;
import de.rub.selab22a15.services.LocationRecordService;

public class ActivityFragment extends Fragment {
    private static final String LOG_TAG_ACTIVITY_RECORD = "ACTIVITY_RECORD";

    private ActivityResultLauncher<String> requestPermissionLauncher;


    private TextInputEditText textEditActiveRecordingName;
    private SwitchMaterial switchActiveRecordingLocation;
    private Chronometer chronometerActiveRecordingTime;
    private MaterialButton buttonActiveRecordingStart;
    private MaterialButton buttonActiveRecordingStop;

    private AddRecord addRecord;

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
                        switchActiveRecordingLocation.setChecked(false);
                    }
                });

        FragmentActivity activity = requireActivity();

        // Active Recording Views
        textEditActiveRecordingName = activity.findViewById(R.id.textEditActivityRecordActiveRecording);
        switchActiveRecordingLocation = activity.findViewById(R.id.switchActivityRecordActiveRecordingLocation);
        chronometerActiveRecordingTime = activity.findViewById(R.id.chronometerActivityRecordActiveRecording);
        buttonActiveRecordingStart = activity.findViewById(R.id.buttonActivityRecordActiveRecordingStart);
        buttonActiveRecordingStop = activity.findViewById(R.id.buttonActivityRecordActiveRecordingStop);



        // Active Recording Actions
        switchActiveRecordingLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
                        .setNegativeButton(R.string.stringCancel, (dialog, which) -> switchActiveRecordingLocation.setChecked(false))
                        .setPositiveButton(R.string.stringOkay, (dialog, which) -> requestPermissionLauncher.launch(ACCESS_FINE_LOCATION))
                        .show();
            } else {
                requestPermissionLauncher.launch(ACCESS_FINE_LOCATION);
            }
        });
        buttonActiveRecordingStart.setOnClickListener(v -> startRecord());
        buttonActiveRecordingStop.setOnClickListener(v -> stopRecord());

        addRecord = new AddRecord();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ActivityRecordService.isRunning()) {
            resumeActiveRecording();
            return;
        }

        resetUI();
    }

    private void resumeActiveRecording() {
        if (!ActivityRecordService.isRunning()) {
            Log.w(LOG_TAG_ACTIVITY_RECORD, "resumeIntent() got called while no service is running");
            return;
        }

        activity = ActivityRecordService.getActivity();

        textEditActiveRecordingName.setText(activity.getActivity());
        textEditActiveRecordingName.setInputType(InputType.TYPE_NULL);

        if (LocationRecordService.isRunning()) {
            switchActiveRecordingLocation.setChecked(true);
        }
        switchActiveRecordingLocation.setEnabled(false);

        chronometerActiveRecordingTime.setBase(ActivityRecordService.getTimeElapsedRealtimeStarted());
        chronometerActiveRecordingTime.start();

        buttonActiveRecordingStart.setEnabled(false);
        buttonActiveRecordingStop.setEnabled(true);

        addRecord.disable();
    }

    private void startRecord() {
        String activityName = Objects.requireNonNull(textEditActiveRecordingName.getText()).toString();
        if (activityName.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.toastActivityFragmentStartError), Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        activity = new Activity(System.currentTimeMillis(), activityName);
        blockUI();

        startAccelerometerRecording();
        if (switchActiveRecordingLocation.isChecked()) {
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
        textEditActiveRecordingName.setText("");
        textEditActiveRecordingName.setInputType(InputType.TYPE_CLASS_TEXT);
        switchActiveRecordingLocation.setEnabled(true);
        switchActiveRecordingLocation.setChecked(false);
        chronometerActiveRecordingTime.setBase(SystemClock.elapsedRealtime());
        chronometerActiveRecordingTime.stop();
        buttonActiveRecordingStart.setEnabled(true);
        buttonActiveRecordingStop.setEnabled(false);

        addRecord.enable();
    }

    private void blockUI() {
        textEditActiveRecordingName.setInputType(InputType.TYPE_NULL);
        switchActiveRecordingLocation.setEnabled(false);
        chronometerActiveRecordingTime.setBase(SystemClock.elapsedRealtime());
        chronometerActiveRecordingTime.start();
        buttonActiveRecordingStart.setEnabled(false);
        buttonActiveRecordingStop.setEnabled(true);

        addRecord.disable();
    }

    private void discard() {
        if (activity == null) {
            Log.w(LOG_TAG_ACTIVITY_RECORD, "Warning: this.activity is null");
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
            Log.w(LOG_TAG_ACTIVITY_RECORD, "Warning: this.activity is null");
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

    private class ActiveRecording {

    }

    private class AddRecord {
        private boolean isInitialised;
        private boolean isEnabled;

        private CardView cardViewAddRecord;
        private TextInputEditText textEditAddRecordName;
        private TextInputEditText textEditAddRecordDate;
        private Long timestampAddRecord;

        private void initViews() {
            FragmentActivity activity = requireActivity();

            // Bind Views
            cardViewAddRecord = activity.findViewById(R.id.cardViewActivityRecordAddRecord);
            textEditAddRecordName = activity.findViewById(R.id.textEditActivityRecordAddRecordName);
            textEditAddRecordDate = activity.findViewById(R.id.textEditActivityRecordAddRecordDate);
            MaterialButton buttonAddRecordSave = activity.findViewById(R.id.buttonActivityRecordAddRecordSave);

            // Actions
            textEditAddRecordDate.setOnClickListener(v -> pickDateTime());
            buttonAddRecordSave.setOnClickListener(v -> addRecord());

            isInitialised = true;
        }

        protected void enable() {
            if (!isInitialised) {
                initViews();
            }

            cardViewAddRecord.setVisibility(View.VISIBLE);
            isEnabled = true;
        }

        protected void disable() {
            cardViewAddRecord.setVisibility(View.GONE);
            isEnabled = false;
        }

        private void addRecord() {
            if (textEditAddRecordName.getText() == null) {
                Log.e(LOG_TAG_ACTIVITY_RECORD, "textEditAddRecordName.getText() is null");
                return;
            }

            String activityName = textEditAddRecordName.getText().toString();

            if (activityName.isEmpty()) {
                Toast.makeText(requireContext(), R.string.toastActivityRecordAddRecordSaveActivityMissing, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if (timestampAddRecord == null) {
                Toast.makeText(requireContext(), R.string.toastActivityRecordAddRecordSaveDateMissing, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            new ActivityRepository(requireActivity().getApplication())
                    .insert(new Activity(timestampAddRecord, activityName));
        }

        private void pickTime() {
            Calendar calendar = Calendar.getInstance();

            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTitleText("Select Activity Time")
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE))
                    .build();

            timePicker.addOnPositiveButtonClickListener(v -> {
                timestampAddRecord += TimeUnit.MILLISECONDS.convert(timePicker.getHour(), TimeUnit.HOURS);
                timestampAddRecord += TimeUnit.MILLISECONDS.convert(timePicker.getMinute(), TimeUnit.MINUTES);
                // Timezone is buggy if not done like this
                Long timestampEditText = timestampAddRecord - calendar.getTimeZone().getOffset(timestampAddRecord);
                textEditAddRecordDate.setText(DateFormat.getInstance().format(timestampEditText));
            });

            timePicker.show(getParentFragmentManager(), null);
        }

        private void pickDateTime() {
            // Pick Date
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Activity Date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            datePicker.addOnPositiveButtonClickListener(currentDateInMillis -> {
                timestampAddRecord = currentDateInMillis;

                Log.d("picker", timestampAddRecord.toString());
                pickTime(); // Pick Time
            });
            datePicker.show(getParentFragmentManager(), null);
        }
    }
}