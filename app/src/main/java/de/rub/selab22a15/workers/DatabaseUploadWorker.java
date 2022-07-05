package de.rub.selab22a15.workers;

import static de.rub.selab22a15.database.research.ResearchDatabase.DATABASE_NAME_RESEARCH;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.rub.selab22a15.App;

public class DatabaseUploadWorker extends Worker {

    public static void start(Context context) {
        Log.d(LOG_TAG, "Inside start(Context)");

        WorkRequest workRequest = new OneTimeWorkRequest.Builder(DatabaseUploadWorker.class)
                .build();

        WorkManager.getInstance(context)
                .enqueue(workRequest);
    }

    private static final String LOG_TAG = "DATABASE_UPLOAD_WORKER";

    public DatabaseUploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        long timestamp = System.currentTimeMillis();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signInAnonymously();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return Result.failure();
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String databaseReferenceName = System.currentTimeMillis() + "." + DATABASE_NAME_RESEARCH;
        Uri databaseUri = Uri.fromFile(getApplicationContext().getDatabasePath(DATABASE_NAME_RESEARCH));
        StorageReference databaseReference = storageReference.child(
                FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + databaseReferenceName);

        databaseReference.putFile(databaseUri)
                .addOnSuccessListener(taskSnapshot -> {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(
                            App.APPLICATION_PREFERENCES, Context.MODE_PRIVATE);

                    sharedPreferences.edit().putLong(App.KEY_LAST_TIME_UPLOADED, timestamp).apply();
                });

        return Result.success();
    }
}
