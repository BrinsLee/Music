package com.brins.home.bridge

import androidx.fragment.app.Fragment
import com.brins.bridgelib.home.HomeBridgeInterface
import com.brins.home.fragment.HomeFragment

class HomeBridge : HomeBridgeInterface {
    override fun getHomeFragment(): Fragment = HomeFragment.getInstance()
}