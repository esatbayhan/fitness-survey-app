package de.rub.selab22a15.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.rub.selab22a15.workers.AccelerometerRecordWorker;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            return;
        }

        AccelerometerRecordWorker.start(context);
        SurveyAlarmReceiver.setAlarm(context);
    }
}
