package com.brins.bridgelib.video

import androidx.fragment.app.Fragment
import com.brins.bridgelib.BridgeInterface

/**
 * Created by lipeilin
 * on 2021/1/29
 */
interface VideoBridgeInterface : BridgeInterface {
    fun getVideoFragment() : Fragment
}