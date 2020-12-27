package com.brins.baselib.utils

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

/**
 * Description:
 * Created by lipeilin, 2020/8/20
 */
class HighlightClickableSpan(var mColor: Int, var mClickListener: View.OnClickListener) : ClickableSpan() {


    override fun onClick(p0: View) {
        mClickListener.onClick(p0)
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.color = mColor
        ds.isUnderlineText = false
    }
}