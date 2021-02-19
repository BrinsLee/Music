package com.brins.loacl.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.loacl.fragment.LocalMusicFragment
import javax.inject.Inject

/**
 * Created by lipeilin
 * on 2021/2/18
 */
class LocalMusicPagerAdapter @Inject constructor(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mList: ArrayList<Fragment> = ArrayList()

    init {
        mList.apply {
            add(LocalMusicFragment.getInstance(LocalMusicFragment.TYPE_MUSIC))
            add(LocalMusicFragment.getInstance(LocalMusicFragment.TYPE_ARTIST))
            add(LocalMusicFragment.getInstance(LocalMusicFragment.TYPE_ALBUM))
        }
    }

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }


}