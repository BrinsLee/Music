package com.brins.video.fragment

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.brins.baselib.config.*
import com.brins.baselib.fragment.BaseFragment
import com.brins.baselib.utils.UIUtils
import com.brins.video.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : BaseFragment() {

    private val changeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val count = tab_layout.tabCount
            for (i in 0 until count) {
                val tab = tab_layout.getTabAt(i)
                val textView = tab?.customView as TextView
                if (tab.getPosition() == position) {
                    textView.setTextSize(13f)
                    textView.setTypeface(Typeface.DEFAULT_BOLD)
                } else {
                    textView.setTextSize(13f)
                    textView.setTypeface(Typeface.DEFAULT)
                }
            }
        }
    }

    private var mediator: TabLayoutMediator? = null

    private var list = mutableListOf<Fragment>(
        VideoCategoryFragment.getInstance(MAINLAND),
        VideoCategoryFragment.getInstance(HONGKONG_TAIWAN),
        VideoCategoryFragment.getInstance(EUROPE_AMERICA),
        VideoCategoryFragment.getInstance(JAPAN),
        VideoCategoryFragment.getInstance(KOREA)

    )

    private var titleList = mutableListOf<String>(
        MAINLAND,
        HONGKONG_TAIWAN,
        EUROPE_AMERICA,
        JAPAN,
        KOREA

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_video
    }

    override fun reLoad() {

    }

    override fun init(savedInstanceState: Bundle?) {
        vp_video.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        vp_video.adapter = object : FragmentStateAdapter(childFragmentManager, getLifecycle()) {
            override fun getItemCount(): Int {
                return list.size
            }

            override fun createFragment(position: Int): Fragment {
                return list[position]
            }

        }
        vp_video.registerOnPageChangeCallback(changeCallback)
        mediator = TabLayoutMediator(
            tab_layout,
            vp_video,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                val textView = TextView(getMyContext())
                val states = arrayOfNulls<IntArray>(2)
                states[0] = intArrayOf(android.R.attr.state_selected)
                states[1] = intArrayOf()
                val colors = intArrayOf(
                    UIUtils.getColor(R.color.black),
                    UIUtils.getColor(R.color.default_btn_text)
                )
                val colorStateList = ColorStateList(states, colors)
                textView.setText(titleList[position])
                textView.setTextSize(13f)
                textView.setTextColor(colorStateList)
                tab.setCustomView(textView)
            })

        mediator?.attach()
    }

    companion object {
        fun getInstance(): VideoFragment {
            return VideoFragment()
        }
    }

    override fun onDestroy() {
        mediator?.detach()
        vp_video.unregisterOnPageChangeCallback(changeCallback)
        super.onDestroy()
    }
}