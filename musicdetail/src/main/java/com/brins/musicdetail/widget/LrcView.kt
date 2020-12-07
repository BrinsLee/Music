package com.brins.musicdetail.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Looper
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import com.brins.baselib.utils.UIUtils
import com.brins.musicdetail.R
import com.brins.musicdetail.model.LrcEntry
import com.brins.musicdetail.utils.LrcUtils
import kotlin.math.abs

class LrcView @JvmOverloads constructor(
    context: Context
    , attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {

    companion object {
        /**
         * 调整时间
         */
        private const val ADJUST_DURATION: Long = 100

        /**
         * 时间线出现的时间
         */
        private const val TIMELINE_KEEP_TIME = 4 * DateUtils.SECOND_IN_MILLIS

        private const val TAG = "LrcView"
    }

    /**
     * 歌词列表
     */
    private val mLrcEntryList: MutableList<LrcEntry> = ArrayList()

    /**
     * 歌词画笔
     */
    private val mLrcPaint = TextPaint()

    /**
     * 时间线画笔
     */
    private val mTimePaint = TextPaint()

    /**
     * 时间线画笔度量
     */
    private var mTimeFontMetrics: Paint.FontMetrics? = null

    /**
     * 播放图标
     */
    private var mPlayDrawable: Drawable? = null

    /**
     * 分界线高度
     */
    private var mDividerHeight = 0f

    /**
     * 动画持续时间
     */
    private var mAnimationDuration: Long = 0

    /**
     * 正常字体颜色
     */
    private var mNormalTextColor = 0

    /**
     * 正常字体大小
     */
    private var mNormalTextSize = 0f

    /**
     * 当前播放字体颜色
     */
    private var mCurrentTextColor = 0

    /**
     * 当前播放字体大小
     */
    private var mCurrentTextSize = 0f

    /**
     * 时间线字体颜色
     */
    private var mTimelineTextColor = 0

    /**
     * 时间线颜色
     */
    private var mTimelineColor = 0

    /**
     * 时间字体大小
     */
    private var mTimeTextColor = 0

    /**
     * 图标宽度
     */
    private var mDrawableWidth = 0

    /**
     * 时间字体宽度
     */
    private var mTimeTextWidth = 0
    private var mDefaultLabel: String? = null

    /**
     * 歌词间隔
     */
    private var mLrcPadding = 0f
    private var mOnPlayClickListener: OnPlayClickListener? = null
    private var mOnTapListener: OnTapListener? = null
    private var mAnimator: ValueAnimator? = null
    private var mGestureDetector: GestureDetector? = null
    private var mScroller: Scroller? = null
    private var mOffset = 0f
    private var mCurrentLine = 0
    private var mFlag: Any? = null
    private var isShowTimeline = false
    private var isTouching = false

    /**
     * 歌词显示位置，靠左/居中/靠右
     */
    private var mTextGravity = 0

    private val mSimpleOnGestureListener: GestureDetector.SimpleOnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return if (!hasLrc()) {
                    super.onDown(e)
                } else mOnPlayClickListener != null || mOnTapListener != null
            }

            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (!hasLrc() || mOnPlayClickListener == null) {
                    return super.onScroll(e1, e2, distanceX, distanceY)
                }
                removeCallbacks(hideTimelineRunnable)
                if (!isShowTimeline) {
                    mScroller!!.forceFinished(true)
                    isTouching = true
                    isShowTimeline = true
                    invalidate()
                } else {
                    mOffset += -distanceY
                    mOffset = mOffset.coerceAtMost(getOffset(0))
                    mOffset = mOffset.coerceAtLeast(getOffset(mLrcEntryList.size - 1))
                    invalidate()
                }
                return true
            }

            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (!hasLrc() || mOnPlayClickListener == null) {
                    return super.onFling(e1, e2, velocityX, velocityY)
                }

                if (isShowTimeline) {
                    mScroller!!.fling(
                        0,
                        mOffset.toInt(),
                        0,
                        velocityY.toInt(),
                        0,
                        0,
                        getOffset(mLrcEntryList.size - 1).toInt(),
                        getOffset(0).toInt()
                    )
                    return true
                }
                return super.onFling(e1, e2, velocityX, velocityY)
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (!hasLrc()) {
                    return super.onSingleTapConfirmed(e)
                }
                if (mOnPlayClickListener != null && isShowTimeline && mPlayDrawable!!.bounds
                        .contains(e.x.toInt(), e.y.toInt())
                ) {
                    val centerLine: Int = getCenterLine()
                    val centerLineTime: Long = mLrcEntryList[centerLine].time
                    // onPlayClick 消费了才更新 UI
                    if (mOnPlayClickListener != null && mOnPlayClickListener!!.onPlayClick(
                            this@LrcView,
                            centerLineTime
                        )
                    ) {
                        isShowTimeline = false
                        removeCallbacks(hideTimelineRunnable)
                        mCurrentLine = centerLine
                        invalidate()
                        return true
                    }
                } else if (mOnTapListener != null) {
                    mOnTapListener!!.onTap(this@LrcView, e.x, e.y)
                }
                return super.onSingleTapConfirmed(e)
            }
        }

    interface OnPlayClickListener {
        /**
         * 播放按钮被点击，应该跳转到指定播放位置
         *
         * @param view 歌词控件
         * @param time 选中播放进度
         * @return 是否成功消费该事件，如果成功消费，则会更新UI
         */
        fun onPlayClick(view: LrcView?, time: Long): Boolean
    }

    interface OnTapListener {
        /**
         * 歌词控件被点击
         *
         * @param view 歌词控件
         * @param x    点击坐标x，相对于控件
         * @param y    点击坐标y，相对于控件
         */
        fun onTap(view: LrcView?, x: Float, y: Float)
    }

    init {
        val ta: TypedArray =
            getContext().obtainStyledAttributes(attributeSet, R.styleable.LrcView)
        mCurrentTextSize = ta.getDimension(
            R.styleable.LrcView_lrcTextSize,
            resources.getDimension(R.dimen.lrc_text_size)
        )
        mNormalTextSize = ta.getDimension(
            R.styleable.LrcView_lrcNormalTextSize,
            resources.getDimension(R.dimen.lrc_text_size)
        )
        if (mNormalTextSize == 0f) {
            mNormalTextSize = mCurrentTextSize
        }
        mDividerHeight = ta.getDimension(
            R.styleable.LrcView_lrcDividerHeight,
            resources.getDimension(R.dimen.lrc_divider_height)
        )

        val defDuration = resources.getInteger(R.integer.lrc_animation_duration)
        mAnimationDuration =
            ta.getInt(R.styleable.LrcView_lrcAnimationDuration, defDuration).toLong()
        mAnimationDuration =
            if (mAnimationDuration < 0) defDuration.toLong() else mAnimationDuration
        mNormalTextColor = ta.getColor(
            R.styleable.LrcView_lrcNormalTextColor,
            UIUtils.getColor(R.color.lrc_normal_text_color)
        )
        mCurrentTextColor = ta.getColor(
            R.styleable.LrcView_lrcCurrentTextColor,
            UIUtils.getColor(R.color.lrc_current_text_color)
        )
        mTimelineTextColor = ta.getColor(
            R.styleable.LrcView_lrcTimelineTextColor,
            UIUtils.getColor(R.color.lrc_timeline_text_color)
        )
        mDefaultLabel = ta.getString(R.styleable.LrcView_lrcLabel)
        mDefaultLabel =
            if (TextUtils.isEmpty(mDefaultLabel)) getContext().getString(R.string.lrc_label) else mDefaultLabel

        mTimelineColor = ta.getColor(
            R.styleable.LrcView_lrcTimelineColor,
            UIUtils.getColor(R.color.lrc_timeline_color)
        )
        val timelineHeight = ta.getDimension(
            R.styleable.LrcView_lrcTimelineHeight,
            resources.getDimension(R.dimen.lrc_timeline_height)
        )
        mPlayDrawable = ta.getDrawable(R.styleable.LrcView_lrcPlayDrawable)
        mPlayDrawable =
            if (mPlayDrawable == null) UIUtils.getDrawable(R.drawable.base_icon_lrc_play) else mPlayDrawable

        mLrcPadding = ta.getDimension(R.styleable.LrcView_lrcPadding, 0f)
        mLrcPadding += mPlayDrawable!!.intrinsicWidth / 2
        mTimeTextColor = ta.getColor(
            R.styleable.LrcView_lrcTimeTextColor,
            UIUtils.getColor(R.color.lrc_time_text_color)
        )
        val timeTextSize = ta.getDimension(
            R.styleable.LrcView_lrcTimeTextSize,
            resources.getDimension(R.dimen.lrc_time_text_size)
        )
        mTextGravity = ta.getInteger(R.styleable.LrcView_lrcTextGravity, LrcEntry.GRAVITY_CENTER)
        ta.recycle()
        mDrawableWidth = resources.getDimension(R.dimen.lrc_drawable_width).toInt()
        mTimeTextWidth = resources.getDimension(R.dimen.lrc_time_width).toInt()
        mLrcPaint.isAntiAlias = true
        mLrcPaint.textSize = mCurrentTextSize
        mLrcPaint.textAlign = Paint.Align.LEFT
        mTimePaint.isAntiAlias = true
        mTimePaint.textSize = timeTextSize
        mTimePaint.textAlign = Paint.Align.CENTER
        //noinspection SuspiciousNameCombination
        mTimePaint.strokeWidth = timelineHeight
        mTimePaint.strokeCap = Paint.Cap.ROUND
        mTimeFontMetrics = mTimePaint.fontMetrics

        mGestureDetector = GestureDetector(getContext(), mSimpleOnGestureListener)
        mGestureDetector!!.setIsLongpressEnabled(false)
        mScroller = Scroller(getContext())
    }

    /**
     * 获取当前在视图中央的行数
     */
    private fun getCenterLine(): Int {
        var centerLine = 0
        var minDistance = Float.MAX_VALUE
        for (i in mLrcEntryList.indices) {
            if (abs(mOffset - getOffset(i)) < minDistance) {
                minDistance = abs(mOffset - getOffset(i))
                centerLine = i
            }
        }
        return centerLine
    }

    /**
     * 设置非当前行歌词字体颜色
     */
    fun setNormalColor(normalColor: Int) {
        mNormalTextColor = normalColor
        postInvalidate()
    }

    /**
     * 普通歌词文本字体大小
     */
    fun setNormalTextSize(size: Float) {
        mNormalTextSize = size
    }

    /**
     * 当前歌词文本字体大小
     */
    fun setCurrentTextSize(size: Float) {
        mCurrentTextSize = size
    }

    /**
     * 设置当前行歌词的字体颜色
     */
    fun setCurrentColor(currentColor: Int) {
        mCurrentTextColor = currentColor
        postInvalidate()
    }

    /**
     * 设置拖动歌词时选中歌词的字体颜色
     */
    fun setTimelineTextColor(timelineTextColor: Int) {
        mTimelineTextColor = timelineTextColor
        postInvalidate()
    }

    /**
     * 设置拖动歌词时时间线的颜色
     */
    fun setTimelineColor(timelineColor: Int) {
        mTimelineColor = timelineColor
        postInvalidate()
    }

    /**
     * 设置拖动歌词时右侧时间字体颜色
     */
    fun setTimeTextColor(timeTextColor: Int) {
        mTimeTextColor = timeTextColor
        postInvalidate()
    }

    /**
     * 设置歌词是否允许拖动
     *
     * @param draggable
     * @param onPlayClickListener
     */
    fun setDraggable(draggable: Boolean, onPlayClickListener: OnPlayClickListener) {
        mOnPlayClickListener = if (draggable) {
            onPlayClickListener
        } else {
            null
        }
    }

    /**
     * 设置歌词控件点击监听器
     *
     * @param onTapListener 歌词控件点击监听器
     */
    fun setOnTapListener(onTapListener: OnTapListener) {
        mOnTapListener = onTapListener
    }

    /**
     * 设置歌词为空时屏幕中央显示的文字，如“暂无歌词”
     */
    fun setLabel(label: String) {
        runOnUi(Runnable {
            mDefaultLabel = label
            invalidate()
        })
    }

    /**
     * 加载歌词文件
     *
     * @param lrcFile 歌词文件
     */
    fun loadLrc(lrc: String) {
        loadLrc(lrc, "")
    }

    /**
     * 加载双语歌词
     *
     * @param lrc
     * @param secondLrc
     */
    fun loadLrc(lrc: String, secondLrc: String) {
        if (lrc.isNotEmpty()) {
            runOnUi(Runnable {
                reset()
                val entries: MutableList<LrcEntry> = if (secondLrc.isEmpty()) {
                    LrcUtils.parseLrc(lrc)
                } else {
                    LrcUtils.parseLrc(lrc, secondLrc)
                }
                onLrcLoaded(entries)

            })
        }

    }

    private fun getFlag(): Any? {
        return mFlag
    }

    private fun onLrcLoaded(entryList: List<LrcEntry>?) {
        if (entryList != null && entryList.isNotEmpty()) {
            mLrcEntryList.addAll(entryList)
        }
        mLrcEntryList.sort()
        initEntryList()
        invalidate()
    }

    private fun initEntryList() {
        if (!hasLrc() || width == 0) {
            return
        }
        for (lrcEntry in mLrcEntryList) {
            lrcEntry.initLrc(mLrcPaint, getLrcWidth().toInt(), mTextGravity)
        }
        mOffset = height / 2.toFloat()
    }

    /**
     * 获取歌词宽度
     */
    private fun getLrcWidth(): Float {
        return width - mLrcPadding * 2
    }

    /**
     * 刷新歌词
     *
     * @param time 当前播放时间
     */
    fun updateTime(time: Long) {
        runOnUi(Runnable {
            if (hasLrc()) {
                val line: Int = findShowLine(time)
                if (line != mCurrentLine) {
                    mCurrentLine = line
                    if (!isShowTimeline) {
                        smoothScrollTo(line)
                    } else {
                        invalidate()
                    }
                }
            }
        })
    }

    private fun findShowLine(time: Long): Int {
        var left = 0
        var right = mLrcEntryList.size
        while (left <= right) {
            val middle = (left + right) / 2
            val middleTime: Long = mLrcEntryList[middle].time
            if (time < middleTime) {
                right = middle - 1
            } else {
                if (middle + 1 >= mLrcEntryList.size || time < mLrcEntryList[middle + 1]
                        .time
                ) {
                    return middle
                }
                left = middle + 1
            }
        }
        return 0
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            initPlayDrawable()
            initEntryList()
            if (hasLrc()) {
                smoothScrollTo(mCurrentLine, 0L)
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerY = height / 2
        //无歌词
        if (!hasLrc()) {
            mLrcPaint.color = mCurrentTextColor
            val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(
                    mDefaultLabel!!,
                    0,
                    mDefaultLabel!!.length,
                    mLrcPaint,
                    getLrcWidth().toInt()
                ).setAlignment(Layout.Alignment.ALIGN_CENTER).setIncludePad(false)
                    .setLineSpacing(0f, 1f).build()
            } else {
                StaticLayout(
                    mDefaultLabel, mLrcPaint,
                    getLrcWidth().toInt(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false
                )
            }
            drawText(canvas, staticLayout, centerY.toFloat())
            return
        }
        val centerLine = getCenterLine()
        //显示时间轴
        if (isShowTimeline) {
            mPlayDrawable?.draw(canvas)
            mTimePaint.setColor(mTimelineColor)
            canvas.drawLine(
                mTimeTextWidth.toFloat(),
                centerY.toFloat(),
                width - mTimeTextWidth.toFloat(),
                centerY.toFloat(),
                mTimePaint
            )
            mTimePaint.color = mTimeTextColor
            mTimePaint.setColor(mTimeTextColor)
            val timeText =
                LrcUtils.formatTime(mLrcEntryList[centerLine].time)
            val timeX = width - mTimeTextWidth / 2.toFloat()
            val timeY =
                centerY - (mTimeFontMetrics!!.descent + mTimeFontMetrics!!.ascent) / 2
            canvas.drawText(timeText, timeX, timeY, mTimePaint)
        }
        canvas.translate(0f, mOffset)
        var y = 0f
        for (i in 0 until mLrcEntryList.size) {
            if (i > 0) {
                y += (mLrcEntryList[i - 1].getHeight() + mLrcEntryList[i]
                    .getHeight() shr 1) + mDividerHeight
            }
            if (i == mCurrentLine) {
                mLrcPaint.textSize = mCurrentTextSize
                mLrcPaint.color = mCurrentTextColor
            } else if (isShowTimeline && i == centerLine) {
                mLrcPaint.setColor(mTimelineTextColor)
            } else {
                mLrcPaint.textSize = mNormalTextSize
                mLrcPaint.setColor(mNormalTextColor)
            }
            drawText(canvas, mLrcEntryList[i].staticLayout!!, y)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            isTouching = false
            // 启动延时任务，恢复歌词位置
            if (hasLrc() && isShowTimeline) {
                adjustCenter()
                postDelayed(hideTimelineRunnable, TIMELINE_KEEP_TIME)
            }
        }
        return mGestureDetector!!.onTouchEvent(event)
    }

    /**
     * 将中心行微调至正中心
     */
    private fun adjustCenter() {
        smoothScrollTo(getCenterLine(), ADJUST_DURATION)
    }

    private fun drawText(
        canvas: Canvas,
        staticLayout: StaticLayout,
        y: Float
    ) {
        canvas.save()
        canvas.translate(mLrcPadding, y - (staticLayout.height shr 1))
        staticLayout.draw(canvas)
        canvas.restore()
    }

    private fun initPlayDrawable() {
        val l = (mTimeTextWidth - mDrawableWidth) / 2
        val t = height / 2 - mDrawableWidth / 2
        val r = l + mDrawableWidth
        val b = t + mDrawableWidth
        mPlayDrawable!!.setBounds(l, t, r, b)
    }

    /**
     * 重置
     *
     */
    private fun reset() {
        endAnimation()
        mScroller!!.forceFinished(true)
        isShowTimeline = false
        isTouching = false
        removeCallbacks(hideTimelineRunnable)
        mLrcEntryList.clear()
        mOffset = 0f
        mCurrentLine = 0
        invalidate()
    }

    private val hideTimelineRunnable = Runnable {
        if (hasLrc() && isShowTimeline) {
            isShowTimeline = false
            smoothScrollTo(mCurrentLine)
        }
    }

    /**
     * 歌词是否有效
     *
     * @return true，如果歌词有效，否则false
     */
    fun hasLrc(): Boolean {
        return mLrcEntryList.isNotEmpty()
    }

    /**
     * 滚动到某一行
     */
    private fun smoothScrollTo(line: Int) {
        smoothScrollTo(line, mAnimationDuration)
    }

    /**
     * 滚动到某一行
     */
    private fun smoothScrollTo(line: Int, duration: Long) {
        val offset: Float = getOffset(line)
        endAnimation()
        mAnimator = ValueAnimator.ofFloat(mOffset, offset)
        mAnimator?.apply {
            this.duration = duration
            interpolator = LinearInterpolator()
            addUpdateListener { animation: ValueAnimator ->
                mOffset = animation.animatedValue as Float
                invalidate()
            }
        }
        LrcUtils.resetDurationScale()
        mAnimator!!.start()
    }

    private fun getOffset(line: Int): Float {
        if (mLrcEntryList[line].offset == Float.MIN_VALUE) {
            var offset = height / 2.toFloat()
            for (i in 1..line) {
                offset -= (mLrcEntryList[i - 1].getHeight() + mLrcEntryList[i]
                    .getHeight() shr 1) + mDividerHeight
            }
            mLrcEntryList[line].offset = offset
        }
        return mLrcEntryList[line].offset
    }

    /**
     * 结束滚动动画
     */
    private fun endAnimation() {
        if (mAnimator != null && mAnimator!!.isRunning) {
            mAnimator!!.end()
        }
    }

    /**
     * 主线程中运行
     *
     * @param r
     */
    private fun runOnUi(r: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            r.run()
        } else {
            post(r)
        }
    }
}