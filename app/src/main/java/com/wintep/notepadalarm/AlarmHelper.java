package com.wintep.notepadalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmHelper {
    private final Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    public void setAlarm(Calendar calendar, String alarmNote, int alarmResourceId, boolean isVibrate, String eventOrAlarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Long originalTime = calendar.getTimeInMillis();
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarmNote", alarmNote);
        alarmIntent.putExtra("alarmResourceId", alarmResourceId);
        alarmIntent.putExtra("vibrate", isVibrate);
        alarmIntent.putExtra("eventOrAlarm", eventOrAlarm);
        alarmIntent.putExtra("savingTime", System.currentTimeMillis());
        alarmIntent.putExtra("originalTime", originalTime);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /* public void setRepeatingAlarm(Calendar calendar, String alarmNote, int alarmSoundResourceId, boolean isVibrate, String eventOrAlarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarmNote", alarmNote);
        alarmIntent.putExtra("alarmSoundResourceId", alarmSoundResourceId);
        alarmIntent.putExtra("vibrate", isVibrate);
        alarmIntent.putExtra("eventOrAlarm", eventOrAlarm);
        alarmIntent.putExtra("savingTime", System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set repeating alarm for every day at the specified time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    } */

}

