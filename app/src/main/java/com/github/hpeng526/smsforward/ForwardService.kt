package com.github.hpeng526.smsforward

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.IntentFilter
import android.util.Log
import android.app.PendingIntent
import android.graphics.BitmapFactory
import android.graphics.Bitmap


class ForwardService : Service() {

    val receiver = SMSReceiver()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d(this.javaClass.name, "ForwardService create")
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        Log.d(this.javaClass.name, "ForwardService destroy")
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == Const.START_ACTION) {

            Log.d("ForwardService", "Receive from " + intent.toString())

            val notificationIntent = Intent(this, MainActivity::class.java)
            notificationIntent.action = Const.MAIN_ACTION
            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

            val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

            val notification = Notification.Builder(this)
                    .setContentTitle("SMSForward")
                    .setTicker("sms Forwarder")
                    .setContentText("Monitoring...")
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .build()

            startForeground(-1982, notification)
        } else if (intent?.action == Const.STOP_ACTION) {
            Log.d("ForwardService", "Received Stop Foreground Intent")
            stopForeground(true)
            stopSelf()
        }
        return START_STICKY
    }
}