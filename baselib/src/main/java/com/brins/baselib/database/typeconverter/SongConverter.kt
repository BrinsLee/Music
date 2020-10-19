package com.brins.baselib.database.typeconverter

import androidx.room.TypeConverter
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.utils.GsonUtils

/**
 * Created by lipeilin
 * on 2020/10/19
 */
class SongConverter {

    @TypeConverter
    fun getSongFromString(value: String): BaseMusic.Song {
        return GsonUtils.fromJson(value, BaseMusic.Song::class.java)
    }

    @TypeConverter
    fun storeSongToString(value: BaseMusic.Song): String {
        return GsonUtils.toJson(value)
    }
}