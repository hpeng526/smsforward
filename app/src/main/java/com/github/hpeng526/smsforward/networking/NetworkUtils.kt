package com.github.hpeng526.smsforward.networking

import okhttp3.*


val JSON = MediaType.parse("application/json; charset=utf-8")

fun postJson(url: String, jsonData: String, callback: Callback) {
    val okHttpClient = OkHttpClient()
    val body = RequestBody.create(JSON, jsonData)
    val request = Request.Builder()
            .url(url)
            .post(body)
            .build()
    okHttpClient.newCall(request).enqueue(callback)
}

