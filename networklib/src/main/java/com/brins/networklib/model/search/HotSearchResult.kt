package com.brins.networklib.model.search

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_SEARCH_HOT

/**
 * Created by lipeilin
 * on 2020/11/15
 */
class HotSearchResult {

    var code = 0

    var data : ArrayList<HotSearchData>? = null


    class HotSearchData : BaseData() {
        var searchWord = ""

        var score = 0

        var content = ""

        var source = 0

        var iconType = 0

        var iconUrl = ""

        var url = ""

        var alg = ""

        override val itemType: Int
            get() = ITEM_SEARCH_HOT
    }

}