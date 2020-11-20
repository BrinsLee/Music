package com.brins.lightmusic.bridge

import android.content.Context
import com.brins.baselib.cache.login.LoginCache
import com.brins.bridgelib.app.AppBridgeInterface
import com.brins.lightmusic.BaseApplication

class AppBridge : AppBridgeInterface {
    override fun getAppContext(): Context = BaseApplication.getInstance().baseContext
    override fun getAccessToken(): String? {
        return LoginCache.UserCookie
    }

    override fun setAccessToken(accessToken: String?) {
        LoginCache.UserCookie = accessToken ?: ""
    }
}