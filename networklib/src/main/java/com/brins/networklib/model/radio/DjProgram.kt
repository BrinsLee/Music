package com.brins.networklib.model.radio

import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.UserProfile

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class DjProgram {

    var id = ""

    var type = 0

    var name = ""

    var copywriter = ""

    var picUrl = ""

    var canDislike = false


    var program: Program? = null

    class Program {

        var mainSong: BaseMusic? = null

        var dj : UserProfile? = null

        var blurCoverUrl = ""

        var radio : Radio? = null

        var subscribedCount = 0

        var mainTrackId = ""

        var serialNum = 0

        var listenerCount = 0

        var name = ""

        var id = ""

        var createTime = ""

        var description = ""

        var userId = ""

        var coverUrl = ""

        var channels : Array<String>? = null

        var buyed = false

        var adjustedPlayCount = 0

        var duration : Long = 0

        var shareCount = 0

        var subscribed = false

        var likedCount = 0

        var commentCount = 0

        var isPublish = false

    }
}
