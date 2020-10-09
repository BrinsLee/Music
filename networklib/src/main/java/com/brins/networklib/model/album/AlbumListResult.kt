package com.brins.networklib.model.album

import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_ALBUM_LIST_MUSIC

class AlbumListResult {

    var code = 0

    var songs: MutableList<Song>? = null

    var album: BaseMusic.Album? = null

    class Song : BaseMusic() {

        override val itemType: Int
            get() = ITEM_ALBUM_LIST_MUSIC
    }

/*    class Artist : Serializable {

        var name = ""

        var id: String = ""

        var picId: String = ""

        var img1v1Id: String = ""

        var picUrl: String = ""

        var img1v1Url: String = ""

        var albumSize = 0

        var alias: Array<String>? = null

        var musicSize = 0

    }*/

/*    class Album {
        var id = ""

        var name = ""

        var picUrl = ""

        var blurPicUrl = ""

        var artists: ArrayList<Artist>? = null

        var company = ""

        var description = ""

        var subType = ""

        var type = ""

        var size = 0

        var info: Info? = null
    }*/

}