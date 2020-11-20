package com.brins.networklib.interceptor

import android.text.TextUtils
import android.util.Log
import com.brins.baselib.cache.login.LoginCache.UserCookie
import com.brins.baselib.config.API_NEED_COOKIE
import okhttp3.Interceptor
import okhttp3.Response

class AddCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        Log.d("addCookie:", UserCookie)
        val path = chain.request().url().pathSegments().toString()
        if (API_NEED_COOKIE.contains(path) && !TextUtils.isEmpty(UserCookie)) {
            builder.addHeader("Cookie", UserCookie)
        }
        return chain.proceed(builder.build())
    }
}