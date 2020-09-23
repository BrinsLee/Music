package com.brins.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.home.fragment.NewAlbumFragment
import com.brins.home.fragment.TopSongFragment

class NewAlbumViewPageAdapter(manager: FragmentManager) : FragmentPagerAdapter(
    manager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private val mList: ArrayList<Fragment> = ArrayList()

    init {
        mList.add(NewAlbumFragment())
        mList.add(TopSongFragment())
    }

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }
}