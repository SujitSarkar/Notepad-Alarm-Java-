package com.wintep.notepadalarm;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AlarmWorker extends Worker {
    public AlarmWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d("doWork","doWork Method called");

        PowerManager powerManager = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp:AlarmWakeLock");
        wakeLock.acquire(10*60*1000L /*10 minutes*/);

        final Context context = getApplicationContext();
        final Intent alarmIntent = new Intent(context, AlarmActivity.class);

        final Data extras = getInputData();
        final String alarmNote = extras.getString("alarmNote");
        final int alarmResourceId = extras.getInt("alarmResourceId", 0);
        final boolean isVibrate = extras.getBoolean("vibrate", true);
        final String eventOrAlarm = extras.getString("eventOrAlarm");
        final long originalTime = extras.getLong("originalTime", System.currentTimeMillis());
        final long savingTime = extras.getLong("savingTime", System.currentTimeMillis());

        alarmIntent.putExtra("alarmNote", alarmNote);
        alarmIntent.putExtra("alarmResourceId", alarmResourceId);
        alarmIntent.putExtra("vibrate", isVibrate);
        alarmIntent.putExtra("eventOrAlarm", eventOrAlarm);
        alarmIntent.putExtra("originalTime", originalTime);
        alarmIntent.putExtra("savingTime", savingTime);

        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
        return Result.success();
    }
}
