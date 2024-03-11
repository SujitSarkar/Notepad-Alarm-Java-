package com.wintep.notepadalarm;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmViewHolder extends RecyclerView.ViewHolder {
    public TextView alarmNote, savingDate, alarmDate;

    public AlarmViewHolder(@NonNull View itemView) {
        super(itemView);
        alarmNote = itemView.findViewById(R.id.alarm_note);
        savingDate = itemView.findViewById(R.id.saving_date);
        alarmDate = itemView.findViewById(R.id.alarm_date);
    }
}
