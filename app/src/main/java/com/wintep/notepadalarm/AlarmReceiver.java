package com.wintep.notepadalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm received");
        // Acquire a partial wake lock to ensure the device stays awake
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "AlarmReceiver:WakeLock");
        wakeLock.acquire(5 * 60 * 1000L); // Acquire the wake lock for 2 minutes

        // Release the wake lock after handling the alarm
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }

        ///Trigger Alarm activity
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.putExtras(intent.getExtras());
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}
