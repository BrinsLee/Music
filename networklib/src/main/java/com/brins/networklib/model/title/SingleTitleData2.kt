package com.brins.networklib.model.title

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_MINE_SINGLE_TITLE

/**
 * Created by lipeilin
 * on 2020/11/27
 */
class SingleTitleData2 : BaseData() {

    private var mTitle: String? = null

    fun getTitle(): String? {
        return mTitle
    }

    fun setTitle(title: String): SingleTitleData2 {
        mTitle = title
        return this
    }

    override val itemType: Int
        get() = ITEM_MINE_SINGLE_TITLE
}