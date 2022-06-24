package de.rub.selab22a15;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import de.rub.selab22a15.db.GPS;
import de.rub.selab22a15.db.GPSRepository;

public class GPSActivity extends AppCompatActivity {
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    public static final int PERMISSIONS_FINE_LOCATION = 99;

    GPSRepository gpsRepository;

    TextView tvLatitude;
    TextView tvLongitude;
    TextView tvAltitude;
    TextView tvAccuracy;
    TextView tvGPSSpeed;

    Switch swGPSHighAccuracy;
    Switch swGPSEnabled;

    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        gpsRepository = new GPSRepository(getApplication());

        tvLatitude = findViewById(R.id.tvLongitude);
        tvLongitude = findViewById(R.id.tvLatitude);
        tvAltitude = findViewById(R.id.tvAltitude);
        tvAccuracy = findViewById(R.id.tvAccuracy);
        tvGPSSpeed = findViewById(R.id.tvGPSSpeed);

        swGPSHighAccuracy = findViewById(R.id.swGPSHighAccuracy);
        swGPSEnabled = findViewById(R.id.swGPSEnabled);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                saveToDatabase(locationResult.getLastLocation());
                updateUIValues(locationResult.getLastLocation());
            }
        };

        swGPSHighAccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swGPSHighAccuracy.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }
            }
        });

        swGPSEnabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swGPSEnabled.isChecked()) {
                    startLocationUpdates();
                } else {
                    stopLocationUpdates();
                }
            }
        });

        updateGPS();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();
    }

    private void stopLocationUpdates() {
        tvLatitude.setText("Not tracking location");
        tvLongitude.setText("Not tracking location");
        tvAltitude.setText("Not tracking location");
        tvAccuracy.setText("Not tracking location");
        tvGPSSpeed.setText("Not tracking location");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            updateGPS();
        } else {
            Toast.makeText(this, "This app requires permission to be granted in order to work properly", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveToDatabase(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        Double altitude = (location.hasAltitude()) ? location.getAltitude() : null;
        Float accuracy = (location.hasAccuracy()) ? location.getAccuracy() : null;
        Float speed = (location.hasSpeed()) ? location.getSpeed() : null;

        GPS gps = new GPS(System.currentTimeMillis(), latitude, longitude, altitude, accuracy, speed);
        gpsRepository.insert(gps);
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GPSActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, this::updateUIValues);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateUIValues(Location location) {
        tvLatitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
        tvLongitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
        tvAccuracy.setText("Accuracy: " + String.valueOf(location.getAccuracy()));

        if (location.hasAltitude()) {
            tvAltitude.setText("Altitude: " + String.valueOf(location.getAltitude()));
        } else {
            tvAltitude.setText("Altitude: Not available");
        }

        if (location.hasSpeed()) {
            tvGPSSpeed.setText("Speed: " + String.valueOf(location.getSpeed()));
        } else {
            tvGPSSpeed.setText("Speed: Not available");
        }
    }
}