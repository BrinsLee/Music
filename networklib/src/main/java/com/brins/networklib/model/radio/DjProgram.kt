package com.brins.networklib.model.radio

import com.brins.baselib.module.BaseData
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_RADIO_PROGRAM_DATA
import com.brins.baselib.module.UserProfile

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class DjProgram : BaseData() {


    var mainSong: BaseMusic? = null

    var dj: UserProfile? = null

    var blurCoverUrl = ""

    var radio: Radio? = null

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

    var channels: Array<String>? = null

    var buyed = false

    var adjustedPlayCount = 0

    var duration: Long = 0

    var shareCount = 0

    var subscribed = false

    var likedCount = 0

    var commentCount = 0

    var isPublish = false

    override val itemType: Int
        get() = ITEM_RADIO_PROGRAM_DATA
}
