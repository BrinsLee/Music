package com.brins.dailylib.bridge

import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.bridgelib.daily.DailyMusicBridgeInterface

/**
 * Created by lipeilin
 * on 2020/11/11
 */
class DailyMusicBridge : DailyMusicBridgeInterface {
    override fun toDailyMusicActivity() {
        ARouterUtils.go(RouterHub.DAILYMUSICACTIVITY)
    }
}