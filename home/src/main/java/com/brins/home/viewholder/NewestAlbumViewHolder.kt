package com.brins.home.viewholder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.brins.baselib.utils.ActivityManager
import com.brins.home.R
import com.brins.home.adapter.NewAlbumViewPageAdapter
import com.brins.networklib.model.album.AlbumResult
import com.brins.networklib.model.album.NewestAlbum
import com.chad.library.adapter.base.BaseViewHolder
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class NewestAlbumViewHolder(itemView: View, var manager: FragmentManager) :
    BaseViewHolder<AlbumResult<NewestAlbum>>(itemView) {

    private var mIndicator: MagicIndicator = itemView.findViewById(R.id.magicIndicator_newest_album)
    private var mViewPager: ViewPager = itemView.findViewById(R.id.vp_album)
    private var commonNavigator = CommonNavigator(mContext)
    private var mList = arrayListOf("新专辑", "新歌")
    private lateinit var mAdapter: NewAlbumViewPageAdapter

    init {
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ColorTransitionPagerTitleView(context)
                simplePagerTitleView.normalColor = Color.GRAY
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.text = mList[index]
                simplePagerTitleView.setOnClickListener {
                    mViewPager.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getCount(): Int {
                return mList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
                linePagerIndicator.setColors(Color.BLACK)
                return linePagerIndicator
            }

        }

        mIndicator.navigator = commonNavigator
        val titleContainer =
            commonNavigator.titleContainer // must after setNavigator
        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return UIUtil.dip2px(mContext, 15.0)
            }
        }
        val fragmentContainerHelper =
            FragmentContainerHelper(mIndicator)
        fragmentContainerHelper.setInterpolator(OvershootInterpolator(2.0f))
        fragmentContainerHelper.setDuration(300)
        mViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                fragmentContainerHelper.handlePageSelected(position)
            }
        })
        mAdapter = NewAlbumViewPageAdapter(manager)
        mViewPager.adapter = mAdapter
    }


}