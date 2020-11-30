package com.brins.baselib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.chad.library.adapter.base2.listener.OnItemClickListener
import kotlin.math.min

/**
 * Created by lipeilin
 * on 2020/11/28
 */
class MultiImageView(context: Context, attributeSet: AttributeSet? = null) :
    LinearLayout(context, attributeSet) {

    companion object {
        var MAX_WIDTH = 0

    }

    private var imagesList: List<String>? = null
    private var pxOneMaxWandH = 0
    private var pxMoreWandH = 0 // 多张图的宽高
    private var pxImagePadding = dp2px(3f)
    private var MAX_PER_ROW_COUNT = 3
    private var onePicParams: LayoutParams? = null
    private var moreParams: LayoutParams? = null
    private var moreParaColumnFirst: LayoutParams? = null
    private var rowParams: LayoutParams? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    fun setList(lists: List<String>) {
        imagesList = lists
        if (MAX_WIDTH > 0) {
// 如果需要两张和四张图横向铺满，这里去掉注释即可。
//  if (lists.size() == 2 || lists.size() == 4) {
//  pxMoreWandH = (MAX_WIDTH - pxImagePadding) / 2;
//  } else {
            pxMoreWandH = (MAX_WIDTH - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
//  }
            pxOneMaxWandH = MAX_WIDTH * 2 / 3
            initImageLayoutParams()

        }
        initView()
    }

    private fun initView() {
        this.orientation = VERTICAL
        this.removeAllViews()
        if (MAX_WIDTH == 0) {
            addView(View(context))
            return
        }
        if (imagesList == null || imagesList!!.isEmpty()) {
            return
        }
        if (imagesList!!.size == 1) {
            addView(createImageView(0, false))
        } else {
            val allCount = imagesList!!.size
            if (allCount == 4) {
                MAX_PER_ROW_COUNT = 2
            } else {
                MAX_PER_ROW_COUNT = 3
            }
            val rowCount =
                allCount / MAX_PER_ROW_COUNT + (if (allCount % MAX_PER_ROW_COUNT > 0) 1 else 0)
            for (i in 0 until rowCount) {
                val rowLayout = LinearLayout(context)
                rowLayout.apply {
                    orientation = HORIZONTAL
                    layoutParams = rowParams
                }
                if (i != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }
                var columnCount =
                    if (allCount % MAX_PER_ROW_COUNT == 0) MAX_PER_ROW_COUNT else allCount % MAX_PER_ROW_COUNT
                if (i != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT
                }
                addView(rowLayout)
                val rowOffset = i * MAX_PER_ROW_COUNT
                for (j in 0 until columnCount) {
                    val pos = j + rowOffset
                    rowLayout.addView(createImageView(pos, true))
                }
            }
        }
    }

    private fun createImageView(pos: Int, isMuli: Boolean): ImageView {
        var url = ""
        if (!imagesList?.get(pos).isNullOrEmpty()) {
            url = imagesList!![pos]
        }
        val imageView = ImageView(context)
        if (isMuli) {
            imageView.apply {
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                layoutParams = if (pos % MAX_PER_ROW_COUNT == 0) moreParaColumnFirst
                else moreParams
            }
        } else {
            imageView.apply {
                adjustViewBounds = true
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                maxHeight = pxOneMaxWandH
                layoutParams = onePicParams
            }

        }
        imageView.id = url.hashCode()
        GlideHelper.setImageResource(
            imageView, url
        )
        return imageView
    }


    private fun initImageLayoutParams() {
        onePicParams = LayoutParams(pxOneMaxWandH, LayoutParams.WRAP_CONTENT)
        moreParaColumnFirst = LayoutParams(pxMoreWandH, pxMoreWandH)
        moreParams = LayoutParams(pxMoreWandH, pxMoreWandH)
        moreParams!!.setMargins(pxImagePadding, 0, 0, 0)
        rowParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (MAX_WIDTH == 0) {
            val width = measureWidth(widthMeasureSpec)
            if (width > 0) {
                MAX_WIDTH = width - paddingLeft - paddingRight
                if (imagesList != null && imagesList!!.isNotEmpty()) {
                    setList(imagesList!!)
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {

        var result = 0
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = min(result, specSize)
            }
        }
        return result
    }
}