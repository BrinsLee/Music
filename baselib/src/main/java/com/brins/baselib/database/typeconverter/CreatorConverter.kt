package com.brins.baselib.database.typeconverter

import androidx.room.TypeConverter
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.MusicList
import com.brins.baselib.utils.GsonUtils

/**
 * Created by lipeilin
 * on 2020/10/27
 */
class CreatorConverter {

    @TypeConverter
    fun getCreatorFromString(value: String): MusicList.Subscriber {
        return GsonUtils.fromJson(value, MusicList.Subscriber::class.java)
    }

    @TypeConverter
    fun restoreCreatorToString(value: MusicList.Subscriber?): String {
        return GsonUtils.toJson(value)
    }

}