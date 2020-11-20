package com.brins.baselib.database.typeconverter

import androidx.room.TypeConverter
import com.brins.baselib.module.MusicStatus

/**
 * Created by lipeilin
 * on 2020/10/19
 */
class PlayStatusConverter {

    @TypeConverter
    fun getSongFromString(value: String): MusicStatus {
        return MusicStatus.valueOf(value)
    }

    @TypeConverter
    fun storeSongToString(value: MusicStatus): String {
        return value.toString()
    }
}