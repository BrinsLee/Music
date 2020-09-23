package com.brins.networklib.model.banner

import com.brins.networklib.model.music.Music
import com.google.gson.annotations.SerializedName

class Banner {

    @SerializedName("pic")
    var picUrl: String = ""

    @SerializedName("targetId")
    var targetId: String = ""

    @SerializedName("titleColor")
    var titleColor: String = "white"

    @SerializedName("url")
    var url: String? = null

    @SerializedName("song")
    var song: Music? = null

    @SerializedName("typeTitle")
    var typeTitle: String = ""
    /*override val itemType: Int
        get() = ITEM_HOME_BANNER*/

}
