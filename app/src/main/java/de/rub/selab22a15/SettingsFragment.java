package de.rub.selab22a15;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.atomic.AtomicBoolean;

import de.rub.selab22a15.db.AppRepository;
import de.rub.selab22a15.receivers.SurveyAlarmReceiver;
import de.rub.selab22a15.services.ActivityRecordService;
import de.rub.selab22a15.workers.AccelerometerRecordWorker;

public class SettingsFragment extends PreferenceFragmentCompat {

    // BATTERY MODES
    public static final String BATTERY_USAGE_LOW = "0";
    public static final String BATTERY_USAGE_MEDIUM = "1";
    public static final String BATTERY_USAGE_HIGH = "2";

    // SURVEY SCHEDULE
    public static final String SURVEY_SCHEDULE_MORNING = "0";
    public static final String SURVEY_SCHEDULE_AFTERNOON = "1";
    public static final String SURVEY_SCHEDULE_EVENING = "2";
    public static final String SURVEY_SCHEDULE_NIGHT = "3";

    private static final String LOG_TAG = "PREFERENCES";

    // Shared Preferences Keys
    public static final String KEY_LANGUAGE = "LANGUAGE";
    public static final String KEY_PASSIVE_RECORDING = "PASSIVE_RECORDING";
    public static final String KEY_BATTERY = "BATTERY";
    public static final String KEY_SURVEY_SCHEDULE = "SURVEY_SCHEDULE";
    public static final String KEY_UPLOAD = "UPLOAD";
    public static final String KEY_DELETE = "DELETE";

    // Default Shared Preferences Values
    public static final String DEFAULT_SURVEY_SCHEDULE = SURVEY_SCHEDULE_EVENING;

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener = (sharedPreferences, key) -> {
            switch (key) {
                case KEY_PASSIVE_RECORDING:
                    passiveRecordingHandler(sharedPreferences);
                    break;
                case KEY_SURVEY_SCHEDULE:
                    SurveyAlarmReceiver.setAlarm(requireContext());
                    break;
            }
        };

        PreferenceManager.getDefaultSharedPreferences(requireContext())
                .registerOnSharedPreferenceChangeListener(listener);

        Preference buttonUpload = findPreference(KEY_UPLOAD);
        if (buttonUpload != null) {
            buttonUpload.setOnPreferenceClickListener(preference -> {
                uploadDatabase();
                return true;
            });
        }

        Preference buttonDelete = findPreference(KEY_DELETE);
        if (buttonDelete != null) {
            buttonDelete.setOnPreferenceClickListener(preference -> {
                deleteDataDialog();
                return true;
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        PreferenceManager.getDefaultSharedPreferences(requireContext())
                .unregisterOnSharedPreferenceChangeListener(listener);
    }

    private void passiveRecordingHandler(SharedPreferences sharedPreferences) {
        boolean isGranted = sharedPreferences.getBoolean(KEY_PASSIVE_RECORDING, false);

        if (isGranted) {
            AccelerometerRecordWorker.start(requireContext());
        } else {
            AccelerometerRecordWorker.stop(requireContext());
        }
    }

    private void uploadDatabase() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signInAnonymously()
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInAnonymously:failure",
                                    task.getException());
                            Toast.makeText(requireContext(), "Authentication failed. Try again later",
                                            Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        AtomicBoolean isSuccessful = new AtomicBoolean(false);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String databaseReferenceName = System.currentTimeMillis() + "." + getString(R.string.appDatabaseName);
        Uri databaseUri = Uri.fromFile(requireActivity().getDatabasePath(getString(R.string.appDatabaseName)));
        StorageReference databaseReference = storageReference.child(
                FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + databaseReferenceName);

        databaseReference.putFile(databaseUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(getContext(), getString(R.string.toastPreferenceUploadSuccess), Toast.LENGTH_LONG)
                            .show();
                    isSuccessful.set(true);
                })
                .addOnFailureListener(taskSnapshot -> {
                    Toast.makeText(getContext(), getString(R.string.toastPreferenceUploadFailure), Toast.LENGTH_LONG)
                            .show();
                    isSuccessful.set(false);
                });
    }

    private void deleteDataDialog() {
        if (ActivityRecordService.isRunning()) {
            Toast.makeText(requireContext(), R.string.toastPreferenceDeleteAbortText, Toast.LENGTH_LONG)
                    .show();
            return;
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.alertPreferencesDeleteTitle)
                .setMessage(R.string.alertPreferencesDeleteMessage)
                .setPositiveButton(R.string.stringDelete, (dialog, which) -> deleteData())
                .setNeutralButton(R.string.stringCancel, (dialog, which) -> {})
                .show();
    }

    private void deleteData() {
        AppRepository.clearDatabase(requireContext());
        Toast.makeText(requireContext(), R.string.toastPreferencesDeleteText, Toast.LENGTH_SHORT)
                .show();
    }
}