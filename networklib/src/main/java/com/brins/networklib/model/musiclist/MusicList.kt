package com.brins.networklib.model.musiclist

import com.brins.baselib.module.*

class MusicList : BaseData() {


    var subscribers = arrayListOf<Subscriber>()

    var subscribed: Boolean = false

    var creator: Subscriber? = null

    var backgroundCoverUrl: String? = null

    var titleImageUrl = ""

    var englishTitle = ""

    var trackUpdateTime = ""

    var subscribedCount = 0

    var userId = ""

    var createTime = ""

    var updateTime = ""

    var coverImgUrl = ""

    var trackCount = 0

    var playCount = 0

    var description = ""

    var name = ""

    var id = ""

    var shareCount = 0

    var commentCount = 0

    var trackNumberUpdateTime = ""

    var tags: Array<String>? = null

    var copywriter = ""

    var tag = ""


    var tracks: ArrayList<Track> = arrayListOf()

    class Subscriber {

        var province = 0

        var avatarUrl = ""

        var authStatus = 0

        var followed: Boolean = false

        var accountStatus = 0

        var gender = 0

        var city = 0

        var birthday = ""

        var userId = ""

        var nickname = ""

        var signature = ""

        var description = ""

        var backgroundUrl = ""

        var vipType = 0

        var expertTags: Array<String>? = null

    }

    class Track : BaseMusic() {
        override val itemType: Int
            get() = ITEM_MUSIC_LIST_TRACK_MUSIC

    }


    override val itemType: Int
        get() = ITEM_HOME_TOP_RECOMMEND

}