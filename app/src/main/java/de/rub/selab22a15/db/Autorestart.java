package de.rub.selab22a15.db;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Autorestart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Intent serviceIntent = new Intent(context, ForegroundApp.class);
            context.startForegroundService(serviceIntent);
        }
    }
}
