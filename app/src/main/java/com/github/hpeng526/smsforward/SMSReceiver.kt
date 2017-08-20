package com.github.hpeng526.smsforward

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.github.hpeng526.smsforward.networking.postJson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent?.action
        Log.d(this.javaClass.name, context?.toString())

        if (action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMsg = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            smsMsg.forEach({ sms ->

                val messageBody = sms.messageBody.trim().replace("\n", "\\n")
                val address = sms.originatingAddress
                val message = "[$address] $messageBody"

                val phone = context!!.getSharedPreferences("data", Context.MODE_PRIVATE).getString("phone", "")

                val curTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                val jsonData = "{\"user_id\":100,\"url\":\"http://z.cn\",\"data\":{\"first\":{\"value\":\"短信通知\",\"color\":\"#173177\"},\"send\":{\"value\":\"来自: $phone\",\"color\":\"#173177\"},\"text\":{\"value\":\"$message\",\"color\":\"#173177\"},\"time\":{\"value\":\"$curTime\",\"color\":\"#173177\"}}}"

                Log.d(this.javaClass.name, jsonData)

                postJson(Const.GATEWAY_URL, jsonData, object : Callback {

                    var console = context!!.getSharedPreferences("data", Context.MODE_PRIVATE).getString("console", "")

                    val editor = context!!.getSharedPreferences("data", Context.MODE_PRIVATE).edit()

                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        Log.e(this.javaClass.name, e.message)
                        console = console + "\nerror: " + e.message
                        editor.putString("console", console)
                        editor.apply()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val respString = response.body()?.string().toString()
                        Log.d(this.javaClass.name, respString)
                        console = console + "\nresp: " + respString
                        editor.putString("console", console)
                        editor.apply()
                        response.close()
                    }
                })
            })

        }
    }


}