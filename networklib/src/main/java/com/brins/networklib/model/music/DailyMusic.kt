package com.brins.networklib.model.music

import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_DAILY_MUSIC

/**
 * Created by lipeilin
 * on 2020/11/2
 */
class DailyMusic : BaseMusic() {

    override val itemType: Int
        get() = ITEM_DAILY_MUSIC
}