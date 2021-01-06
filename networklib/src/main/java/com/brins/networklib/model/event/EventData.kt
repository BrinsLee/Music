package com.brins.networklib.model.event

import com.brins.baselib.module.*
import java.io.Serializable

/**
 * Created by lipeilin
 * on 2020/11/28
 */
class EventData : BaseData(), Serializable {


    var actName: String? = null

    var forwardCount = 0

    var uuid = ""

    var showTime: Long = 0

    var pics: ArrayList<Image>? = null

    var eventTime: Long = 0

    var user: UserProfile? = null

    var id = ""

    /**
     * 18 分享单曲
    19 分享专辑
    17、28 分享电台节目
    22 转发
    39 发布视频
    35、13 分享歌单
    24 分享专栏文章
    41、21 分享视频
     */
    var type = 0

    var topEvent = false

    var info: Info? = null

    var json: String? = null

    var jsonData: EventJson? = null

    override val itemType: Int
        get() = ITEM_MINE_EVENT_DATA

    class Image : Serializable {
        var originUrl = ""

        var squareUrl = ""

        var rectangleUrl = ""

        var pcSquareUrl = ""

        var pcRectangleUrl = ""

        var width = 0

        var height = 0

        var format = ""
    }

    class Info : Serializable {

        var liked = false

        var resourceId = ""

        var likedCount = 0

        var commentCount = 0

        var shareCount = 0

        var threadId = ""
    }

    class EventJson : Serializable {

        var actId = ""

        var title = ""

        var msg = ""

        var text: ArrayList<String>? = null

        var startTime: Long = 0

        var endTime: Long = 0

        var coverMobileUrl = ""

        var sharePicUrl = ""

        var song: BaseMusic? = null

        var playlist: MusicList? = null

        var album: BaseMusic.Album? = null
    }
}