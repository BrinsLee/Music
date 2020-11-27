package com.brins.loginlib.bridge

import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.bridgelib.login.LoginBridgeInterface

/**
 * Created by lipeilin
 * on 2020/11/11
 */
class LoginBridge : LoginBridgeInterface {

    override fun toMineInfoActivity() {
        ARouterUtils.go(RouterHub.MINEINFOACTIVITY)
    }

    override fun toLoginSelectActivity() {
        ARouterUtils.go(RouterHub.LOGINSELECTACTIVITY)
    }
}