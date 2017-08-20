package com.github.hpeng526.smsforward

import android.app.Service
import android.content.*
import android.os.IBinder
import android.util.Log


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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(this.javaClass.name, "start")
        return START_STICKY
    }

    override fun onDestroy() {
        Log.d(this.javaClass.name, "ForwardService destroy")
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}