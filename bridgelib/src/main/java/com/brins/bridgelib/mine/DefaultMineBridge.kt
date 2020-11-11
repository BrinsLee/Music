package com.brins.bridgelib.mine

import androidx.fragment.app.Fragment

/**
 * Created by lipeilin
 * on 2020/11/11
 */
class DefaultMineBridge : MineBridgeInterface {
    override fun getMineFragment(): Fragment = Fragment()
}