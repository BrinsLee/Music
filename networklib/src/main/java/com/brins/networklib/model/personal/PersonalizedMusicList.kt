package com.brins.networklib.model.personal

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_PERSONALIZED

/**
 * @author lipeilin
 * @date 2020/7/22
 */
class PersonalizedMusicList : BaseData() {

    var id = ""

    var type = 0

    var name = ""

    var copywriter = ""

    var picUrl = ""

    var canDislike = false

    var trackNumberUpdateTime: Long = 0

    var playCount = 0

    var trackCount = 0

    var highQuality = false

    var alg = ""
    override val itemType: Int
        get() = ITEM_HOME_PERSONALIZED


}