package com.brins.networklib.interceptor

import android.util.Log
import com.brins.baselib.utils.SpUtils
import com.brins.baselib.utils.SpUtils.KEY_COOKIE
import com.brins.baselib.utils.SpUtils.SP_USER_INFO
import com.brins.bridgelib.app.AppBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.baselib.config.UserCookie

import okhttp3.Interceptor
import okhttp3.Response
import java.lang.StringBuilder

class ReceivedCookiesInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val stringBuilder = StringBuilder()
        val cookies = response.headers("Set-Cookie")
        val path = response.request().url().pathSegments()
        if (path[0] == "login" && cookies.isNotEmpty()) {
            cookies.forEach {
                if (it.contains("MUSIC_U=") || it.startsWith("_"))
                stringBuilder.append(it)
            }
            UserCookie = stringBuilder.toString()
            SpUtils.obtain(
                SP_USER_INFO,
                BridgeProviders.instance.getBridge(AppBridgeInterface::class.java).getAppContext()
            )
                .save(KEY_COOKIE, UserCookie)

            Log.d("InterceptorCookie:", UserCookie)
        }
        return response
    }


}