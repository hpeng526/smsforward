package com.github.hpeng526.smsforward

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.github.hpeng526.smsforward.networking.postJson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = Handler(Looper.getMainLooper())
        val phoneNumText = getSharedPreferences("data", Context.MODE_PRIVATE).getString("phone", "")
        val consoleText = getSharedPreferences("data", Context.MODE_PRIVATE).getString("console", "")
        val editText = findViewById(R.id.phoneText) as EditText?
        val respText = findViewById(R.id.respTextView) as TextView?
        editText!!.setText(phoneNumText, TextView.BufferType.EDITABLE)
        respText!!.setText(consoleText, TextView.BufferType.SPANNABLE)
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

//        val toast = Toast.makeText(applicationContext, phone, Toast.LENGTH_LONG)
//        toast.show()

        val msg = "[$phone] 来自测试短信"

        val curTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val jsonData = "{\"user_id\":100,\"url\":\"http://z.cn\",\"data\":{\"first\":{\"value\":\"短信通知\",\"color\":\"#173177\"},\"send\":{\"value\":\"来自$phone\",\"color\":\"#173177\"},\"text\":{\"$msg\":\"Text\",\"color\":\"#173177\"},\"time\":{\"value\":\"$curTime\",\"color\":\"#173177\"}}}"

        val respText = findViewById(R.id.respTextView) as TextView?
        respText!!.setText(respText!!.text.toString() + "\n" + jsonData, TextView.BufferType.SPANNABLE)
        editor.putString("console", jsonData)
        editor.apply()

//        postJson("http://gateway/u", jsonData, object : Callback {
//
//
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//                Log.e("smslog", e.message)
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                println("我是异步线程,线程Id为:" + Thread.currentThread().id)
//                var respString = response.body()?.string().toString()
//                Log.d("smslog", respString)
//
//                mHandler!!.post({
//                    val respText = findViewById(R.id.respTextView) as TextView?
//                    respText!!.setText(respText!!.text.toString() + "\n" + respString, TextView.BufferType.SPANNABLE)
//                })
//
//                response.close()
//            }
//        })

    }
}
