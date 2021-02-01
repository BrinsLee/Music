package com.brins.networklib.model.musicvideo

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_VIDEO_MUSIC_VIDEO_COMMENT
import com.google.gson.annotations.SerializedName

class MvCommentsBean : BaseData() {
    @SerializedName("user")
    var user: CommenterBean? = null

    @SerializedName("commentId")
    var commentId: String = ""

    @SerializedName("content")
    var content: String = ""
    override val itemType: Int
        get() = ITEM_VIDEO_MUSIC_VIDEO_COMMENT


}