package com.brins.baselib.database.typeconverter

import androidx.room.TypeConverter
import com.brins.baselib.module.MusicList
import com.brins.baselib.utils.GsonUtils

/**
 * Created by lipeilin
 * on 2020/10/27
 */
class TrackIdsConverter {

    @TypeConverter
    fun getTrackIdsFromString(value: String): ArrayList<MusicList.TrackId>? {
        return GsonUtils.listFromJson<MusicList.TrackId>(value)
    }

    @TypeConverter
    fun restoreTrackIdsToString(value: ArrayList<MusicList.TrackId>?): String {
        return GsonUtils.listToJson(value)
    }
}