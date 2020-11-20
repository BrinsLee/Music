package com.brins.networklib.model.search

import com.google.gson.annotations.SerializedName

class SearchResultBean<T> {

    @SerializedName(
        "songs",
        alternate = ["albums", "artists", "playlists", "mvs", "djRadios", "userprofiles"]
    )
    var data: ArrayList<T>? = null
}
