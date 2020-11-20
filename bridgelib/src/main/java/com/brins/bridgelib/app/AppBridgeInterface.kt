package com.brins.bridgelib.app

import android.content.Context
import com.brins.bridgelib.BridgeInterface

interface AppBridgeInterface : BridgeInterface {

    fun getAppContext(): Context

    fun getAccessToken(): String?

    fun setAccessToken(accessToken: String?)
}