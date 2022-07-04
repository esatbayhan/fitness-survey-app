package de.rub.selab22a15.services;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import de.rub.selab22a15.database.research.Activity;
import de.rub.selab22a15.database.research.GPS;
import de.rub.selab22a15.database.research.GPSRepository;
import de.rub.selab22a15.helpers.ServiceNotification;

public class LocationRecordService extends Service {
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static boolean isRunning;
    private static Activity activity;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private GPSRepository gpsRepository;

    public static boolean isRunning() {
        return isRunning;
    }

    public static void setActivity(Activity activity) {
        LocationRecordService.activity = activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION)
                == PERMISSION_DENIED) {
            stopSelf();
        }

        gpsRepository = new GPSRepository(getApplication());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();

                if (location == null) {
                    return;
                }

                if (activity == null) {
                    return;
                }

                GPS gps = new GPS(
                        System.currentTimeMillis(),
                        activity.getTimestamp(),
                        location.getLongitude(),
                        location.getLatitude(),
                        location.getAltitude(),
                        location.getAccuracy(),
                        location.getSpeed());
                gpsRepository.insert(gps);
            }
        };

        createLocationRequest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION)
                == PERMISSION_DENIED) {
            stopSelf();

        }
        isRunning = true;

        startForeground(ServiceNotification.NOTIFICATION_ID, ServiceNotification.getNotification(this));

        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        activity = null;
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
    }
}
