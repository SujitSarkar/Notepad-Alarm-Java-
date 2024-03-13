package com.wintep.notepadalarm;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {
    String alarmNote;
    String eventOrAlarm;
    int alarmResourceId;
    boolean isVibrate;
    long originalTime;
    long savingTime;
    TextView alarmTimeTextView, alarmNoteTextView;
    Button snoozeButton, stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmTimeTextView = findViewById(R.id.alarmTime);
        alarmNoteTextView = findViewById(R.id.alarmNote);
        snoozeButton = findViewById(R.id.snoozeButton);
        stopButton = findViewById(R.id.stopButton);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        if (intent != null) {
            alarmNote = intent.getStringExtra("alarmNote");
            eventOrAlarm = intent.getStringExtra("eventOrAlarm");
            alarmResourceId = intent.getIntExtra("alarmResourceId",0);
            isVibrate = intent.getBooleanExtra("vibrate", true);
            originalTime = intent.getLongExtra("originalTime",System.currentTimeMillis());
            savingTime = intent.getLongExtra("savingTime",System.currentTimeMillis());

            alarmTimeTextView.setText(getFormattedTime());
            alarmNoteTextView.setText(alarmNote);

            Date date = new Date(originalTime);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Log.d("originalTime Year", String.valueOf(calendar.get(Calendar.YEAR)));
            Log.d("originalTime Month",String.valueOf(calendar.get(Calendar.MONTH) + 1));
            Log.d("originalTime Day",String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            Log.d("originalTime Hour",String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
            Log.d("originalTime Minute",String.valueOf(calendar.get(Calendar.MINUTE)));
            Log.d("alarmNote",String.valueOf(alarmNote));
            Log.d("eventOrAlarm",String.valueOf(eventOrAlarm));
            Log.d("alarmResourceId",String.valueOf(alarmResourceId));
            Log.d("vibrate",String.valueOf(isVibrate));

            playRingtone();

            snoozeButton.setOnClickListener(view -> snoozeAlarm());
            stopButton.setOnClickListener(view -> stopAlarm());
        }
    }

    void playRingtone(){
        try {
            // Play alarm sound
            MediaPlayer mediaPlayer = MediaPlayer.create(this, alarmResourceId);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            } else {
                Log.e(TAG, "Failed to create MediaPlayer with invalid resource ID: " + alarmResourceId);
            }

            // Vibrate if required
            if (isVibrate) {
                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    // Vibrate continuously
                    vibrator.vibrate(new long[]{0, 1000}, 0);
                } else {
                    Log.e(TAG, "Failed to vibrate: Vibrator service not available");
                }
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Resource not found: " + e.getMessage());
        }
    }

    private String getFormattedTime() {
        if (eventOrAlarm.equals("alarm")) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            return timeFormat.format(new Date(originalTime));
        } else {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM, yyyy - hh:mm aa", Locale.getDefault());
            return dateTimeFormat.format(new Date(originalTime));
        }
    }

    private void snoozeAlarm() {}

    private void stopAlarm() {
        if (eventOrAlarm.equals("alarm")) {

        } else {

        }
    }
}