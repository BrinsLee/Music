package com.brins.baselib.utils

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.brins.baselib.R
import com.brins.baselib.utils.SizeUtils.px2dp
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.min

class AppBarZoomBehavior @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) :
    AppBarLayout.Behavior(context, attributeSet) {

    private var mImageView: ImageView? = null
    private var mImageBackground: ImageView? = null
    private var mAppbarHeight = 0 //记录AppbarLayout原始高度
    private var mImageViewHeight = 0 //记录ImageView原始高度

    private var mTotalDy: Float = 0f//手指在Y轴滑动的总距离
    private var mScaleValue = 0f//图片缩放比例

    private var mLastBottom = 0//Appbar的变化高度


    private var isAnimate = false //是否做动画标志

    private var valueAnimator: ValueAnimator? = null

    companion object {

        private val TAG = "AppBarZoomBehavior"
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
        mImageBackground = abl.findViewWithTag("coverBg")
        if (mImageView != null) {
            mImageViewHeight = mImageView!!.height
        }
    }

    /**
     * 是否处理嵌套滑动
     * 当手指按下屏幕的时候触发，用来决定是否要让Behavior处理这次滑动，true为处理，false为不处理，
     * 如果不处理，那么Behavior的后续方法也就不会在再调用了，方法中也提供了一些辅助参数，
     * 比如type，可以用来判断用户动作，比如是TYPE_TOUCH按住屏幕拖动，TYPE_NON_TOUCH快速拉动屏幕等。

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
     * 嵌套滚动发生之前被调用
     * 在nested scroll child 消费掉自己的滚动距离之前，嵌套滚动每次被nested scroll child
     * 更新都会调用onNestedPreScroll。注意有个重要的参数consumed，可以修改这个数组表示你消费
     * 了多少距离。假设用户滑动了100px,child 做了90px 的位移，你需要把 consumed［1］的值改成90，
     * 这样coordinatorLayout就能知道只处理剩下的10px的滚动。
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param dx  用户水平方向的滚动距离
     * @param dy  用户竖直方向的滚动距离
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
        mTotalDy = Math.min(mTotalDy, MAX_ZOOM_HEIGHT)
        mScaleValue = Math.max(1f, 1f + mTotalDy / MAX_ZOOM_HEIGHT)
        mImageView?.scaleX = mScaleValue
        mImageView?.scaleY = mScaleValue
        mLastBottom = mAppbarHeight + (mImageViewHeight / 2 * (mScaleValue - 1)).toInt()
        abl.bottom = mLastBottom
        (mImageView?.parent as? ConstraintLayout)?.apply {
            bottom = mLastBottom
        }
    }


    /**
     * 当用户快速滑动屏幕，产生惯性滑动的时候，会触发此方法，这个方法参数中提供了滑动方向与速度。
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
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

    /**
     * 滑动停止的时候调用，如果没有发生惯性滑动，那么会直接到这个方法。
     *
     * @param coordinatorLayout
     * @param child
     * @param target
     * @param type
     */
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
                    val bottom =
                        (mLastBottom - (mLastBottom - mAppbarHeight) * animation.animatedFraction).toInt()
                    abl.bottom = bottom
                    (mImageView?.parent as? ConstraintLayout)?.apply {
                        this.bottom = bottom
                    }
                }
                valueAnimator?.start()
            } else {
                mImageView?.scaleX = 1f
                mImageView?.scaleY = 1f
                abl.bottom = mAppbarHeight
                (mImageView?.parent as? ConstraintLayout)?.apply {
                    this.bottom = mAppbarHeight
                }
            }
        }
    }
}