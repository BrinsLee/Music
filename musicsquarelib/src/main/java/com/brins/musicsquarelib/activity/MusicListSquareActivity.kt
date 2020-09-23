package com.brins.musicsquarelib.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.route.RouterHub.Companion.MUSICLISTSQUAREACTIVITY
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.getStatusBarHeight
import com.brins.musicsquarelib.R
import com.brins.musicsquarelib.adapter.MusicListSquareAdapter
import com.brins.musicsquarelib.presenter.MusicListSquarePresenter
import kotlinx.android.synthetic.main.activity_music_list_square.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

@Route(path = MUSICLISTSQUAREACTIVITY)
class MusicListSquareActivity : BaseMvpActivity<MusicListSquarePresenter>() {

    private var mAdapter: MusicListSquareAdapter? = null
    private var mTitleList: ArrayList<String> = arrayListOf("推荐", "官方", "精品", "欧美", "流行", "电子")

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_list_square
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        header.setPadding(0, getStatusBarHeight(this), 0, 0)
        initViewPager()

    }

    private fun initViewPager() {
        mAdapter = MusicListSquareAdapter(supportFragmentManager)
        vp_music_square.adapter = mAdapter
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ColorTransitionPagerTitleView(context)
                simplePagerTitleView.text = mTitleList[index]
                simplePagerTitleView.normalColor = Color.GRAY
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.textSize = 14f
                simplePagerTitleView.setOnClickListener {
                    vp_music_square.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getCount(): Int {
                return mTitleList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
                linePagerIndicator.setColors(Color.BLACK)
                return linePagerIndicator
            }

        }

        magic_indicator.navigator = commonNavigator
        val titleContainer =
            commonNavigator.titleContainer
        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerPadding = UIUtil.dip2px(this, 15.0)
        titleContainer.dividerDrawable = UIUtils.getDrawable(R.drawable.base_bg_simple_divider)
        ViewPagerHelper.bind(magic_indicator, vp_music_square)
    }
}