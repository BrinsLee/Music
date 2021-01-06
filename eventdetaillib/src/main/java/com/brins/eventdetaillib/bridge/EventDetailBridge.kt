package com.brins.eventdetaillib.bridge

import android.os.Bundle
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub.Companion.EVENTDETAILACTIVITY
import com.brins.bridgelib.event.EventDetailBridgeInterface

/**
 * Created by lipeilin
 * on 2020/12/31
 */
class EventDetailBridge : EventDetailBridgeInterface {

    override fun toEventDetailActivity(bundle: Bundle) {
        ARouterUtils.go(EVENTDETAILACTIVITY, bundle)
    }
}