package com.brins.musicdetail.bridge

import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.bridgelib.musicdetail.MusicDetailBridgeInterface

/**
 * Created by lipeilin
 * on 2020/11/11
 */
class MusicDetailBridge : MusicDetailBridgeInterface {
    override fun toMusicDetailActivity() {
        ARouterUtils.go(RouterHub.MUSICDETAILACTIVITY)
    }
}