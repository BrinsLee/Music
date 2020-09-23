package com.brins.musiclistlib.utils

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.brins.musiclistlib.R
import com.google.android.material.appbar.AppBarLayout

class AppBarZoomBehavior @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) :
    AppBarLayout.Behavior(context, attributeSet) {

    private var mImageView: ImageView? = null
    private var mAppbarHeight = 0 //记录AppbarLayout原始高度
    private var mImageViewHeight = 0 //记录ImageView原始高度

    private var mTotalDy: Float = 0f//手指在Y轴滑动的总距离
    private var mScaleValue = 0f//图片缩放比例

    private var mLastBottom = 0//Appbar的变化高度


    private var isAnimate = false //是否做动画标志

    private var valueAnimator: ValueAnimator? = null

    companion object {

        private val MAX_ZOOM_HEIGHT = 300f//放大最大高度
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        abl: AppBarLayout,
        layoutDirection: Int
    ): Boolean {
        val handled = super.onLayoutChild(parent, abl, layoutDirection)
        init(abl)
        return handled
    }

    /**
     * 初始化操作，在这里获取到ImageView的引用，和Appbar的原始高度
     *
     * @param abl
     */
    private fun init(abl: AppBarLayout) {
        abl.clipChildren = false
        mAppbarHeight = abl.height
        mImageView = abl.findViewById(R.id.cover)
        if (mImageView != null) {
            mImageViewHeight = mImageView!!.height
        }
    }

    /**
     * 是否处理嵌套滑动
     *
     * @param parent
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @param type
     * @return
     */
    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        isAnimate = true
        return isAnimate
    }

    /**
     * 做具体滑动处理
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        if (mImageView != null && child.bottom >= mAppbarHeight &&
            dy < 0 && type == ViewCompat.TYPE_TOUCH
        ) {
            zoomHeaderImageView(child, dy)
        } else {
            if (mImageView != null && child.bottom > mAppbarHeight && dy > 0 && type == ViewCompat.TYPE_TOUCH) {
                consumed[1] = dy
                zoomHeaderImageView(child, dy)
            } else {
                if (valueAnimator == null || !valueAnimator!!.isRunning) {
                    super.onNestedPreScroll(
                        coordinatorLayout,
                        child,
                        target,
                        dx,
                        dy,
                        consumed,
                        type
                    )
                }
            }
        }
    }

    private fun zoomHeaderImageView(
        abl: AppBarLayout,
        dy: Int
    ) {
        mTotalDy += -dy.toFloat()
        mTotalDy = mTotalDy.coerceAtMost(MAX_ZOOM_HEIGHT)
        mScaleValue = 1f.coerceAtLeast(1f + mTotalDy / MAX_ZOOM_HEIGHT)
        mImageView?.scaleX = mScaleValue
        mImageView?.scaleY = mScaleValue
        mLastBottom = mAppbarHeight + (mImageViewHeight / 2 * (mScaleValue - 1)).toInt()
        abl.bottom = mLastBottom
    }


    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (velocityY > 100) {
            isAnimate = false
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        type: Int
    ) {
        recovery(child)
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
    }

    private fun recovery(abl: AppBarLayout) {
        if (mTotalDy > 0) {
            mTotalDy = 0f
            if (isAnimate) {
                valueAnimator =
                    ValueAnimator.ofFloat(mScaleValue, 1f).setDuration(220)
                valueAnimator?.addUpdateListener { animation ->
                    val value = animation.animatedValue as Float
                    mImageView?.scaleX = value
                    mImageView?.scaleY = value
                    abl.bottom =
                        (mLastBottom - (mLastBottom - mAppbarHeight) * animation.animatedFraction).toInt()
                }
                valueAnimator?.start()
            } else {
                mImageView?.scaleX = 1f
                mImageView?.scaleY = 1f
                abl.bottom = mAppbarHeight
            }
        }
    }
}