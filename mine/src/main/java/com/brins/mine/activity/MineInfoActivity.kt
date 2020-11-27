package com.brins.mine.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.route.RouterHub.Companion.MINEINFOACTIVITY
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.utils.setTranslucent
import com.brins.mine.R
import com.brins.mine.fragment.MineActivityFragment
import com.brins.mine.fragment.MineHomeFragment
import com.brins.mine.presenter.MinePresenter
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_mine_info.*
import kotlinx.android.synthetic.main.mine_info_header.*
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import kotlin.math.abs

@Route(path = MINEINFOACTIVITY)
class MineInfoActivity : BaseMvpActivity<MinePresenter>() {
    private var mTitleList: ArrayList<String> = arrayListOf("主页", "动态")
    private var mAdapter: MineInfoViewPageAdapter = MineInfoViewPageAdapter(supportFragmentManager)
    override fun getLayoutResId(): Int {
        return R.layout.activity_mine_info
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.setContentInsetsAbsolute(0, 0)
        if (LoginCache.isLogin && LoginCache.userProfile != null) {
            LoginCache.userProfile?.apply {
                GlideHelper.setImageResource(cover, this.backgroundUrl)
                GlideHelper.setImageResource(iv_avatar, this.avatarUrl)
                tv_title.text = this.nickname
                tv_nickname.text = this.nickname
                tv_follows.text = "关注：${this.follows}"
                tv_fans.text = "粉丝：${this.followeds}"
            }
        }

        appbar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                val percent =
                    abs(verticalOffset * 1.0f) / appBarLayout!!.totalScrollRange //滑动比例

                if (percent > 0.8) {
                    tv_title.visibility = View.VISIBLE
                    tv_focus.visibility = View.VISIBLE
                    val alpha = 1 - (1 - percent) * 5
                    tv_title.alpha = alpha
                    tv_focus.alpha = alpha
                    ll_mine_info.alpha = (1 - percent) * 5

                } else {
                    tv_title.visibility = View.GONE
                    tv_focus.visibility = View.GONE
                }

            }

        })

        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ColorTransitionPagerTitleView(context)
                simplePagerTitleView.text = mTitleList[index]
                simplePagerTitleView.normalColor = UIUtils.getColor(R.color.default_btn_text)
                simplePagerTitleView.selectedColor = UIUtils.getColor(R.color.black)
                simplePagerTitleView.textSize = 15f
                simplePagerTitleView.setOnClickListener {
                    vp_mine_info.currentItem = index
                }
                return simplePagerTitleView

            }

            override fun getCount(): Int {
                return mTitleList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_MATCH_EDGE
                linePagerIndicator.lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
                linePagerIndicator.setColors(Color.BLACK)
                return linePagerIndicator
            }

        }
        magicIndicator_mine_info.navigator = commonNavigator
        val titleContainer = commonNavigator.titleContainer
        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return UIUtil.dip2px(mContext, 15.0)
            }
        }
        val fragmentContainerHelper =
            FragmentContainerHelper(magicIndicator_mine_info)
        fragmentContainerHelper.setInterpolator(OvershootInterpolator(2.0f))
        fragmentContainerHelper.setDuration(300)
        vp_mine_info.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {
                fragmentContainerHelper.handlePageSelected(position)
            }
        })
        vp_mine_info.adapter = mAdapter
        ViewPagerHelper.bind(magicIndicator_mine_info, vp_mine_info)
    }

    override fun setStatusBar() {
        setTranslucent(this)
    }

    class MineInfoViewPageAdapter(manager: FragmentManager) : FragmentPagerAdapter(
        manager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

        private val mList: ArrayList<Fragment> = ArrayList()

        init {
            mList.add(MineHomeFragment())
            mList.add(MineActivityFragment())
        }

        override fun getItem(position: Int): Fragment {
            return mList[position]
        }

        override fun getCount(): Int {
            return mList.size
        }

    }
}