package com.brins.networklib.model.musicvideo

import com.google.gson.annotations.SerializedName

class MvResult {
    @SerializedName("data",alternate = ["mvs"])
    var dataBeans : List<LastestMvDataBean>? = null
}