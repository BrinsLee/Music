package com.brins.networklib.model.radio

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_RADIO_RECOMMEND_DATA
import com.brins.baselib.module.UserProfile

/**
 * Created by lipeilin
 * on 2021/2/5
 */
class Radio :BaseData() {

    var id: String = ""

    var name: String = ""

    var picUrl: String = ""

    var programCount = 0

    var subCount: Long = 0

    var createTime: Long = 0

    var categoryId: Int = 0

    var category: String = ""

    var rcmdtext: String = ""

    var radioFeeType: String = ""

    var feeScope = 0

    var playCount: Long = 0

    var subed = false

    var dj: UserProfile? = null

    var copywriter : String = ""

    var buyed = false

    override val itemType: Int
        get() = ITEM_RADIO_RECOMMEND_DATA

}