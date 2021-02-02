package com.brins.eventdetaillib.bridge

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub.Companion.EVENTDETAILACTIVITY
import com.brins.bridgelib.event.EventDetailBridgeInterface
import com.brins.eventdetaillib.fragment.EventCommentFragment

/**
 * Created by lipeilin
 * on 2020/12/31
 */
class EventDetailBridge : EventDetailBridgeInterface {

    override fun toEventDetailActivity(bundle: Bundle) {
        ARouterUtils.go(EVENTDETAILACTIVITY, bundle)
    }

    override fun getEventCommentFragment(bundle: Bundle): Fragment {
        return EventCommentFragment.getInstance(bundle)
    }
}