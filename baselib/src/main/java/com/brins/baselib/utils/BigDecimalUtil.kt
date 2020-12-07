package com.brins.baselib.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by lipeilin
 * on 2020/11/24
 */

fun convertNumMillion(value: Long, divisor: Int = 10000, format: String = "#.# w"): String {
    if (value < 10000) {
        return value.toString()
    }
    val df = DecimalFormat(format)
    df.roundingMode = RoundingMode.HALF_UP
    val s = df.format(BigDecimal.valueOf(value).divide(BigDecimal.valueOf(divisor.toLong())))
    return s
}

fun convertNumHundredMillion(value: Long): String {
    return convertNumMillion(value, divisor = 100000000, format = "#.# 亿")
}

fun convertNum(value: Long): String {
    return if (value > 100000000) {
        convertNumHundredMillion(value)
    } else {
        convertNumMillion(value)
    }
}