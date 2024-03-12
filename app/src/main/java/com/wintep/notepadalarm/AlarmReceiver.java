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

        ///Trigger Alarm activity
        Intent alarmIntent = new Intent(context, AlarmActivity.class);

        alarmIntent.putExtra("alarmNote", intent.getStringExtra("alarmNote"));
        alarmIntent.putExtra("alarmResourceId", intent.getIntExtra("alarmResourceId",0));
        alarmIntent.putExtra("vibrate", intent.getBooleanExtra("vibrate", true));
        alarmIntent.putExtra("eventOrAlarm", intent.getStringExtra("eventOrAlarm"));
        alarmIntent.putExtra("savingTime", intent.getLongExtra("savingTime",System.currentTimeMillis()));
        alarmIntent.putExtra("originalTime", intent.getLongExtra("originalTime",System.currentTimeMillis()));

        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}
