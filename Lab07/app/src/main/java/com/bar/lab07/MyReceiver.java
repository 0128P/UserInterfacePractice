package com.bar.lab07;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.RemoteInput;

public class MyReceiver extends BroadcastReceiver {
    private static final String KEY_TEXT_REPLY = "key_text_reply";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

        if (remoteInput != null) {
            String replyTxt = remoteInput.getString(KEY_TEXT_REPLY);
            Log.d("ssu", "receiver...." + replyTxt);
        }
    }
}