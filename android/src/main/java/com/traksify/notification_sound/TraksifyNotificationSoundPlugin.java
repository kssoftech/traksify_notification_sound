package com.traksify.notification_sound;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class TraksifyNotificationSoundPlugin
        implements FlutterPlugin, MethodCallHandler {

    private MethodChannel channel;
    private Context context;

    private static final String CHANNEL_ID = "device_events_v1";
    private static final String CHANNEL_NAME = "Device Events";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        context = binding.getApplicationContext();

        channel = new MethodChannel(
                binding.getBinaryMessenger(),
                "traksify_notification_sound"
        );

        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(
            @NonNull MethodCall call,
            @NonNull Result result) {

        if (call.method.equals("initialize")) {

            createNotificationChannel();

            result.success(true);

        } else {

            result.notImplemented();
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }

        NotificationManager notificationManager =
                (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            return;
        }

        Uri soundUri = Uri.parse(
                "android.resource://"
                        + context.getPackageName()
                        + "/"
                        + com.traksify.notification_sound.R.raw.device_online
        );

        AudioAttributes audioAttributes =
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(
                                AudioAttributes.CONTENT_TYPE_SONIFICATION
                        )
                        .build();

        NotificationChannel notificationChannel =
                new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                );

        notificationChannel.setDescription(
                "Notifications for Traksify device events"
        );

        notificationChannel.setSound(
                soundUri,
                audioAttributes
        );

        notificationChannel.enableVibration(true);

        notificationManager.createNotificationChannel(
                notificationChannel
        );
    }

    @Override
    public void onDetachedFromEngine(
            @NonNull FlutterPluginBinding binding) {

        if (channel != null) {
            channel.setMethodCallHandler(null);
            channel = null;
        }

        context = null;
    }
}
