package com.brins.radiolib.bridge

import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub.Companion.RADIOACTIVITY
import com.brins.bridgelib.radio.RadioBridgeInterface

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class RadioBridge : RadioBridgeInterface {

    override fun toRadioActivity() {
        ARouterUtils.go(RADIOACTIVITY)
    }
}