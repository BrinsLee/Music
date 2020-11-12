package com.brins.baselib.module.intelligence

import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_HOME_MUSIC_LIST_INTELLIGENCE

/**
 * Created by lipeilin
 * on 2020/11/12
 */
class MusicListIntelligenceData {

    var id = ""

    var alg = ""

    var recommended = false

    var songInfo: SongInfo? = null

    class SongInfo : BaseMusic() {
        override val itemType: Int
            get() = ITEM_HOME_MUSIC_LIST_INTELLIGENCE
    }

}