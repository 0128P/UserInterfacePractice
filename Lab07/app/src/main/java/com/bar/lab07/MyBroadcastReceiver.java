package com.bar.lab07;

import static androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int intExtra = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1);
        Log.d("ssu", "snooze extra: " + intExtra);
    }
}