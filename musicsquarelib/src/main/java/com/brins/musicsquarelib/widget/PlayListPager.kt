package com.brins.musicsquarelib.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import kotlin.math.abs

class PlayListPager @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) : ViewGroup(context, attributeSet, def) {

    companion object {
        private const val TAG = "PlayListPager"

        //最小缩放倍数 = 0.75
        private const val MIN_SCALE = 0.75f

        //最小透明度 = 0.5
        private const val MIN_ALPHA = 0.5f

        //最小滑动距离 = 15
        private const val MIN_SLOP_DISTANCE = 5f
    }

    //记录刚按下的x、y
    private var mDownX = 0  //记录刚按下的x、y
    private var mDownY = 0

    //位移距离，我们通过该值来判断View的滑动距离、动画差值
    private var totalOffsetX = 0f

    //单击某一个View时的位移动画
    private var offsetAnimator: ValueAnimator? = null

    //位移百分比，用来计算缩放比例、基线位置、透明度
    private var offsetPercent = 0f

    //如果滑动超出了最小滑动距离，则判定为滑动，否则判定为单击
    private var isDraged = false

    //判断是否交换过层级
    private var isReordered = false

    //每次都要记录一下点击的位置，来判断是否超出最小滑动距离
    private var mLastX = 0f //每次都要记录一下点击的位置，来判断是否超出最小滑动距离
    private var mLastY = 0f
    private var clickListener: OnPlayListClickListener? = null

    /**
     * 点击事件
     */
    interface OnPlayListClickListener {
        fun onPlayListClick(position: Int)
    }

