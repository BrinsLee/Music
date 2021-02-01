package com.brins.video.bridge

import androidx.fragment.app.Fragment
import com.brins.bridgelib.video.VideoBridgeInterface
import com.brins.video.fragment.VideoFragment

/**
 * Created by lipeilin
 * on 2021/1/29
 */
class VideoBridge : VideoBridgeInterface {
    override fun getVideoFragment(): Fragment {
        return VideoFragment.getInstance()
    }
}