package com.example.trainbookingapp;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationScheduler {
    public static void scheduleNotification(Context context, long bookedDateInMillis) {
        long oneDayInMillis = 24 * 60 * 60 * 1000; // 1 day in milliseconds
        long notificationTime = bookedDateInMillis - oneDayInMillis;

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, notificationTime, pendingIntent);
    }
}
