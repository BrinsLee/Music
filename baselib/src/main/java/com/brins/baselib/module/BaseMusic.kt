package com.brins.baselib.module

import com.google.gson.annotations.SerializedName

abstract class BaseMusic : BaseData() {

    @SerializedName("id")
    var id: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("dt", alternate = ["duration"])
    var duration: Int = 0

    var cover: String = ""

    @SerializedName("picUrl")
    var picUrl = ""

    @SerializedName("musicUrl", alternate = ["mp3Url"])
    var musicUrl: String = ""

    @SerializedName("song", alternate = ["album", "al"])
    var song: Song? = null

    @SerializedName("ar")
    var artists: ArrayList<Artist>? = null

    var playStatus : MusicStatus = MusicStatus.FIRST_PLAY

    class Song {
        var name: String = ""

        var duration = 0

        var picUrl = ""

        var artists: ArrayList<Artist>? = null

        var album: Album? = null
    }

    class Artist {

        var id = ""

        var name = ""

        var picUrl = ""

        var img1v1Url = ""

        var albumSize = 0
    }

    class Album {
        var name = ""

        var id = ""

        var type = ""

        var size = 0

        var blurPicUrl = ""

        var picUrl = ""

        var company = ""

        var publishTime = 0L

        var subType = ""
    }
}