package com.dicoding.tugasusergithubv2.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.ui.main.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val TYPE_REPEATING = "RepeatingAlarm"
        private const val EXTRA_MESSAGE = "message"

        private const val ID_REPEATING_ALARM = 47
        private const val CHANNEL_ID = "CH_47"
        private const val CHANNEL_NAME = "Channel_Alarm"

        private const val TIME = "09:00"
        private const val MESSAGE = "Reminder"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        showToast(context, message)
        showAlarmNotification(context, message as String)
    }

    fun setRepeatingAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, MESSAGE) // put extra karena akan dikirim ke alarmmanager

        val timeArray = TIME.split(":".toRegex()).dropLastWhile {
            it.isEmpty()
        }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 1)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING_ALARM, intent, 0)

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        )

        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show()
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING_ALARM, intent, 0)
        pendingIntent.cancel()

        // cancel alarm yg ada di alarm manager, cancel ID 101
        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, "Repeating alarm dibatalkan", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(context: Context, message: String?) {
        Toast.makeText(context, "$TYPE_REPEATING: $message", Toast.LENGTH_LONG).show()
    }

    private fun showAlarmNotification(context: Context, message: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0,intent,0)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(TYPE_REPEATING)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_REPEATING_ALARM, notification)
    }
}