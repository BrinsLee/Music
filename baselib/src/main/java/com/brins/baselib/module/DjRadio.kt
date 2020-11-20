package com.brins.baselib.module

/**
 * Created by lipeilin
 * on 2020/11/17
 */
class DjRadio : BaseData() {

    var id = ""

    var name = ""

    var dj : UserProfile? = null

    var picUrl = ""

    var desc = ""

    var subCount = 0

    var programCount = 0

    var createTime = ""

    var categoryId = 0

    var category = ""

    var rcmdText = ""

    var playCount = 0

    var shareCount = 0

    var commentCount = 0



    override val itemType: Int
        get() = ITEM_SEARCH_DJRADIO
}