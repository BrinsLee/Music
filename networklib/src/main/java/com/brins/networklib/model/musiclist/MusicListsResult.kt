package com.brins.networklib.model.musiclist

import com.google.gson.annotations.SerializedName

class MusicListsResult {

    var code = 0

    @SerializedName("playlists", alternate = ["playlist"])
    var playlists: Array<MusicList>? = null
}