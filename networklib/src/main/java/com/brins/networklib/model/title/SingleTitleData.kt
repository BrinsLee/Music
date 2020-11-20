package com.brins.networklib.model.title

import android.view.View
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE
import com.chad.library.adapter.base.model.BaseData

open class SingleTitleData : BaseData() {
    private var mTitle: String? = null

    private var mListener: View.OnClickListener? = null

    fun getTitle(): String? {
        return mTitle
    }

    fun setTitle(title: String): SingleTitleData {
        mTitle = title
        return this
    }

    fun getListener(): View.OnClickListener? = mListener

    fun setListener(listener: View.OnClickListener): SingleTitleData {
        mListener = listener
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