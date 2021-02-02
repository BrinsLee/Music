package com.brins.lightmusic.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.bridgelib.find.FindBridgeInterface
import com.brins.bridgelib.home.HomeBridgeInterface
import com.brins.bridgelib.mine.MineBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.bridgelib.video.VideoBridgeInterface
import javax.inject.Inject

class MainPagerAdapter @Inject constructor(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    private var list = mutableListOf(
        BridgeProviders.instance.getBridge(HomeBridgeInterface::class.java).getHomeFragment(),
        BridgeProviders.instance.getBridge(FindBridgeInterface::class.java).getFindFragment(),
        BridgeProviders.instance.getBridge(VideoBridgeInterface::class.java).getVideoFragment(),
        BridgeProviders.instance.getBridge(MineBridgeInterface::class.java).getMineFragment()
        /*ARouterUtils.getFragment(RouterHub.HOMEFRAGMENT),
        Fragment(),
        Fragment(),
        ARouterUtils.getFragment(RouterHub.MINEFRAGMENT)*/
    )

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}