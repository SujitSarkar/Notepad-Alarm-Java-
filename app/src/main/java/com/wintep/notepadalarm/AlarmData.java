package com.wintep.notepadalarm;

public class AlarmData {
    private String alarmNote;
    private int alarmResourceId;
    private boolean isVibrate;
    private String eventOrAlarm;
    private long savingTime;
    private long originalTime;

    public AlarmData(String alarmNote, int alarmResourceId, boolean isVibrate, String eventOrAlarm, long savingTime, long originalTime) {
        this.alarmNote = alarmNote;
        this.alarmResourceId = alarmResourceId;
        this.isVibrate = isVibrate;
        this.eventOrAlarm = eventOrAlarm;
        this.savingTime = savingTime;
        this.originalTime = originalTime;
    }

    public String getAlarmNote() {
        return alarmNote;
    }

    public int getAlarmResourceId() {
        return alarmResourceId;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public String getEventOrAlarm() {
        return eventOrAlarm;
    }

    public long getSavingTime() {
        return savingTime;
    }

    public long getOriginalTime() {
        return originalTime;
    }
}

