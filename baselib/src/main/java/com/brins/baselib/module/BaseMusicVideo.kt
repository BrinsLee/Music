package com.brins.baselib.module

import com.google.gson.annotations.SerializedName

/**
 * Created by lipeilin
 * on 2020/11/17
 */
class BaseMusicVideo : BaseData() {

    var id = ""

    @SerializedName("cover", alternate = ["imgurl"])
    var cover = ""
    var name: String = ""
    var playCount: Int = 0
    var artistId: String = ""
    var artistName: String = ""
    var duration: Int = 0
    var briefDesc: String = ""
    var sub: Boolean = false
    var artists: ArrayList<Artist>? = null

    override val itemType: Int
        get() = ITEM_SEARCH_MUSIC_VIDEO

    class Artist {

        var id = ""

        var name = ""

        var picUrl = ""

        var img1v1Url = ""

        var albumSize = 0
    }
}