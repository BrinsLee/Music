package com.brins.networklib.model.musiclist

import com.brins.baselib.module.ITEM_MINE_MUSIC_LIST
import com.chad.library.adapter.base.model.BaseData

/**
 * Created by lipeilin
 * on 2020/10/21
 */
class MusicListData : BaseData() {
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
        return ITEM_MINE_MUSIC_LIST
    }
}