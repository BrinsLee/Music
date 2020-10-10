package com.brins.home.viewholder

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.brins.baselib.config.KEY_URL
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.home.R
import com.brins.networklib.model.banner.BannerResult
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import java.util.*

class BannerViewHolder(itemView: View) : BaseViewHolder<BannerResult>(itemView),
    View.OnTouchListener {

    companion object {
        private const val TAG = "BannerViewHolder"
        private const val DEFAULT_CYCLE_INTERVAL: Long = 5000

    }

    private var mIsTouching = false
    private var mPreSelected = -1
    private val mDotMargin = 2.5f
    private val mDotSize = 6

    private var mImageViews: ArrayList<View>

    /**
     * 下方的下原点View
     */
    private var mDotViewList: ArrayList<View>

    /**
     * 下方小点展示的Root
     */
    private var mDotRoot: LinearLayout? = null

    /**
     * 广告viewpager
     */
    private var mViewPager: ViewPager

    /**
     * 根布局
     */
    private var mRootLayout: ConstraintLayout? = null

    private var mPagerAdapter: CycleImageViewPagerAdapter? = null


    private var mCycleHandler: Handler

    private val mInterval = DEFAULT_CYCLE_INTERVAL


    private val mDelayRunnable = Runnable { showNextView() }


    init {
        mCycleHandler = Handler(Looper.getMainLooper())
        mImageViews = ArrayList()
        mDotViewList = ArrayList()
        mPagerAdapter = CycleImageViewPagerAdapter()
        mRootLayout = itemView.findViewById(R.id.holder_banner_root)
        mDotRoot = itemView.findViewById(R.id.view_pager_dot_root)
        mViewPager = itemView.findViewById(R.id.vp_banner)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (mDotViewList.size <= 1) {
                    return
                }

                if (mPreSelected >= 0) {
                    mDotViewList[mPreSelected]
                        .setBackgroundResource(R.drawable.home_shape_view_pager_unselected_dot)
                }
                val reallyPosition: Int = position % mDotViewList.size
                mDotViewList[reallyPosition]
                    .setBackgroundResource(R.drawable.home_shape_view_pager_selected_dot)
                mPreSelected = reallyPosition

                //手动滑动到这里，清零
                mCycleHandler.removeCallbacksAndMessages(null)
                mCycleHandler.postDelayed(mDelayRunnable, mInterval)

                val view = mImageViews[reallyPosition]
                if (view.tag != null) {
                    val url = view.tag as String
                    val commonImageView: RoundedImageView =
                        view.findViewById(R.id.banner_image)
                    GlideHelper.setImageResource(
                        commonImageView,
                        url,
                        R.drawable.base_icon_default_picture,
                        object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable?>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                if (null != resource) {
                                    commonImageView.setImageDrawable(resource)
                                }
                                return true
                            }

                        })
                    view.tag = null
                }
            }

        })
        mViewPager.setOnTouchListener(this)
    }

    override fun setData(data: BannerResult?) {
        val lastData = mData
        super.setData(data)
        if (data?.bannners == null) {
            return
        }
        mPreSelected = -1

        //检查数据，如果数据完全一致，则不刷新ViewPager
        var isAllSame = true

        mImageViews.clear()
        mDotViewList.clear()
        mDotRoot!!.removeAllViews()
        for (i in 0 until mData.bannners!!.size) {
            val view: View = LayoutInflater.from(mContext)
                .inflate(R.layout.home_widget_item_banner, null)
            val bannerImage: RoundedImageView = view.findViewById(R.id.banner_image)
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            bannerImage.setOnClickListener {
                if (mData.bannners!![i].url.isNullOrEmpty()) {
                    /*if (mData.bannners!![i].song == null) {
                        mData.bannners!![i].song = Music().setId(mData.bannners!![i].targetId)
                    }*/
                    mData.bannners!![i].song?.let {
                        EventBusManager.post(
                            EventBusKey.KEY_EVENT_BANNER_MUSIC, it
                        )
                    }
                } else {
                    val bundle = Bundle()
                    bundle.putString(KEY_URL, mData.bannners!![i].url)
                    ARouterUtils.go(RouterHub.BROWSERACTIVITY, bundle)
                }
            }

            if (i == 0) {
                GlideHelper.setRoundImageResource(bannerImage, mData.bannners!![0].picUrl, 8)
                /*GlideHelper.setImageResource(
                    bannerImage,
                    mData.bannners!![0].picUrl,
                    R.drawable.base_icon_default_picture,
                    object : RequestListener<Drawable?> {
                        override fun onLoadFailed(
                            @Nullable e: GlideException?,
                            model: Any,
                            target: Target<Drawable?>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any,
                            target: Target<Drawable?>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (null != resource) {
                                bannerImage.setImageDrawable(resource)
                            }
                            return true
                        }
                    })*/
            }

            view.tag = mData.bannners!![i].picUrl
            bannerImage.scaleType = ImageView.ScaleType.CENTER_CROP
            mImageViews.add(view)
            if (i < mData.bannners!!.size) {
                addDot()
            }

        }

        showViewPager()

    }

    private fun showViewPager() {
        mViewPager.adapter = mPagerAdapter
        if (mData.bannners?.size ?: 0 <= 1) {
            mViewPager.currentItem = 0
            mDotRoot!!.visibility = View.GONE
        } else {
            val currentItem: Int =
                Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % mData.bannners!!.size
            mViewPager.currentItem = currentItem
            startScroll()
            mDotRoot!!.visibility = View.VISIBLE
            for (i in mDotViewList.indices) {
                if (i == currentItem % mDotViewList.size) {
                    mDotViewList[i]
                        .setBackgroundResource(R.drawable.home_shape_view_pager_selected_dot)
                } else {
                    mDotViewList[i]
                        .setBackgroundResource(R.drawable.home_shape_view_pager_unselected_dot)
                }
            }
        }
    }

    private fun addDot() {
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                dp2px(mDotSize.toFloat()),
                dp2px(mDotSize.toFloat())
            )
        layoutParams.leftMargin = dp2px(mDotMargin)
        layoutParams.rightMargin = dp2px(mDotMargin)
        val view = View(mContext)
        view.layoutParams = layoutParams
        view.setBackgroundResource(R.drawable.home_shape_view_pager_unselected_dot)
        mDotViewList.add(view)
        mDotRoot!!.addView(view, layoutParams)
    }

    /**
     * 恢复自动滚动
     */
    fun startScroll() {
        if (mData == null || mData.bannners?.size ?: 0 === 1) {
            return
        }
        mCycleHandler.removeCallbacksAndMessages(null)
        mCycleHandler.postDelayed(mDelayRunnable, mInterval)
    }

    /**
     * 暂停自动滚动
     */
    fun pauseScroll() {
        mCycleHandler.removeCallbacksAndMessages(null)
    }

    private fun showNextView() {
        if (mData == null || mData.bannners?.size ?: 0 === 1 || mIsTouching) {
            return
        }
        mViewPager.setCurrentItem(mViewPager.currentItem + 1, true)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                mIsTouching = true
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mIsTouching = false
            }
        }
        return false
    }

    private inner class CycleImageViewPagerAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View, o: Any): Boolean {
            return view === o
        }

        override fun getCount(): Int {
            return if (mData.bannners?.size ?: 0 <= 1) {
                mData.bannners!!.size
            } else Int.MAX_VALUE
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            if (mImageViews[position % mImageViews.size].parent != null) {
                (mImageViews[position % mImageViews.size]
                    .parent as ViewPager).removeView(mImageViews[position % mImageViews.size])
            }
            container.addView(mImageViews[position % mImageViews.size])

            return mImageViews[position % mImageViews.size]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        }
    }
}