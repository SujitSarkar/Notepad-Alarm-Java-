package com.wintep.notepadalarm;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    private List items = new ArrayList();
    private Resources resources;
    private Context context;

    public void setData(List list) {
        items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        resources = context.getResources();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_item_view, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
//        holder.alarmNote.setText(items.get(position).getTitle());
        holder.alarmNote.setText("Note");
        holder.savingDate.setText("22-Mar-2023 12:45 PM");
        holder.alarmDate.setText("22-Mar-2023 12:45 PM");
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
