package com.brins.networklib.model.title

import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE
import com.chad.library.adapter.base.model.BaseData

open class SingleTitleData : BaseData() {
    private var mTitle: String? = null

    fun getTitle(): String? {
        return mTitle
    }

    fun setTitle(title: String): SingleTitleData {
        mTitle = title
        return this
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
        return ITEM_HOME_SINGLE_TITLE
    }
}