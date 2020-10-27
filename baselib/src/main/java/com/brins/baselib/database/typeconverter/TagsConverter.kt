package com.brins.baselib.database.typeconverter

import androidx.room.TypeConverter
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.utils.GsonUtils
import java.lang.StringBuilder

/**
 * Created by lipeilin
 * on 2020/10/27
 */
class TagsConverter {

    @TypeConverter
    fun getTagsFromString(value: String): Array<String> {
        return value.split(",").toTypedArray()
    }

    @TypeConverter
    fun restoreTagsToString(value: Array<String>?): String {
        val strBuilder = StringBuilder()
        value?.let {
            for (i in value.indices) {
                strBuilder.append(value[i])
                if (i != value.lastIndex) {
                    strBuilder.append(",")
                }
            }
        }
        return strBuilder.toString()
    }
}