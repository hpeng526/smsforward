package com.github.hpeng526.smsforward

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SMSReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val action = intent!!.action

        if (action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMsg = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            smsMsg.forEach({ sms ->

                val messageBody = sms.messageBody
                val address = sms.originatingAddress
                val message = "[$address] $messageBody"

                print(message)

            })
        }
    }


}