package com.wintep.notepadalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Alarm received");

        // Get alarm note and sound resource ID from intent
        String alarmNote = intent.getStringExtra("alarmNote");
        int alarmSoundResourceId = intent.getIntExtra("alarmSoundResourceId", 0);
        boolean vibrate = intent.getBooleanExtra("vibrate", false);

        // Play alarm sound
        MediaPlayer mediaPlayer = MediaPlayer.create(context, alarmSoundResourceId);
        mediaPlayer.start();
        // Vibrate
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(1000);
            }
        }

        // Show toast with alarm note
        Toast.makeText(context, "Alarm: " + alarmNote, Toast.LENGTH_LONG).show();
    }
}
