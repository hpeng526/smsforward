package com.github.hpeng526.smsforward

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(this.javaClass.name, "MainActivity create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = Handler(Looper.getMainLooper())
        val phoneNumText = getSharedPreferences("data", Context.MODE_PRIVATE).getString("phone", "")
        val consoleText = getSharedPreferences("data", Context.MODE_PRIVATE).getString("console", "")
        val editText = findViewById(R.id.phoneText) as EditText?
        val respText = findViewById(R.id.respTextView) as TextView?
        editText!!.setText(phoneNumText, TextView.BufferType.EDITABLE)
        respText!!.setText(consoleText, TextView.BufferType.SPANNABLE)

        val forwardServiceIntent = Intent(this, ForwardService::class.java)
        forwardServiceIntent.action = Const.START_ACTION
        startService(forwardServiceIntent)
    }

    override fun onDestroy() {
        Log.d(this.javaClass.name, "MainActivity destroy")
        super.onDestroy()
        val forwardServiceIntent = Intent(this, ForwardService::class.java)
        forwardServiceIntent.action = Const.START_ACTION
        startService(forwardServiceIntent)
    }

    fun clearConsole(v: View) {
        val consoleText = findViewById(R.id.respTextView) as TextView?
        consoleText!!.setText("", TextView.BufferType.SPANNABLE)
        val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        editor.putString("console", "")
        editor.apply()
    }

    fun savePhone(v: View) {
        val editText = findViewById(R.id.phoneText) as EditText?

        val phone = editText!!.text.toString()

        val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
        editor.putString("phone", phone)

        val msg = "[$phone] 来自测试短信"

        val curTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val jsonData = "{\"user_id\":100,\"url\":\"http://z.cn\",\"data\":{\"first\":{\"value\":\"短信通知\",\"color\":\"#173177\"},\"send\":{\"value\":\"来自$phone\",\"color\":\"#173177\"},\"text\":{\"value\":\"$msg\",\"color\":\"#173177\"},\"time\":{\"value\":\"$curTime\",\"color\":\"#173177\"}}}"

        val respText = findViewById(R.id.respTextView) as TextView?
        respText!!.setText(respText!!.text.toString() + "\n" + jsonData, TextView.BufferType.SPANNABLE)
        editor.putString("console", "test:" + jsonData)
        editor.apply()

    }
}
