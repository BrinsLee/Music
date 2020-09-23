package com.brins.networklib.model.personal

import com.brins.baselib.module.ITEM_HOME_PERSONALIZED
import com.chad.library.adapter.base.model.BaseData

/**
 * @author lipeilin
 * @date 2020/7/22
 */
class PersonalizedResult<T> : BaseData() {

    var code = 0
    var category = 0

    var result: ArrayList<T>? = null
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
        return ITEM_HOME_PERSONALIZED
    }
}