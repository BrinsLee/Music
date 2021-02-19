package com.brins.loacl.activity

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseActivity
import com.brins.baselib.route.RouterHub.Companion.LOCALMUSICACTIVITY
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.hasPermissions
import com.brins.baselib.utils.requestPermission
import com.brins.baselib.widget.CommonHeaderView
import com.brins.baselib.widget.PermissionDialog
import com.brins.loacl.R
import com.brins.loacl.adapter.LocalMusicPagerAdapter
import com.brins.loacl.widget.ClipPagerTitleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_local_music.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import javax.inject.Inject

@Route(path = LOCALMUSICACTIVITY)
@AndroidEntryPoint
class LocalMusicActivity : BaseActivity() {

    private var mTitleList: ArrayList<String> = arrayListOf("单曲", "歌手", "专辑")

    @Inject
    lateinit var mLocalMusicAdapter: LocalMusicPagerAdapter
    override fun getLayoutResId(): Int {
        return R.layout.activity_local_music
    }

    override fun init(savedInstanceState: Bundle?) {
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }
        })
        if (hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            initViewPager()
        } else {

            PermissionDialog(
                this,
                UIUtils.getString(R.string.permission_storage),
                UIUtils.getString(R.string.permission_storage_target),
                object : PermissionDialog.OnNextClickListener {
                    override fun onNextClick() {
                        requestPermission(
                            this@LocalMusicActivity,
                            ::onGranted,
                            ::onDenied,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    }

                }).show()
        }
    }

    fun onGranted(data: List<String>) {
        initViewPager()
    }

    fun onDenied(data: List<String>) {

    }

    private fun initViewPager() {
        vp_music_detail.offscreenPageLimit = 2
        vp_music_detail.adapter = mLocalMusicAdapter
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val clipPagerTitle = ClipPagerTitleView(this@LocalMusicActivity)
                clipPagerTitle.setText(mTitleList[index])
                clipPagerTitle.setTextColor(UIUtils.getColor(R.color.lrc_normal_text_color))
                clipPagerTitle.setClipColor(Color.TRANSPARENT)
                clipPagerTitle.setOnClickListener {
                    vp_music_detail.currentItem = index
                }
                return clipPagerTitle
            }

            override fun getCount(): Int {
                return mTitleList.size
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                val navigatorHeight = context.resources.getDimension(R.dimen.base_navigator_height2)
                val borderWidth = UIUtil.dip2px(context, 1.0).toFloat()
                val lineHeight = navigatorHeight - 2 * borderWidth
                indicator.lineHeight = lineHeight
                indicator.roundRadius = lineHeight / 2
                indicator.yOffset = borderWidth
                indicator.setColors(UIUtils.getColor(R.color.normal_text_color))
                return indicator
            }

        }

        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, vp_music_detail)

    }
}