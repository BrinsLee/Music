package com.brins.networklib.model.recommend

import com.brins.baselib.module.ITEM_HOME_TOP_RECOMMEND
import com.chad.library.adapter.base.model.BaseData

class RecommendResult<T> : BaseData() {

    var code = 0

    var playlists: ArrayList<T>? = null

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
        return ITEM_HOME_TOP_RECOMMEND
    }
}