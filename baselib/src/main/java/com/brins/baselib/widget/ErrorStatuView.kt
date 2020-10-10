package com.brins.baselib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.brins.baselib.R

class ErrorStatuView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) : FrameLayout(context, attributeSet, def) {

    private val container: LinearLayout
    private var mListener: OnClickListener? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.base_error_view, this, true)
        container = view.findViewById(R.id.ll_error_view)
    }

    fun setListener(listener: OnClickListener) {
        mListener = listener
        container.setOnClickListener(mListener)
    }

}