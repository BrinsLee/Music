package com.brins.musicdetail.activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.widget.LinearLayout
import androidx.core.animation.addListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.PlayMode
import com.brins.baselib.route.RouterHub
import com.brins.baselib.utils.SizeUtils
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.utils.hideStatusBar
import com.brins.musicdetail.R
import com.brins.musicdetail.adapter.MusicDetailAdapter
import com.brins.musicdetail.fragment.MusicDetailFragment
import com.brins.musicdetail.fragment.MusicLrcFragment
import com.brins.playerlib.contract.PlayerContract
import com.brins.playerlib.model.PlayBackService
import com.brins.playerlib.presenter.PlayerPresenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_music_detail.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.system.exitProcess

@AndroidEntryPoint
@Route(path = RouterHub.MUSICDETAILACTIVITY)
class MusicDetailActivity : BaseMvpActivity<PlayerPresenter>(), PlayerContract.View,
    View.OnClickListener, Animator.AnimatorListener {

    private var mTitleList: ArrayList<String> = arrayListOf("歌曲", "歌词")
    private var mPlayer: PlayBackService? = null
    private var animator: ObjectAnimator? = null
    private var mAdapter: MusicDetailAdapter? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_detail
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        back_iv.setOnClickListener(this)
        mPresenter?.bindPlaybackService()
        initViewPager()
    }

    private fun initViewPager() {
        mAdapter = MusicDetailAdapter(supportFragmentManager)
        vp_music_detail.adapter = mAdapter
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ColorTransitionPagerTitleView(context)

                simplePagerTitleView.text = mTitleList[index]
                simplePagerTitleView.normalColor = UIUtils.getColor(R.color.white_50)
                simplePagerTitleView.selectedColor = UIUtils.getColor(R.color.white)
                simplePagerTitleView.textSize = 15f
                simplePagerTitleView.setOnClickListener {
                    vp_music_detail.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getCount(): Int {
                return mTitleList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.lineWidth = 0f
                indicator.lineHeight = 0f
                return indicator
            }

        }

        magic_indicator.navigator = commonNavigator
        val titleContainer =
            commonNavigator.titleContainer // must after setNavigator

        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerPadding = UIUtil.dip2px(this, 15.0)
        titleContainer.dividerDrawable = UIUtils.getDrawable(R.drawable.base_bg_simple_divider)
        ViewPagerHelper.bind(magic_indicator, vp_music_detail)
    }

    override fun setStatusBar() {
        hideStatusBar(this)
    }

    override fun onPlaybackServiceBound(service: PlayBackService) {
        mPlayer = service
        mPlayer!!.getPlayingSong()?.let {
            var cover = it.song?.album?.blurPicUrl
            if (cover == null || cover.isEmpty()) {
                cover = it.song?.picUrl
            }
            GlideHelper.setBlurImageResource(cover_bg, cover, 100f)
        }
    }

    override fun onPlaybackServiceUnbound() {
        mPlayer = null
    }

    override fun onSongUpdated(song: BaseMusic?) {
        mPlayer!!.getPlayingSong()?.let {
            var cover = it.song?.album?.blurPicUrl
            if (cover == null || cover.isEmpty()) {
                cover = it.song?.picUrl
            }
            GlideHelper.setBlurImageResource(cover_bg, cover, 100f)
        }
        mAdapter?.let {
            (it.getItem(0) as MusicDetailFragment).initMusicDetail()
            (it.getItem(1) as MusicLrcFragment).initMusicLrc()

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateSong(params: EventBusParams) {
        when (params.key) {
            EventBusKey.KEY_EVENT_UPDATE_MUSIC -> {
                onSongUpdated(params.`object` as BaseMusic?)
            }
        }
    }

    override fun onSongPlay() {

    }

    override fun onSongPause() {

    }

    override fun updatePlayMode(playMode: PlayMode) {
        TODO("Not yet implemented")
    }

    override fun onMusicDelete() {

    }

    override fun onDestroy() {
        onPlaybackServiceUnbound()
        super.onDestroy()
    }

    override fun onClick(p0: View) {
        when (p0.id) {

            R.id.back_iv -> {
                finishWithAnimation()
            }
        }
    }

    private fun finishWithAnimation() {
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(
                container,
                "translationY",
                0f,
                SizeUtils.getScreenHeight(this, false).toFloat()
            )
            animator?.apply {
                duration = 800
                interpolator = AccelerateDecelerateInterpolator()
                addListener(this@MusicDetailActivity)
            }
        }
        animator!!.start()
    }

    override fun onBackPressed() {
        finishWithAnimation()
    }

    fun getPlayBackService() = mPlayer

    override fun onAnimationRepeat(p0: Animator?) {

    }

    override fun onAnimationEnd(p0: Animator?) {
        finish()
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationStart(p0: Animator?) {
    }
}