package com.brins.find.bridge

import androidx.fragment.app.Fragment
import com.brins.bridgelib.find.FindBridgeInterface
import com.brins.find.fragment.FindFragment

/**
 * Created by lipeilin
 * on 2020/12/4
 */
class FindBridge : FindBridgeInterface {
    override fun getFindFragment(): Fragment = FindFragment.getInstance()
}