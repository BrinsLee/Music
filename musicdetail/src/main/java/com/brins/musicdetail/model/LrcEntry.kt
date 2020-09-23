package com.brins.musicdetail.model

import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils

class LrcEntry(var time: Long = 0L, var text: String = "") : Comparable<LrcEntry> {

    constructor(
        time: Long = 0L,
        text: String = "",
        secondText: String = ""
    ) : this(time, text) {

        this.secondText = secondText
    }

    companion object {
        const val GRAVITY_CENTER = 0
        const val GRAVITY_LEFT = 1
        const val GRAVITY_RIGHT = 2
    }

    /**
     * 双语歌词
     */
    var secondText = ""

    var staticLayout: StaticLayout? = null

    /**
     * 歌词距离视图顶部的距离
     */
    var offset = Float.MIN_VALUE

    fun initLrc(paint: TextPaint, width: Int, gravity: Int) {
        var align: Layout.Alignment? = null
        when (gravity) {
            GRAVITY_LEFT -> align = Layout.Alignment.ALIGN_NORMAL
            GRAVITY_CENTER -> align = Layout.Alignment.ALIGN_CENTER
            GRAVITY_RIGHT -> align = Layout.Alignment.ALIGN_OPPOSITE
        }
        val lrc = getShowText()
        staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(lrc, 0, lrc.length, paint, width)
                .setLineSpacing(0f, 1f).build()
        } else {
            StaticLayout(getShowText(), paint, width, align, 1f, 0f, false)
        }
        offset = Float.MIN_VALUE
    }

    override fun compareTo(other: LrcEntry): Int {
        return (time - other.time).toInt()
    }

    private fun getShowText(): String {
        return if (!TextUtils.isEmpty(secondText)) {
            """
     $text
     $secondText
     """.trimIndent()
        } else {
            text
        }
    }

    fun getHeight(): Int {
        return if (staticLayout == null) {
            0
        } else staticLayout!!.height
    }
}