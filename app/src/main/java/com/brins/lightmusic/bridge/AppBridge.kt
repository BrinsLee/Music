package com.brins.lightmusic.bridge

import android.content.Context
import com.brins.bridgelib.app.AppBridgeInterface
import com.brins.lightmusic.BaseApplication

class AppBridge : AppBridgeInterface {
    override fun getAppContext(): Context = BaseApplication.getInstance().baseContext
}