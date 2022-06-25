package de.rub.selab22a15;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import de.rub.selab22a15.db.Accelerometer;
import de.rub.selab22a15.db.AccelerometerRepository;
import de.rub.selab22a15.db.Activity;
import de.rub.selab22a15.db.ActivityRepository;

public class ActivityFragment extends Fragment {
    private TextInputEditText oteActivity;

    private Chronometer cmtActivity;

    private MaterialButton btnStartActivity;
    private MaterialButton btnPauseActivity;
    private MaterialButton btnResumeActivity;
    private MaterialButton btnStopActivity;


    private Activity activity;

    private float accelerometerX, accelerometerY, accelerometerZ;
    private final float EPSILON = 0.1f;
    private long pauseOffset;

    private AccelerometerRepository accelerometerRepository;

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private SensorEventListener accelerometerEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = getActivity();
        oteActivity = activity.findViewById(R.id.oteActivity);

        cmtActivity = activity.findViewById(R.id.cmtActivity);

        btnStartActivity = activity.findViewById(R.id.btnStartActivity);
        btnPauseActivity = activity.findViewById(R.id.btnPauseActivity);
        btnResumeActivity = activity.findViewById(R.id.btnResumeActivity);
        btnStopActivity = activity.findViewById(R.id.btnStopActivity);

        accelerometerRepository = new AccelerometerRepository(activity.getApplication());

        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerEventListener = new AccelerometerEventListener();

        btnStartActivity.setOnClickListener(view1 -> {
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
        reset();
    }

    private void save() {
        this.activity.setActivity(oteActivity.getText().toString());

        new ActivityRepository(getActivity().getApplication())
                .insert(this.activity);

        reset();
    }

    private void startAccelerometerRecording() {
        sensorManager.registerListener(accelerometerEventListener, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopAccelerometerRecording() {
        sensorManager.unregisterListener(accelerometerEventListener);
    }

    private void alertSave() {
        new MaterialAlertDialogBuilder(getContext())
                .setMessage(R.string.alertSaveActivityMessage)
                .setNegativeButton(R.string.alertSaveActivityNegative, (dialogInterface, i) -> {
                    discard();
                })
                .setPositiveButton(R.string.alertSaveActivityPositive, ((dialogInterface, i) -> {
                    save();
                }))
                .show();
    }

    //    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        FragmentActivity activity = getActivity();
//
//        tvAccelerometer = activity.findViewById(R.id.tvAccelerometer);
//        swAccelerometerActivated = activity.findViewById(R.id.swAccelerometerActivated);
//
//        accelerometerRepository = new AccelerometerRepository(activity.getApplication());
//
//        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
//        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        accelerometerEventListener = new AccelerometerEventListener();
//
//        swAccelerometerActivated.setOnCheckedChangeListener((compoundButton, isChecked) -> {
//            if (isChecked) {
//                sensorManager.registerListener(accelerometerEventListener, sensorAccelerometer,
//                        SensorManager.SENSOR_DELAY_NORMAL);
//            }
//            else {
//                sensorManager.unregisterListener(accelerometerEventListener);
//            }
//        });
//    }
//
    class AccelerometerEventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() != Sensor.REPORTING_MODE_ON_CHANGE) {
                return;
            }

            long timestamp = System.currentTimeMillis();
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            if (Math.abs(accelerometerX - x) < EPSILON &&
                    Math.abs(accelerometerY - y) < EPSILON &&
                    Math.abs(accelerometerZ - z) < EPSILON) {
                return;
            }

            accelerometerX = x;
            accelerometerY = y;
            accelerometerZ = z;

            accelerometerRepository.insert(
                    new Accelerometer(timestamp, activity.getTimestamp(), x, y, z));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}