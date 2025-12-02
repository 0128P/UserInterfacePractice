package com.bar.lab07;

import static androidx.core.app.NotificationCompat.EXTRA_NOTIFICATION_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bar.lab07.databinding.ActivityLab073Binding;

// https://developer.android.com/develop/ui/views/notifications/build-notification
public class Lab07_3Activity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLab073Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLab073Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.basicButton.setOnClickListener(this);
        binding.bigTextButton.setOnClickListener(this);
        binding.bigPictureButton.setOnClickListener(this);
        binding.progressButton.setOnClickListener(this);
        binding.actionButton.setOnClickListener(this);
        binding.remoteInputButton.setOnClickListener(this);

        requestPermissions();
    }

    @Override
    public void onClick(View v) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this); // required requestPermissions

        //채널 생성
        String channelId = "one-channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "My Channel One";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelDescription = "My Channel One Description";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);

            manager.createNotificationChannel(channel);
        }

        //빌더 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
        builder.setContentTitle("메시지 도착");
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.noti_large);
        builder.setLargeIcon(largeIcon);

        //basic notification
        if (v == binding.basicButton) {
            String bigText = getString(R.string.big_text);

            builder.setContentText(bigText);

            notify(manager, builder);
        }

        //big text style notification
        else if (v == binding.bigTextButton) {
            String bigText = getString(R.string.big_text);

            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(bigText));

            notify(manager, builder);
        }

        //big picture Style
        else if (v == binding.bigPictureButton) {
            Bitmap bigPicture = BitmapFactory.decodeResource(getResources(), R.drawable.noti_big);

            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bigPicture));

            notify(manager, builder);
        }

        //add a progress bar
        else if (v == binding.progressButton) {
            Thread t = new Thread(() -> {
                for (int i = 1; i <= 10; i++) {
                    builder.setProgress(10, i, false);

                    notify(manager, builder);

                    SystemClock.sleep(1000);
                }
                builder.setContentText("Download complete").setProgress(0,0,false);
                notify(manager, builder);
            });
            t.start();
        }

        //add an action button
        else if (v == binding.actionButton) {
            Intent intent = new Intent(this, MyBroadcastReceiver.class);
            intent.putExtra(EXTRA_NOTIFICATION_ID, 0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            builder.addAction(0, "snooze", pendingIntent);

            notify(manager, builder);
        }

        //add a direct reply action
        else if (v == binding.remoteInputButton) {
            //RemoteInput
            String KEY_TEXT_REPLY = "key_text_reply";
            String replyLabel = "Please enter your reply."; // hint text
            //androidx.core.app.RemoteInput 이용
            RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                    .setLabel(replyLabel)
                    .build();

            Intent intent = new Intent(this, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 20, intent, PendingIntent.FLAG_MUTABLE);

            NotificationCompat.Action.Builder actionBuilder = new NotificationCompat.Action.Builder(
                    0,
                    "답장",
                    pendingIntent
            );
            actionBuilder.addRemoteInput(remoteInput);

            builder.addAction(actionBuilder.build());

            notify(manager, builder);
        }
    }

    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1000);
        }
    }

    private void notify(NotificationManagerCompat manager, NotificationCompat.Builder builder) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            return;
        }

        manager.notify(222, builder.build());
    }
}