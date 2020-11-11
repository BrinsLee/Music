package com.brins.musiclistlib.bridge

import android.os.Bundle
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.bridgelib.musiclist.MusicListBridgeInterface

/**
 * Created by lipeilin
 * on 2020/11/11
 */
class MusicListBridge : MusicListBridgeInterface {
    override fun toMusicListActivity(bundle: Bundle?) {
        bundle?.let {
            ARouterUtils.go(RouterHub.MUSICLISTACTIVITY, it)
        }
    }
}