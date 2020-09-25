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
        mList.add(UniversalFragment.newInstance("0", 0))
/*        mList.add(UniversalFragment.newInstance("1", 1))
        mList.add(UniversalFragment.newInstance("2", 2))
        mList.add(UniversalFragment.newInstance("3", 3))
        mList.add(UniversalFragment.newInstance("4", 4))*/

    }

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ((mList[position] as UniversalFragment).getTitle())
    }


    override fun getItemId(position: Int): Long {
        return mList[position].id.toLong()
    }
}