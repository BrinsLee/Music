package com.brins.networklib.interceptor

import android.text.TextUtils
import android.util.Log
import com.brins.baselib.config.UserCookie
import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        Log.d("addCookie:", UserCookie)
        if (!TextUtils.isEmpty(UserCookie)) {
            builder.addHeader("Cookie", UserCookie)
        }
        return chain.proceed(builder.build())
    }
}