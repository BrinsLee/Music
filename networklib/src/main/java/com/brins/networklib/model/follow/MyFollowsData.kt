package com.brins.networklib.model.follow

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_FIND_FOLLOW

/**
 * Created by lipeilin
 * on 2020/12/3
 */
class MyFollowsData : BaseData() {
    override val itemType: Int
        get() = ITEM_FIND_FOLLOW

}