package com.brins.eventdetaillib.model

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE

class SingleTitleData : BaseData() {
    override val itemType: Int
        get() = ITEM_HOME_SINGLE_TITLE

    private var mTitle: String? = null

    fun getTitle(): String? {
        return mTitle
    }

    fun setTitle(title: String): SingleTitleData {
        mTitle = title
        return this
    }
}