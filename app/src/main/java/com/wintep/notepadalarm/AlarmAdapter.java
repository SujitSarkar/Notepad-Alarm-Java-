package com.wintep.notepadalarm;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    List<AlarmData> alarmDataList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<AlarmData> list) {
        alarmDataList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item_view, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        holder.alarmNote.setText(alarmDataList.get(position).getAlarmNote());
        holder.savingDate.setText(getSavingDate(alarmDataList.get(position).getSavingTime()));
        holder.alarmDate.setText(String.valueOf(alarmDataList.get(position).getOriginalTime()));
    }

    @Override
    public int getItemCount() {
        return alarmDataList.size();
    }

    private String getSavingDate(long dateInMillis) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM, yyyy\nhh:mm aa", Locale.getDefault());
        return dateTimeFormat.format(new Date(dateInMillis));

    }

    private String getAlarmDate(long dateInMillis, String eventOrAlarm) {
        if (eventOrAlarm.equals("alarm")) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            return timeFormat.format(new Date(dateInMillis));
        } else {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM, yyyy\nhh:mm aa", Locale.getDefault());
            return dateTimeFormat.format(new Date(dateInMillis));
        }
    }
}
