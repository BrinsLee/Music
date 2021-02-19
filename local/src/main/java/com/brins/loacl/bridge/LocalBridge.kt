package com.brins.loacl.bridge

import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub.Companion.LOCALMUSICACTIVITY
import com.brins.bridgelib.local.LocalBridgeInterface

/**
 * Created by lipeilin
 * on 2021/2/18
 */
class LocalBridge : LocalBridgeInterface {
    override fun toLocalMusicActivity() {
        ARouterUtils.go(LOCALMUSICACTIVITY)
    }
}