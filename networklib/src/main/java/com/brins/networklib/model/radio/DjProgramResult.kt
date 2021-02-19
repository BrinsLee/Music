package com.brins.networklib.model.radio

import com.google.gson.annotations.SerializedName

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class DjProgramResult {
    var code: Int = 0

    @SerializedName("programs", alternate = ["result"])
    var programs: MutableList<DjProgram>? = null
}