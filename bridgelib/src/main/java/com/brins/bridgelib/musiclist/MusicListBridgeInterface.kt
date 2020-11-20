package com.brins.bridgelib.musiclist

import android.os.Bundle
import com.brins.bridgelib.BridgeInterface

/**
 * Created by lipeilin
 * on 2020/11/11
 */
interface MusicListBridgeInterface : BridgeInterface {

    fun toMusicListActivity(bundle: Bundle? = null)
}