package com.brins.musicdetail.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.musicdetail.fragment.MusicDetailFragment
import com.brins.musicdetail.fragment.MusicLrcFragment

class MusicDetailAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val mList: ArrayList<Fragment> = ArrayList()

    init {
        mList.add(MusicDetailFragment())
        mList.add(MusicLrcFragment())
    }

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }

}