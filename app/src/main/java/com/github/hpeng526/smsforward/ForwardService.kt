package com.github.hpeng526.smsforward

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.IntentFilter
import android.util.Log


class ForwardService : Service() {

    val receiver = SMSReceiver()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ForwardService", "Receive from " + intent.toString())
        val notification = Notification.Builder(this)
                .setContentTitle("SMSForward")
                .setTicker("sms Forwarder")
                .setContentText("Monitoring...")
                .setOngoing(true)
                .build()
        startForeground(101, notification)
        return START_STICKY
    }
}