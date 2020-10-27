package com.brins.baselib.database.typeconverter

import androidx.room.TypeConverter
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.MusicList
import com.brins.baselib.utils.GsonUtils

/**
 * Created by lipeilin
 * on 2020/10/27
 */
class TracksConverter {

    @TypeConverter
    fun getTracksFromString(value: String): ArrayList<MusicList.Track>? {
        return GsonUtils.listFromJson<MusicList.Track>(value)
    }

    @TypeConverter
    fun restoreTracksToString(value: ArrayList<MusicList.Track>?): String {
        return GsonUtils.listToJson(value)
    }
}