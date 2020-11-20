package com.brins.networklib.model.daily

import com.brins.baselib.module.ITEM_HOME_MUSIC_LIST
import com.chad.library.adapter.base.model.BaseData

class DailyData : BaseData() {

    interface onItemSelectListener {
        fun onDailyClick()

        fun onHeartClick()

        fun onRankClick()

        fun onFmClick()
    }

    private var itemSelectListener: onItemSelectListener? = null

    fun setListener(itemSelectListener: onItemSelectListener): DailyData {
        this.itemSelectListener = itemSelectListener
        return this
    }

    fun getListener(): onItemSelectListener? {
        return itemSelectListener
    }

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
        return ITEM_HOME_MUSIC_LIST
    }
}