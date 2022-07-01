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

import de.rub.selab22a15.db.GPS;
import de.rub.selab22a15.db.GPSRepository;

public class LocationRecordService extends Service {
    private static boolean isRunning;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private GPSRepository gpsRepository;

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        gpsRepository = new GPSRepository(getApplication());
        locationRequest = LocationRequest.create();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    GPS gps = new GPS(
                            System.currentTimeMillis(),
                            location.getLongitude(),
                            location.getLatitude(),
                            location.getAltitude(),
                            location.getAccuracy(),
                            location.getSpeed());
                    gpsRepository.insert(gps);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION)
                == PERMISSION_DENIED) {
            onDestroy();
        }

        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
