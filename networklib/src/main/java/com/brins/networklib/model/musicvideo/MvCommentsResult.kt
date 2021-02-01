package com.brins.networklib.model.musicvideo

import com.google.gson.annotations.SerializedName

class MvCommentsResult {
    @SerializedName("comments")
    var comments : ArrayList<MvCommentsBean>? = null
}