package de.rub.selab22a15;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.atomic.AtomicBoolean;

import de.rub.selab22a15.db.AppDatabase;


public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG_FIREBASE_AUTH = "FIREBASE_AUTH";

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Preference prefExport = findPreference(getString(R.string.preferenceScreenUpload));
        assert prefExport != null;
        prefExport.setOnPreferenceClickListener(preference -> uploadDatabase());
    }

    private boolean uploadDatabase() {
        AppDatabase.destroyInstance();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signInAnonymously()
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG_FIREBASE_AUTH, "signInAnonymously:failure",
                                    task.getException());
                            Toast.makeText(requireContext(), "Authentication failed. Try again later",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return false;
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

        return isSuccessful.get();
    }
}