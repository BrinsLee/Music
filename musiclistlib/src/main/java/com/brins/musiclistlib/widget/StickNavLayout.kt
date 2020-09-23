package com.brins.musiclistlib.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.view.animation.Interpolator
import android.widget.LinearLayout
import android.widget.OverScroller
import androidx.core.view.NestedScrollingParent
import androidx.recyclerview.widget.RecyclerView
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.musiclistlib.R
import kotlin.math.abs
import kotlin.math.sqrt

class StickNavLayout(context: Context, attributeSet: AttributeSet? = null) :
    LinearLayout(context, attributeSet), NestedScrollingParent {

    companion object {
        val TAG = "StickNavLayout"
    }

    private var mView: View? = null
    private var mScroller: OverScroller? = null
    private var mVelocityTracker: VelocityTracker? = null
    private var mOffsetAnimator: ValueAnimator? = null
    private var mInterpolator: Interpolator? = null

    private var mTouchSlop = 0
    private var mMaximumVelocity = 0
    private var mMinimumVelocity = 0
    private var mTopViewHeight = 0
    private var listener: MyStickyListener? = null
    private var barheight = 0

    private var mRvTop = -1

    interface MyStickyListener {
        fun imageScale(v: Float)
    }

    init {
        orientation = LinearLayout.VERTICAL
        mScroller = OverScroller(context)
        mMaximumVelocity =
            ViewConfiguration.get(context).scaledMaximumFlingVelocity
        mMinimumVelocity =
            ViewConfiguration.get(context).scaledMinimumFlingVelocity

        this.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                this@StickNavLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                mRvTop = mView!!.top
            }
        })
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mView = findViewById(R.id.musicRecycler)
    }


    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (mRvTop == -1) {
            mRvTop = mView!!.top
        }
        val moveY = sqrt(abs(dy) * 2.toDouble()).toInt()
        if (dy < 0) {
            if (scrollY == 0 && mView!!.top >= mRvTop) {
                mView?.apply {
                    layout(this.left, this.top + moveY, this.right, this.bottom + moveY)
                    listener?.imageScale(this.top.toFloat())
                    consumed[1] = dy
                }
            } else if (scrollY > 0 && !target.canScrollVertically(-1)) {
                if (scrollY + dy < 0) {
                    scrollTo(0, 0)
                } else {
                    scrollTo(0, scrollY + dy)
                    consumed[1] = dy
                }
            }
        } else if (dy > 0) {
            if (mView!!.top > mRvTop) {
                if (mView!!.top - moveY < mRvTop) {
                    mView?.apply {
                        layout(this.left, mRvTop, this.right, mRvTop + this.height)
                        listener?.imageScale(mRvTop.toFloat())
                        consumed[1] = dy
                    }

                } else {
                    mView?.apply {
                        layout(this.left, this.top - moveY, this.right, this.bottom - moveY)
                        listener?.imageScale(this.top.toFloat())
                        consumed[1] = dy
                    }
                }
            } else if (scrollY < dp2px(155f)) {
                if (scrollY + dy > dp2px(155f)) {
                    scrollTo(0, dp2px(155f))
                    consumed[1] = dy
                } else {
                    scrollTo(0, scrollY + dy)
                    consumed[1] = dy
                }
            }
        }
    }

    override fun onStopNestedScroll(child: View) {
        if (mView!!.getTop() != mRvTop) {
            mView?.apply {
                layout(this.left, mRvTop, this.right, mRvTop + this.height)
                listener?.imageScale(mRvTop.toFloat())

            }
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        var consume = consumed
        if (target is RecyclerView && velocityY < 0) {
            val recyclerView = target
            val firstChild: View = recyclerView.getChildAt(0)
            val childAdapterPosition: Int = recyclerView.getChildAdapterPosition(firstChild)
            val consume = childAdapterPosition > 3
        }
        if (!consume) {
            animateScroll(velocityY, computeDuration(0f), consume)
        } else {
            animateScroll(velocityY, computeDuration(velocityY), consume)
        }
        return true
    }

    fun animateScroll(velocityY: Float, duration: Int, consumed: Boolean) {
        val currentOffset = scrollY
        val topHeight: Int = mView!!.getTop()

        if (mOffsetAnimator == null) {
            mOffsetAnimator = ValueAnimator()
            mOffsetAnimator!!.interpolator = mInterpolator
            mOffsetAnimator!!.addUpdateListener { animation ->
                if (animation.animatedValue is Int) {
                    scrollTo(0, (animation.animatedValue as Int))
                }
            }
        } else {
            mOffsetAnimator!!.cancel()
        }
        mOffsetAnimator!!.duration = Math.min(duration, 600).toLong()
        if (velocityY >= 0) {
            mOffsetAnimator!!.setIntValues(
                currentOffset,
                mView!!.top - dp2px(65f)
            )
            mOffsetAnimator!!.start()
        } else {
            if (!consumed) {
                mOffsetAnimator!!.setIntValues(currentOffset, 0)
                mOffsetAnimator!!.start()
            }
        }
    }

    override fun getNestedScrollAxes(): Int {
        return 0
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercepted = false
        when (ev!!.action) {

            MotionEvent.ACTION_UP ->
                if (mView!!.getTop() > mRvTop) {
                    intercepted = true
                }
        }
        return intercepted
    }

    private fun computeDuration(velocityY: Float): Int {
        var velocityY = velocityY
        val distance: Int
        if (velocityY > 0) {
            //鼠标往上
            distance = Math.abs(mView!!.getTop() - scrollY)
        } else {
            //鼠标往下
            distance = Math.abs(scrollY)
        }
        val duration: Int
        velocityY = Math.abs(velocityY)
        duration = if (velocityY > 0) {
            3 * Math.round(1000 * (distance / velocityY))
        } else {
            val distanceRadtio = distance / height.toFloat()
            ((distanceRadtio + 1) * 150).toInt()
        }
        return duration
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_UP -> mView?.layout(
                mView!!.left,
                mRvTop,
                mView!!.right,
                mRvTop + mView!!.height
            )
        }
        return super.onTouchEvent(event)
    }

    fun setListener(listener: MyStickyListener) {
        this.listener = listener
    }
}