package com.brins.bridgelib.home

import androidx.fragment.app.Fragment

class DefaultHomeBridge : HomeBridgeInterface {
    override fun getHomeFragment(): Fragment = Fragment()
}