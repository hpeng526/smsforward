package com.github.hpeng526.smsforward

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log

/**
 * @author 于晓飞
 * @date   2017/08/19
 * 后台守护进程Service，当前台 ForwardService 被销毁的时候再把它拉起来
 */
class BackgroundService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.action = Const.MAIN_ACTION
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val notification = Notification.Builder(this)
                .setContentTitle("SMSBackground")
                .setTicker("sms Forwarder")
                .setContentText("Monitoring...")
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .build()

        startForeground(1, notification)
        raiseForeground()
        return START_STICKY
    }

    fun raiseForeground() {
        val intent = Intent(this, ForwardService::class.java)
        bindService(intent,object : ServiceConnection {
            override fun onServiceDisconnected(p0: ComponentName?) {
                raiseForeground()
            }

            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

            }
        }, Context.BIND_AUTO_CREATE)
    }
}