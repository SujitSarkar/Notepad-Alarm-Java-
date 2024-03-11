package com.wintep.notepadalarm;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity{
    private FloatingActionButton addAlarmButton;
    final String[] ringtone = {"Alarm 1", "Alarm 2", "Alarm 3", "Islamic", "Blink"};
    private MediaPlayer mediaPlayer;
    private int alarmResourceId = R.raw.alarm_1;
    private RecyclerView alarmRecyclerView;
    private AlarmAdapter alarmAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Context.UI_MODE_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_main);

        alarmRecyclerView = findViewById(R.id.alarm_recycler_view);
        addAlarmButton = findViewById(R.id.add_alarm_button);

        alarmAdapter = new AlarmAdapter();
        alarmRecyclerView.setAdapter(alarmAdapter);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addAlarmButton.setOnClickListener(v -> showAddAlarmDialog());
    }

    void showAddAlarmDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_alarm_bottom_sheet_layout);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        AtomicReference<Date> alarmDate = null;
        AtomicReference<Date> alarmTime = null;
        final TextView cancelButton = dialog.findViewById(R.id.cancel_button);
        final TextView saveButton = dialog.findViewById(R.id.save_button);
        final EditText alarmNote = dialog.findViewById(R.id.alarm_note);
        final TextView dateTextView = dialog.findViewById(R.id.date_text_view);
        final TextView timeTextView = dialog.findViewById(R.id.time_text_view);
        final ToggleButton playPauseToggleButton = dialog.findViewById(R.id.playPauseToggleButton);
        final Switch vibrateSwitch = dialog.findViewById(R.id.vibrate_switch);
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        //Dropdown variables
        AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.auto_complete_text_view);
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(this,R.layout.dropdown_item_layout,ringtone);
        autoCompleteTextView.setAdapter(adapterItems);

        //Set initial value of dropdown
        autoCompleteTextView.setText("Alarm 1", false);
        mediaPlayer = MediaPlayer.create(this,R.raw.alarm_1);

        //Dropdown button onChange action
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            switch (item){
                case "Alarm 1":
                    mediaPlayer = MediaPlayer.create(this,R.raw.alarm_1);
                    alarmResourceId = R.raw.alarm_1;
                    break;
                case "Alarm 2":
                    mediaPlayer = MediaPlayer.create(this,R.raw.alarm_2);
                    alarmResourceId = R.raw.alarm_2;
                    break;
                case "Alarm 3":
                    mediaPlayer = MediaPlayer.create(this,R.raw.alarm_3);
                    alarmResourceId = R.raw.alarm_3;
                    break;
                case "Islamic":
                    mediaPlayer = MediaPlayer.create(this,R.raw.islamic);
                    alarmResourceId = R.raw.islamic;
                    break;
                case "Blink":
                    mediaPlayer = MediaPlayer.create(this,R.raw.blink);
                    alarmResourceId = R.raw.blink;
                    break;
            }
        });

        ///Toggle play pause button
        playPauseToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if(mediaPlayer!=null){
                    mediaPlayer.start();
                }
            } else {
               if(mediaPlayer!=null && mediaPlayer.isPlaying()){
                   mediaPlayer.pause();
               }
            }
        });

        ///Vibrate Switch
        vibrateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
            }
        });

        ///Date Picker
        dateTextView.setOnClickListener(v -> {
            PickerHelper.showDatePickerDialog(this, date -> {
                alarmDate.set(date);
                SimpleDateFormat dateFormat;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    dateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
                    dateTextView.setText(dateFormat.format(date));
                }

            });
        });

        ///Time Picker
        timeTextView.setOnClickListener(v -> PickerHelper.showTimePickerDialog(MainActivity.this, time -> {
            alarmTime.set(time);
            SimpleDateFormat sdf;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sdf = new SimpleDateFormat("hh:mm aa");
                timeTextView.setText(sdf.format(time));
            }
        }));

        saveButton.setOnClickListener(v -> {
            if(alarmTime.get()==null){
                Toast.makeText(this, "Select Time", Toast.LENGTH_SHORT).show();
                return;
            }
            if(alarmDate.get() == null){
                alarmDate.set(new Date(System.currentTimeMillis()));
            }

            final AlarmHelper alarmHelper = new AlarmHelper(MainActivity.this);
            final Calendar alarmCalendar = Calendar.getInstance();

            alarmCalendar.set(Calendar.YEAR, alarmDate.get().getYear());
            alarmCalendar.set(Calendar.MONTH, alarmDate.get().getMonth());
            alarmCalendar.set(Calendar.DAY_OF_MONTH, alarmDate.get().getDay());
            alarmCalendar.set(Calendar.HOUR_OF_DAY, alarmDate.get().getHours());
            alarmCalendar.set(Calendar.MINUTE, alarmTime.get().getMinutes());
            alarmCalendar.set(Calendar.SECOND, 0);

            alarmHelper.setAlarm(alarmCalendar, String.valueOf(alarmNote.getText()), alarmResourceId, vibrateSwitch.isChecked());
        });
    }
}
