package com.brins.lightmusic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.bridgelib.home.HomeBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import javax.inject.Inject

class MainPagerAdapter @Inject constructor(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    private var list = mutableListOf(
        ARouterUtils.getFragment(RouterHub.HOMEFRAGMENT),
        Fragment(),
        Fragment(),
        ARouterUtils.getFragment(RouterHub.MINEFRAGMENT)
    )

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}