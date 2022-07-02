package de.rub.selab22a15.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.rub.selab22a15.workers.AccelerometerRecordWorker;
import de.rub.selab22a15.workers.PeriodicNotificationWorker;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            return;
        }

        Log.d("bootrec", "i am inside on receive");

        AccelerometerRecordWorker.start(context);
        PeriodicNotificationWorker.start(context);
    }
}
