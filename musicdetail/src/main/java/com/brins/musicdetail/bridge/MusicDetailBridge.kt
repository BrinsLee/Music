package com.brins.musicdetail.bridge

import android.os.Bundle
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

    override fun toCommentsActivity(bundle: Bundle) {
        ARouterUtils.go(RouterHub.COMMENTSACTIVITY, bundle)
    }
}