package com.brins.networklib.model.music

import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_BANNER_MUSIC


class Music : BaseMusic() {

    fun setName(name: String): Music {
        this.name = name
        return this
    }

    fun setId(id: String): Music {
        this.id = id
        return this
    }

    override val itemType: Int
        get() = ITEM_BANNER_MUSIC

}