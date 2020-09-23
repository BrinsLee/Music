package com.brins.networklib.model.music

import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_HOME_TOP_MUSIC

class TopMusic :BaseMusic() {

    override val itemType: Int
        get() = ITEM_HOME_TOP_MUSIC


}