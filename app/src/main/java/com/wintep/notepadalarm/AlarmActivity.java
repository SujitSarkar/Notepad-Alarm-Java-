package com.wintep.notepadalarm;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    Vibrator vibrator;
    String alarmNote,eventOrAlarm;
    int alarmResourceId;
    boolean isVibrate;
    long originalTime,savingTime;
    TextView alarmTimeTextView, alarmNoteTextView;
    Button snoozeButton, stopButton;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(vibrator!=null && vibrator.hasVibrator()){
            vibrator.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmTimeTextView = findViewById(R.id.alarmTime);
        alarmNoteTextView = findViewById(R.id.alarmNote);
        snoozeButton = findViewById(R.id.snoozeButton);
        stopButton = findViewById(R.id.stopButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        // Get the Intent that started this activity
        final Intent intent = getIntent();
        if (intent != null) {
            alarmNote = intent.getStringExtra("alarmNote");
            eventOrAlarm = intent.getStringExtra("eventOrAlarm");
            alarmResourceId = intent.getIntExtra("alarmResourceId", 0);
            isVibrate = intent.getBooleanExtra("vibrate", true);
            originalTime = intent.getLongExtra("originalTime", System.currentTimeMillis());
            savingTime = intent.getLongExtra("savingTime", System.currentTimeMillis());

            alarmTimeTextView.setText(getFormattedTime());
            alarmNoteTextView.setText(alarmNote.isEmpty()?"Alarm":alarmNote);

            playRingtone();

            snoozeButton.setOnClickListener(view -> snoozeAlarm());
            stopButton.setOnClickListener(view -> stopAlarm());
        }
    }

    void playRingtone() {
        try {
            // Play alarm sound
            mediaPlayer = MediaPlayer.create(this, alarmResourceId);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            } else {
                Log.e(TAG, "Failed to create MediaPlayer with invalid resource ID: " + alarmResourceId);
            }

            // Vibrate if required
            if (isVibrate) {
                vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
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
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM, yyyy\nhh:mm aa", Locale.getDefault());
            return dateTimeFormat.format(new Date(originalTime));
        }
    }

    private void snoozeAlarm() {
        final Calendar currentDateCalender = Calendar.getInstance();
        currentDateCalender.add(Calendar.MINUTE, 2);

        final AlarmHelper alarmHelper = new AlarmHelper(this);

        if(eventOrAlarm.equals("event")){
            alarmHelper.setOneTimeAlarm(currentDateCalender, String.valueOf(alarmNote), alarmResourceId, isVibrate,"event");
            Toast.makeText(this, "Snoozed for 5 minute", Toast.LENGTH_LONG).show();
        }else{
            alarmHelper.setOneTimeAlarm(currentDateCalender, String.valueOf(alarmNote), alarmResourceId, isVibrate,"alarm");
            Toast.makeText(this, "Snoozed for 5 minute", Toast.LENGTH_LONG).show();
        }
        stopAlarm();
    }

    private void stopAlarm() {
        if(mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        if(vibrator!=null && vibrator.hasVibrator()){
            vibrator.cancel();
        }
        final Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}