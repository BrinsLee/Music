package com.brins.bridgelib.musicdetail

import android.os.Bundle
import com.brins.bridgelib.BridgeInterface

/**
 * Created by lipeilin
 * on 2020/11/11
 */
interface MusicDetailBridgeInterface : BridgeInterface {
    fun toMusicDetailActivity()

    fun toCommentsActivity(bundle: Bundle)
}