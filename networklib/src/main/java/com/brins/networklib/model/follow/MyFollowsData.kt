package com.brins.networklib.model.follow

import com.brins.baselib.module.ITEM_FIND_FOLLOW
import com.chad.library.adapter.base.model.BaseData

/**
 * Created by lipeilin
 * on 2020/12/3
 */
class MyFollowsData : BaseData() {
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
        return ITEM_FIND_FOLLOW
    }
}