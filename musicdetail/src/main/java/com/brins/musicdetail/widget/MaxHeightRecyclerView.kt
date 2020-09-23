package com.brins.musicdetail.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.brins.musicdetail.R

class MaxHeightRecyclerView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
) : RecyclerView(context, attributeSet) {

    private var mMaxHeight = 0

    init {
        initAttribute(context, attributeSet)
    }

    private fun initAttribute(
        context: Context,
        attrs: AttributeSet?
    ) {
        val arr =
            context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView)
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxHeightRecyclerView_maxHeight, mMaxHeight)
        arr.recycle()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {

        if (mMaxHeight > 0) {
            super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST))

        } else {
            super.onMeasure(widthSpec, heightSpec)

        }
    }
}