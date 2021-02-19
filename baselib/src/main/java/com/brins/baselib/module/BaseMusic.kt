package com.brins.baselib.module

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.brins.baselib.database.typeconverter.ArtistsConverter
import com.brins.baselib.database.typeconverter.PlayStatusConverter
import com.brins.baselib.database.typeconverter.SongConverter
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "recently_music")
@TypeConverters(
    SongConverter::class, ArtistsConverter::class, PlayStatusConverter::class
)
open class BaseMusic : BaseData(), Serializable {

    /**
     * 音乐Id
     */
    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    var id: String = ""

    /**
     * 名称
     */
    @ColumnInfo(name = "Name")
    @SerializedName("name")
    var name: String = ""

    /**
     * 时长
     */
    @ColumnInfo(name = "Duration")
    @SerializedName("dt", alternate = ["duration"])
    var duration: Int = 0

    /**
     * 封面图片
     */
    @ColumnInfo(name = "Cover")
    var cover: String = ""

    /**
     * 图片连接
     */
    @ColumnInfo(name = "PicUrl")
    @SerializedName("picUrl")
    var picUrl = ""

    /**
     * 音乐连接
     */
    @ColumnInfo(name = "MusicUrl")
    @SerializedName("musicUrl", alternate = ["mp3Url"])
    var musicUrl: String = ""

    /**
     * 音乐信息
     */
    @ColumnInfo(name = "Song")
    @SerializedName("song", alternate = ["album", "al"])
    var song: Song? = null

    /**
     * 歌手信息
     */
    @ColumnInfo(name = "Artists")
    @SerializedName("ar", alternate = ["artists"])
    var artists: ArrayList<Artist>? = null

    /**
     * 播放状态
     */
    @Transient
    @ColumnInfo(name = "PlayStatus")
    var playStatus: MusicStatus = MusicStatus.FIRST_PLAY

    /**
     * 缓存封面
     */
    @Transient
    var bitmapCover: Bitmap? = null

    /**
     * 模糊封面
     */
    @Transient
    var blurBitmap: Bitmap? = null

    class Song : Serializable {
        var name: String = ""

        var duration = 0

        var picUrl = ""

        var artists: ArrayList<Artist>? = null

        var album: Album? = null
    }

    class Artist : BaseData(), Serializable {

        var id = ""

        var name = ""

        var picUrl = ""

        var img1v1Url = ""

        var albumSize = 0
        override val itemType: Int
            get() = ITEM_SEARCH_ARTIST
    }

    class Album : BaseData(), Serializable {
        var name = ""

        var id = ""

        var type = ""

        var size = 0

        var blurPicUrl = ""

        var picUrl = ""

        var company = ""

        var publishTime = 0L

        var subType = ""

        var info: Info? = null

        var artist: Artist? = null
        override val itemType: Int
            get() = ITEM_SEARCH_ALBUM

    }

    class Info : Serializable {

        var commentCount = 0
        var shareCount = 0
    }

    override val itemType: Int
        get() = ITEM_BASE_MUSIC
}