package com.wintep.notepadalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

public class AlarmHelper {
    private Context context;
    public AlarmHelper(Context context) {
        this.context = context;
    }

    public void setAlarm(Calendar calendar, String alarmNote, int alarmSoundResourceId, boolean isVibrate) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("alarmNote", alarmNote);
        alarmIntent.putExtra("alarmSoundResourceId", alarmSoundResourceId);
        alarmIntent.putExtra("vibrate", isVibrate);
        alarmIntent.putExtra("savingTime", System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}

