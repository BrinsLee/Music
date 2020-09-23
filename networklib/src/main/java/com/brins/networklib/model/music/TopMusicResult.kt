package com.brins.networklib.model.music

import com.brins.baselib.module.ITEM_HOME_TOP_MUSIC
import com.chad.library.adapter.base.model.BaseData

class TopMusicResult<T> : BaseData() {

    var code: Int = 0

    var data: MutableList<T>? = null
    override fun isValidData(): Boolean {
        return false
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun isAutoIndex(): Boolean {
        return false
    }

    override fun getItemType(): Int {
        return ITEM_HOME_TOP_MUSIC
    }
}