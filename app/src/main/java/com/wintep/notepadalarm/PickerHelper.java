package com.wintep.notepadalarm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import java.util.Calendar;
import java.util.Date;

public class PickerHelper {

    public interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }
    public interface OnTimeSelectedListener {
        void onTimeSelected(Date time);
    }

    public static void showDatePickerDialog(Context context, OnDateSelectedListener listener) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (view, newYear, newMonth, newDay) -> {
                    calendar.set(Calendar.YEAR, newYear);
                    calendar.set(Calendar.MONTH, newMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, newDay);
                    Date selectedDate = calendar.getTime();
                    if (listener != null) {
                        listener.onDateSelected(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public static void showTimePickerDialog(Context context, OnTimeSelectedListener listener) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                (view, newHour, newMinute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, newHour);
                    calendar.set(Calendar.MINUTE, newMinute);
                    Date selectedTime = calendar.getTime();
                    if (listener != null) {
                        listener.onTimeSelected(selectedTime);
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }
}
