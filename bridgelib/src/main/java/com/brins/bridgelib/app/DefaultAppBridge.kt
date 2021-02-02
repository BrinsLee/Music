package com.brins.bridgelib.app

import android.content.Context
import android.content.Intent
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.utils.UIUtils

/**
 * Created by lipeilin
 * on 2020/11/11
 */
class DefaultAppBridge : AppBridgeInterface {
    override fun getAppContext(): Context {
        return UIUtils.getApplication().applicationContext
    }

    override fun getAccessToken(): String? {
        return LoginCache.UserCookie
    }

    override fun setAccessToken(accessToken: String?) {
        LoginCache.UserCookie = accessToken ?: ""
    }

    override fun getSplashActivity(context: Context): Intent {
        return Intent()
    }
}