package com.github.hpeng526.smsforward

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.IntentFilter



class ForwardService : Service() {

    override fun onBind(p0: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter()
        filter.addAction("android.provider.Telephony.SMS_RECEIVED")
    }
}