package com.brins.networklib.model.banner

import com.brins.baselib.module.ITEM_HOME_BANNER
import com.brins.networklib.model.banner.Banner
import com.chad.library.adapter.base.model.BaseData
import com.google.gson.annotations.SerializedName

class BannerResult : BaseData() {

    @SerializedName("banners")
    var bannners: ArrayList<Banner>? = null

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
        return ITEM_HOME_BANNER
    }
}