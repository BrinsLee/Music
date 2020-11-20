package com.brins.networklib.model.musiclist

import com.brins.baselib.module.MusicList
import com.google.gson.annotations.SerializedName

class MusicListsResult {

    var code = 0

    @SerializedName("playlists", alternate = ["playlist","recommend"])
    var playlists: Array<MusicList>? = null
}