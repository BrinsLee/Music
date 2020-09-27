package com.brins.musicsquarelib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.brins.baselib.utils.SizeUtils.dp2px

class PlayListViewPager @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ViewPager(context, attributeSet) {

    private var mLastY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val currentNum = currentItem
        when (ev.actionMasked) {

            MotionEvent.ACTION_DOWN -> {
                if (currentNum == 0) {
                    if (ev.y >= 0 && ev.y <= dp2px(165f)) {
                        mLastY = ev.y
                        return false
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun addOnPageChangeListener(listener: OnPageChangeListener) {
        super.addOnPageChangeListener(listener)
    }
}