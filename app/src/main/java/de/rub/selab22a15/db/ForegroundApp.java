package de.rub.selab22a15.db;



import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.rub.selab22a15.AccelerometerTestActivity;
import de.rub.selab22a15.R;

public class ForegroundApp extends Service {
    private float accelerometerX, accelerometerY, accelerometerZ;
    private TextView tvAccelerometer;
    private Switch swAccelerometerActivated;
    private AccelerometerRepository accelerometerRepository;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true){

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

                                    if (accelerometerX == x && accelerometerY == y && accelerometerZ == z) {
                                        return;
                                    }

                                    accelerometerX = x;
                                    accelerometerY = y;
                                    accelerometerZ = z;

                                    tvAccelerometer.setText(String.format("x: %f y %f: z: %f", x, y, z));
                                    accelerometerRepository.insert(new Accelerometer(timestamp, x, y, z));
                                }

                                @Override
                                public void onAccuracyChanged(Sensor sensor, int i) {

                                }
                            }

                            try{
                                Thread.sleep(2000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder notification = new Notification.Builder(this, CHANNELID).
                setContentText("Service is running").
                setContentTitle("Service is enabled").
                setSmallIcon(R.drawable.ic_launcher_background);

        startForeground(1001, notification.build() );
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}






