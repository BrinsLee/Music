package com.brins.baselib.database.typeconverter

import androidx.room.TypeConverter
import com.brins.baselib.module.MusicList
import com.brins.baselib.utils.GsonUtils

/**
 * Created by lipeilin
 * on 2020/10/27
 */
class SubscriberConverter {

    @TypeConverter
    fun getSubscriberFromString(value: String): ArrayList<MusicList.Subscriber> {
        return GsonUtils.listFromJson<MusicList.Subscriber>(value)
    }

    @TypeConverter
    fun restoreSubscriberToString(value: ArrayList<MusicList.Subscriber>?): String {
        return GsonUtils.listToJson(value)
    }
}