package com.brins.bridgelib.find

import androidx.fragment.app.Fragment
import com.brins.bridgelib.BridgeInterface

/**
 * Created by lipeilin
 * on 2020/12/4
 */
interface FindBridgeInterface : BridgeInterface {

    fun getFindFragment(): Fragment
}