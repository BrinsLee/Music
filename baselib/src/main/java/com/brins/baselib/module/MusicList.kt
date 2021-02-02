package com.brins.baselib.module

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.room.*
import com.brins.baselib.database.typeconverter.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "recommend_music_list")
@TypeConverters(
    CreatorConverter::class,
    SubscriberConverter::class,
    TagsConverter::class,
    TracksConverter::class,
    TrackIdsConverter::class
)
class MusicList : BaseData(), Serializable {

    /**
     * 关注者
     */
    @ColumnInfo(name = "subscribers")
    var subscribers = arrayListOf<Subscriber>()

    /**
     * 是否关注
     */
    @ColumnInfo(name = "subscribed")
    var subscribed: Boolean = false

    /**
     * 创建者
     */
    @ColumnInfo(name = "creator")
    var creator: Subscriber? = null

    /**
     * 背景图
     */
    @ColumnInfo(name = "backgroundCoverUrl")
    var backgroundCoverUrl: String? = null

    /**
     * 标题图
     */
    @ColumnInfo(name = "titleImageUrl")
    var titleImageUrl = ""

    /**
     * 英文标题
     */
    @ColumnInfo(name = "englishTitle")
    var englishTitle = ""

    /**
     * 更新时间
     */
    @ColumnInfo(name = "trackUpdateTime")
    var trackUpdateTime = ""

    /**
     * 关注数
     */
    @ColumnInfo(name = "subscribedCount")
    var subscribedCount = 0

    /**
     * 用户Id
     */
    @ColumnInfo(name = "userId")
    var userId = ""

    /**
     * 创建时间
     */
    @ColumnInfo(name = "createTime")
    var createTime = ""

    /**
     * 更新时间
     */
    @ColumnInfo(name = "updateTime")
    var updateTime = ""

    /**
     * 图片Url
     */
    @ColumnInfo(name = "coverImgUrl")
    @SerializedName("coverImgUrl", alternate = ["picUrl"])
    var coverImgUrl = ""

    /**
     * 歌曲数量
     */
    @ColumnInfo(name = "trackCount")
    var trackCount = 0

    /**
     * 播放数量
     */
    @SerializedName("playCount", alternate = ["playcount"])
    @ColumnInfo(name = "playCount")
    var playCount: Long = 0

    /**
     * 描述
     */
    @ColumnInfo(name = "description")
    var description = ""

    /**
     * 名称
     */
    @ColumnInfo(name = "name")
    var name = ""

    /**
     * 音乐Id
     */
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = false)
    var id = ""

    /**
     * 分享数
     */
    @ColumnInfo(name = "shareCount")
    var shareCount = 0

    /**
     * 评论数
     */
    @ColumnInfo(name = "commentCount")
    var commentCount = 0

    /**
     * 歌曲数量更新时间
     */
    @ColumnInfo(name = "trackNumberUpdateTime")
    var trackNumberUpdateTime = ""

    /**
     * 标签
     */
    @ColumnInfo(name = "tags")
    var tags: Array<String>? = null

    /**
     * 版权
     */
    @ColumnInfo(name = "copywriter")
    var copywriter = ""

    /**
     * 标志
     */
    @ColumnInfo(name = "tag")
    var tag = ""

    @Ignore
    var cover: Drawable? = null

    @Ignore
    var coverGradientDrawable: Bitmap? = null

    /**
     * 音乐
     */
    @ColumnInfo(name = "tracks")
    var tracks: ArrayList<Track> = arrayListOf()

    /**
     * 音乐Id列表
     */
    @ColumnInfo(name = "trackIds")
    var trackIds: ArrayList<TrackId> = arrayListOf()

    class Subscriber : Serializable {

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

    class TrackId : Serializable {
        var id = ""

        var at = ""
    }


    override val itemType: Int
        get() = ITEM_HOME_TOP_RECOMMEND

}