package com.example.tribune_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tribune_app.utils.RANDOM_NOTIFICATION
import com.example.tribune_app.utils.SIMPLE_NOTIFY_ID
import com.example.tribune_app.utils.UPLOAD_CHANEL_ID
import java.util.*

object NotificationHelper {
    private var channelCreated = false
    private var lastNotificationId: Int? = null


    fun simpleNotification(context: Context, title: String, text: String) {
        createNotificationChannelIfNotCreated(context)
        val intent = Intent(context, FeedActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, SIMPLE_NOTIFY_ID, intent, 0)

        val builder = createBuilder(
            context,
            title,
            text
        ).setContentIntent(pendingIntent)

        showNotification(context, builder)
    }

    private fun showNotification(
        context: Context,
        builder: NotificationCompat.Builder
    ) {
        with(NotificationManagerCompat.from(context)) {
            val notificationId = Random().nextInt(RANDOM_NOTIFICATION)
            lastNotificationId = notificationId
            notify(notificationId, builder.build())
        }
    }

    private fun createBuilder(
        context: Context,
        title: String,
        content: String
    ): NotificationCompat.Builder =
        NotificationCompat.Builder(context, UPLOAD_CHANEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)

    private fun createNotificationChannelIfNotCreated(context: Context) {
        if (!channelCreated) {
            createNotificationChannel(context)
            channelCreated = true
        }
    }

    private fun createNotificationChannel(context: Context) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            UPLOAD_CHANEL_ID,
            R.string.notification_channel_name.toString(),
            importance
        ).apply {
            description = R.string.notification_channel_description.toString()
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}