package com.brins.networklib.login

import com.brins.baselib.module.ITEM_MINE_UNLOGIN
import com.chad.library.adapter.base.model.BaseData

class UnLoginData : BaseData() {
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
        return ITEM_MINE_UNLOGIN
    }
}