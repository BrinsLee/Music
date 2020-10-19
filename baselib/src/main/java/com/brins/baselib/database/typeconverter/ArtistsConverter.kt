package com.brins.baselib.database.typeconverter

import androidx.room.TypeConverter
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.utils.GsonUtils

/**
 * Created by lipeilin
 * on 2020/10/19
 */
class ArtistsConverter {

    @TypeConverter
    fun getArtistsFromString(value: String): ArrayList<BaseMusic.Artist>? {
        return GsonUtils.listFromJson<BaseMusic.Artist>(value)
    }

    @TypeConverter
    fun restoreArtistsToString(value: ArrayList<BaseMusic.Artist>?): String {
        return GsonUtils.listToJson(value)
    }

}