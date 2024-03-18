package ru.artrostudio.mytask.modules

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.artrostudio.mytask.MainActivity
import ru.artrostudio.mytask.R

@SuppressLint("MissingPermission")  // убирает необходимость запроса разрешений, УБЕРИ потом
class MyNotifications(_context: Context) {
    private val context : Context = _context

    // ТУТ ВСЁ ОЧЕНЬ ДЕРЕВЯННО
    // всё погружено в один метод, а не следовало бы
    // нужно сделать красиво

    fun setNotification() {
        // Создаём уведомление
        val NOTIFICATION_ID = 101
        val CHANNEL_ID = "channelID"

        // создание канала уведомлений. Должно быть создано до запуска уведомления. Книжка рекомендует создавать при запуске приложения
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val descriptionText = "channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // второй аргумент - это то, что запустится в приложении
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // объект действия по умолчанию при касании по уведомлению
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // создание объекта-действия при нажатии на кнопку
        val ACTION_SNOOZE = "snooze"
        // второй аргумент - это то, что запустится в приложении
        val snoozeIntent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(NotificationCompat.EXTRA_NOTIFICATION_ID, 0)
        }
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_notification)
            .setContentTitle("Напоминание")
            .setContentText("Пора покормить кота")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.icon_notification, "NAME_BUTT", snoozePendingIntent)


        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}