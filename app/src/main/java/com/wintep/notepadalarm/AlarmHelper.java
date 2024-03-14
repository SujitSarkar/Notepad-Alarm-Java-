package com.wintep.notepadalarm;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class AlarmHelper {

    private static final String PREF_NAME = "AlarmTagsPref";
    private static final String TAG_LIST_KEY = "tagList";
    private final Context context;
    public AlarmHelper(Context context) {
        this.context = context;
    }

    public void setPeriodicAlarm(Calendar calendar, String alarmNote, int alarmResourceId, boolean isVibrate, String eventOrAlarm) {
        final String tag = String.valueOf(calendar.getTimeInMillis());
        saveTag(tag);
        final long repeatInterval = TimeUnit.DAYS.toMillis(eventOrAlarm.equals("alarm")?1:365);
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(AlarmWorker.class, repeatInterval, TimeUnit.MILLISECONDS)
                .setInputData(createInputData(alarmNote, alarmResourceId, isVibrate, eventOrAlarm, calendar.getTimeInMillis()))
                .setInitialDelay(calendar.getTimeInMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addTag(tag)
                .build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }

    public void setOneTimeAlarm(Calendar calendar, String alarmNote, int alarmResourceId, boolean isVibrate, String eventOrAlarm) {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AlarmWorker.class)
                .setInputData(createInputData(alarmNote, alarmResourceId, isVibrate, eventOrAlarm, calendar.getTimeInMillis()))
                .setInitialDelay(calendar.getTimeInMillis() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addTag(String.valueOf(calendar.getTimeInMillis()))
                .build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }

    private Data createInputData(String alarmNote, int alarmResourceId, boolean isVibrate, String eventOrAlarm, long originalTime) {
        return new Data.Builder()
                .putString("alarmNote", alarmNote)
                .putInt("alarmResourceId", alarmResourceId)
                .putBoolean("vibrate", isVibrate)
                .putString("eventOrAlarm", eventOrAlarm)
                .putLong("savingTime", System.currentTimeMillis())
                .putLong("originalTime", originalTime)
                .build();
    }

    private void saveTag(String tag) {
        final SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<String> tagList = getTagList();
        tagList.add(tag);
        preferences.edit().putStringSet(TAG_LIST_KEY, new HashSet<>(tagList)).apply();
    }

    private List<String> getTagList() {
        final SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Set<String> tagSet = preferences.getStringSet(TAG_LIST_KEY, new HashSet<>());
        return new ArrayList<>(tagSet);
    }

    public List<AlarmData> getWorkList() {
        List<WorkInfo> workList = new ArrayList<>();
        List<String> tagList = getTagList();
        final WorkManager workManager = WorkManager.getInstance(context);
        for (String tag : tagList) {
            try {
                workList.addAll(workManager.getWorkInfosByTag(tag).get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<AlarmData> alarmDataList = new ArrayList<>();

        for (WorkInfo workInfo : workList) {
            Data inputData = workInfo.getOutputData();
            String alarmNote = inputData.getString("alarmNote");
            int alarmResourceId = inputData.getInt("alarmResourceId", 0);
            boolean isVibrate = inputData.getBoolean("vibrate", true);
            String eventOrAlarm = inputData.getString("eventOrAlarm");
            long savingTime = inputData.getLong("savingTime", System.currentTimeMillis());
            long originalTime = inputData.getLong("originalTime", System.currentTimeMillis());

            AlarmData alarmData = new AlarmData(alarmNote, alarmResourceId, isVibrate, eventOrAlarm, savingTime, originalTime);
            alarmDataList.add(alarmData);

//            Log.d("alarmNote",alarmData.getAlarmNote());
//            Log.d("alarmResourceId", String.valueOf(alarmData.getAlarmResourceId()));
//            Log.d("vibrate", String.valueOf(alarmData.isVibrate()));
//            Log.d("eventOrAlarm",alarmData.getAlarmNote());
//            Log.d("savingTime", String.valueOf(alarmData.getSavingTime()));
//            Log.d("originalTime", String.valueOf(alarmData.getOriginalTime()));
        }
        return alarmDataList;
    }
}

