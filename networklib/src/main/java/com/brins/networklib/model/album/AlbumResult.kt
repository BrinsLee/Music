package com.brins.networklib.model.album

import com.brins.baselib.module.ITEM_HOME_NEWEST
import com.brins.baselib.module.ITEM_HOME_NEWEST_ALBUM
import com.chad.library.adapter.base.model.BaseData

class AlbumResult<T> : BaseData() {


    var code: Int = 0

    var albums: MutableList<T>? = null

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
        return ITEM_HOME_NEWEST
    }
}