    /**
     * 判断是否正在滑动该View
     */
    fun isScrolling(): Boolean {
        return isDraged
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth(widthMeasureSpec)
        val height: Int = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        var maxHeight = 0
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.EXACTLY) {
            //如果是具体值就取具体值
            maxHeight = heightSize
        } else {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val lp: PlayLayoutParams =
                    child.layoutParams as PlayLayoutParams
                maxHeight = Math.max(
                    maxHeight,
                    child.measuredHeight + lp.topMargin + lp.bottomMargin
                )
            }
        }
        return maxHeight
    }

    private fun measuredWidth(widthMeasureSpec: Int): Int {
        var totalWidth = 0
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY) {
            totalWidth = widthSize
        } else {
            for (i in 0 until childCount) {
                val layoutParams: PlayLayoutParams = getChildAt(i).layoutParams as PlayLayoutParams
                totalWidth += getChildAt(i).measuredWidth
            }
        }
        return totalWidth
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        for (i in 0 until childCount) {
            val baseLineX = calBaseLine(i)
            val baseLineY = height / 2

            //滑动的过程也是layout的过程，所以在layout的时候也要更改其透明度和缩放度
            val child = getChildAt(i)
            val lp: PlayLayoutParams = child.layoutParams as PlayLayoutParams
            child.scaleX = lp.scale
            child.scaleY = lp.scale
            child.alpha = lp.alpha
            val left = baseLineX - child.measuredWidth / 2
            val top = baseLineY - child.measuredHeight / 2
            val right = left + child.measuredWidth
            val bottom = top + child.measuredHeight
            child.layout(
                left + lp.leftMargin + paddingLeft,
                top + lp.topMargin + paddingTop,
                right + lp.rightMargin + paddingRight,
                bottom + lp.bottomMargin + paddingBottom
            )
        }
    }

    /**
     *
     * 根据offsetPercent来计算基线位置,子View是根据基线来布局的
     *
     * @param i
     * @return
     */
    private fun calBaseLine(i: Int): Int {
        var baseline = 0f
        //最左边的baseline
        val baselineLeft = width / 4.toFloat()
        //最中间的baseline
        val baselineCenter = width / 2.toFloat()
        //最右边的baseline
        val baselineRight = width - baselineLeft

        val layoutParams: PlayLayoutParams = getChildAt(i).layoutParams as PlayLayoutParams

        when (layoutParams.from) {
            0 -> {
                baseline = when (layoutParams.to) {
                    1 -> {
                        baselineLeft + (baselineRight - baselineLeft) * -offsetPercent
                    }
                    2 -> {
                        baselineLeft + (baselineCenter - baselineLeft) * offsetPercent
                    }
                    else -> {
                        baselineLeft
                    }
                }
            }
            1 -> {
                baseline = when (layoutParams.to) {
                    0 -> {
                        baselineRight - (baselineRight - baselineLeft) * offsetPercent
                    }
                    2 -> {
                        baselineRight + (baselineRight - baselineCenter) * offsetPercent
                    }
                    else -> {
                        baselineRight
                    }
                }
            }
            2 -> {
                baseline = when (layoutParams.to) {
                    1 -> {
                        baselineCenter + (baselineRight - baselineCenter) * offsetPercent
                    }

                    0 -> {
                        baselineCenter + (baselineCenter - baselineLeft) * offsetPercent
                    }

                    else -> {
                        baselineCenter
                    }
                }
            }
        }
        return baseline.toInt()
    }

    class PlayLayoutParams : MarginLayoutParams {
        var scale = 0f
        var alpha = 0f
        var from = 0
        var to = 0
        var index = 0

        constructor(c: Context?, attrs: AttributeSet?) : super(
            c,
            attrs
        )

        constructor(width: Int, height: Int) : super(width, height)
        constructor(source: LayoutParams?) : super(source)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return PlayLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return PlayLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return PlayLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        val lp: PlayLayoutParams
        if (params is PlayLayoutParams) {
            lp = params
        } else {
            lp = PlayLayoutParams(params)
        }
        if (childCount < 2) {
            lp.alpha = MIN_ALPHA
            lp.scale = MIN_SCALE
        } else {
            lp.alpha = 1f
            lp.scale = 1f
        }
        super.addView(child, index, params)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        isDraged = false
        val x: Int = ev.x.toInt()
        val y: Int = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = x
                mDownY = y
                mLastX = x.toFloat()
                mLastY = y.toFloat()
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX: Int = (x - mLastX).toInt()
                val offSetY: Int = (y - mLastY).toInt()
                if (abs(offsetX) > MIN_SLOP_DISTANCE && abs(offSetY) > MIN_SLOP_DISTANCE) {
                    mLastX = x.toFloat()
                    mLastY = y.toFloat()
                    isDraged = true
                }
            }
            MotionEvent.ACTION_UP -> {
                handleActionUp(x, y)
                isDraged = false
            }
        }
        return isDraged
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Int = event.x.toInt()
        val y: Int = event.y.toInt()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE
            -> {
                //通过总位移量除以View长来得到百分比
                val offsetX = (x - mLastX).toInt()
                totalOffsetX += offsetX.toFloat()
                Log.d(
                    TAG,
                    "offsetX : $offsetX"
                )
                Log.d(
                    TAG,
                    "totalOffsetX : $totalOffsetX"
                )
                moveItem()
            }
            MotionEvent.ACTION_UP -> {
                handleActionUp(x, y)
                isDraged = false
            }
        }
        mLastX = x.toFloat()
        mLastY = y.toFloat()
        return true
    }

    private fun handleActionUp(x: Int, y: Int) {
        if (abs(x - mDownX) < MIN_SLOP_DISTANCE && abs(y - mLastY) < MIN_SLOP_DISTANCE) {
            for (i in childCount - 1 downTo 0) {

                //确定是单击，首先要判断是点击的是哪一个View，因为传入的points会改变，所以每次都要重新定义
                val points = FloatArray(2)
                points[0] = x.toFloat()
                points[1] = y.toFloat()

                val clickView = getChildAt(i)
                if (isPointInView(clickView, points)) {
                    Log.d(TAG, "isPointInView:$i")
                    if (indexOfChild(clickView) != 2) {
                        //如果点到1、0View，则将他们移到最前方
                        setSelection(clickView)
                    } else {
                        val lp: PlayLayoutParams = clickView.layoutParams as PlayLayoutParams
                        if (clickListener != null) {
                            clickListener!!.onPlayListClick(lp.index)
                        }
                    }
                }
            }
            return
        }
        initAnimator()
    }

    private fun initAnimator() {
        if (offsetAnimator != null && offsetAnimator!!.isRunning) {
            offsetAnimator!!.cancel()
        }
        //初始值是当前已经位移的值
        val start = totalOffsetX.toInt()
        // 终点是到View的长度，如果滑动没有超过一半，就要回到起点，即0
        var end = 0
        if (offsetPercent >= 0.5f) {
            end = width
        } else if (offsetPercent <= -0.5f) {
            end = -width
        }
        startAnimator(start, end)
    }

    private fun setSelection(clickView: View) {
        var start = 0
        var end = 0

        val lp: PlayLayoutParams = clickView.layoutParams as PlayLayoutParams
        if (lp.from == 0) {
            end = width
        } else if (lp.from == 1) {
            end = -width
        }
        startAnimator(start, end)
    }

    private fun startAnimator(start: Int, end: Int) {
        if (offsetAnimator == null) {
            offsetAnimator = ValueAnimator.ofFloat(start.toFloat(), end.toFloat())
            offsetAnimator?.apply {
                setInterpolator(LinearInterpolator())
                addUpdateListener {
                    totalOffsetX = it.getAnimatedValue() as Float
                    moveItem()
                }
            }

        }

        offsetAnimator?.apply {
            setFloatValues(start.toFloat(), end.toFloat())
            start()
        }

    }

    /**
     * 通过百分比的正负值来确定每个View要去到哪里、设置透明度和缩放、交换View的层级
     */
    private fun moveItem() {
        offsetPercent = totalOffsetX / width
        Log.d(
            TAG,
            "offsetPercent : $offsetPercent"
        )
        setViewFromAndTo()
        changeViewLevel()
        changeAlphaAndScale()
        requestLayout()
    }

    private fun changeAlphaAndScale() {
        for (i in 0 until childCount) {
            val lp: PlayLayoutParams = getChildAt(i).layoutParams as PlayLayoutParams
            when (lp.from) {
                0 -> {
                    if (lp.to == 2) {
                        lp.apply {
                            alpha = MIN_ALPHA + (1f - MIN_ALPHA) * offsetPercent
                            scale = MIN_SCALE + (1f - MIN_SCALE) * offsetPercent
                        }
                    } else if (lp.to == 1) {
                        //将View和低层的View交换
                        exchangeOrder(indexOfChild(getChildAt(i)), 0)

                    }
                }
                1 -> {
                    if (lp.to == 0) {
                        exchangeOrder(indexOfChild(getChildAt(i)), 0)
                    } else if (lp.to == 2) {
                        lp.apply {
                            alpha = MIN_ALPHA + (1f - MIN_ALPHA) * abs(offsetPercent)
                            scale = MIN_SCALE + (1f - MIN_SCALE) * abs(offsetPercent)
                        }
                    }
                }
                2 -> {
                    lp.apply {
                        alpha = 1f - (1f - MIN_ALPHA) * abs(offsetPercent)
                        scale = 1f - (1f - MIN_SCALE) * abs(offsetPercent)
                    }
                }
            }
        }
    }

    private fun changeViewLevel() {
        if (abs(offsetPercent) >= 0.5f) {
            if (!isReordered) {
                exchangeOrder(1, 2)
                isReordered = true
            }
        } else {
            if (isReordered) {
                exchangeOrder(1, 2)
                isReordered = false
            }
        }
    }

    private fun exchangeOrder(i: Int, i1: Int) {
        if (i == i1) {
            return
        }
        val fromChild = getChildAt(i)
        val toChild = getChildAt(i1)

        detachViewFromParent(fromChild)
        detachViewFromParent(toChild)

        attachViewToParent(
            fromChild,
            if (i1 > childCount) childCount else i1,
            fromChild.layoutParams
        )

        attachViewToParent(
            toChild,
            if (i > childCount) childCount else i,
            toChild.layoutParams
        )

        invalidate()

    }

    private fun setViewFromAndTo() {
        if (abs(offsetPercent) >= 1) {
            isReordered = false
            for (i in 0 until childCount) {
                val lp: PlayLayoutParams = getChildAt(i).layoutParams as PlayLayoutParams
                lp.from = lp.to
            }
            totalOffsetX %= width
            offsetPercent %= 1f
        } else {
            for (i in 0 until childCount) {
                val lp: PlayLayoutParams = getChildAt(i).layoutParams as PlayLayoutParams
                when (lp.from) {
                    0 -> lp.to = (if (offsetPercent > 0) 2 else 1)
                    1 -> lp.to = (if (offsetPercent > 0) 0 else 2)
                    2 -> lp.to = (if (offsetPercent > 0) 1 else 0)
                }
            }
        }
    }

    /**
     * 用矩阵的方法，来定义一个点是否位于一个区域内
     */
    private fun isPointInView(
        view: View,
        points: FloatArray
    ): Boolean {
        // 像ViewGroup那样，先对齐一下Left和Top
        points[0] = points[0] - view.left
        points[1] = points[1] - view.top
        // 获取View所对应的矩阵
        val matrix = view.matrix
        // 如果矩阵有应用过变换
        if (!matrix.isIdentity) {
            // 反转矩阵
            matrix.invert(matrix)
            // 映射坐标点
            matrix.mapPoints(points)
        }
        //判断坐标点是否在view范围内
        return points[0] >= 0 && points[1] >= 0 && points[0] < view.width && points[1] < view.height
    }

}