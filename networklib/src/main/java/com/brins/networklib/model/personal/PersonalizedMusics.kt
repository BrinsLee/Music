package com.brins.networklib.model.personal

import com.brins.baselib.module.ITEM_HOME_PERSONALIZED_MUSIC
import com.chad.library.adapter.base.model.BaseData

class PersonalizedMusics : BaseData() {
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
        return ITEM_HOME_PERSONALIZED_MUSIC
    }
}