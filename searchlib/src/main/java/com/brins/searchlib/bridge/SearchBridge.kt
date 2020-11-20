package com.brins.searchlib.bridge

import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.bridgelib.search.SearchBridgeInterface

/**
 * Created by lipeilin
 * on 2020/11/15
 */
class SearchBridge : SearchBridgeInterface {
    override fun toSearchActivity() {
        ARouterUtils.go(RouterHub.SEARCHACTIVITY)
    }
}