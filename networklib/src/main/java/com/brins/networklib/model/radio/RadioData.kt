package com.brins.networklib.model.radio

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_RADIO_RECOMMEND_DATA

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class RadioData : BaseData() {
    override val itemType: Int
        get() = ITEM_RADIO_RECOMMEND_DATA
}