package com.brins.mine.bridge

import androidx.fragment.app.Fragment
import com.brins.bridgelib.mine.MineBridgeInterface
import com.brins.mine.fragment.MineFragment

/**
 * Created by lipeilin
 * on 2020/11/11
 */
class MineBridge : MineBridgeInterface {
    override fun getMineFragment(): Fragment {
        return MineFragment.getInstance()
    }
}