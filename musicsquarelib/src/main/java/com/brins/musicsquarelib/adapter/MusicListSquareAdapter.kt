package com.brins.musicsquarelib.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.musicsquarelib.fragment.RecommendFragment
import com.brins.musicsquarelib.fragment.UniversalFragment

class MusicListSquareAdapter(manager: FragmentManager) : FragmentPagerAdapter(
    manager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val mList: ArrayList<Fragment> = ArrayList()

    init {
        mList.add(RecommendFragment())
        mList.add(UniversalFragment.newInstance("华语", 0))
        mList.add(UniversalFragment.newInstance("古风", 1))
        mList.add(UniversalFragment.newInstance("欧美", 2))
        mList.add(UniversalFragment.newInstance("流行", 3))
        mList.add(UniversalFragment.newInstance("电子", 4))

    }

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }
}