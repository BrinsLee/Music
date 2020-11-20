package com.brins.musicsquarelib.bridge

import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.bridgelib.musicsquare.MusicSquareBridgeInterface

/**
 * Created by lipeilin
 * on 2020/11/11
 */
class MusicSquareBridge : MusicSquareBridgeInterface {
    override fun toMusicSquareActivity() {
        ARouterUtils.go(RouterHub.MUSICLISTSQUAREACTIVITY)
    }
}