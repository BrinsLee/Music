package com.brins.musicsquarelib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView
import kotlin.math.abs

class PlayListScrollerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : NestedScrollView(context, attributeSet) {
    var mLastX = 0
    var mLastY = 0
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.x.toInt()
                mLastY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = abs(mLastX - ev.x)
                val offsetY = abs(mLastY - ev.y)
                if (offsetX > offsetY) {
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}