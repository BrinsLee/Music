package com.brins.networklib.model.music

import com.brins.baselib.module.ITEM_MINE_RECENTLY_MUSIC
import com.chad.library.adapter.base.model.BaseData

/**
 * Created by lipeilin
 * on 2020/10/19
 */
class RecentlyMusic : BaseData() {
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
        return ITEM_MINE_RECENTLY_MUSIC
    }
}