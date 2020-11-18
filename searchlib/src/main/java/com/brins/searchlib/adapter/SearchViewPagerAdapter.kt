package com.brins.searchlib.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.brins.baselib.config.SEARCH
import com.brins.searchlib.fragment.SearchResultFragment

/**
 * Created by lipeilin
 * on 2020/11/17
 */
class SearchViewPagerAdapter(manager: FragmentManager) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mList: ArrayList<Fragment> = ArrayList()

    init {
        mList.apply {
            add(SearchResultFragment.getInstance(SEARCH.TYPE_MUSIC))
            add(SearchResultFragment.getInstance(SEARCH.TYPE_ALBUM))
            add(SearchResultFragment.getInstance(SEARCH.TYPE_ARTIST))
            add(SearchResultFragment.getInstance(SEARCH.TYPE_MUSIC_LIST))
            add(SearchResultFragment.getInstance(SEARCH.TYPE_USER))
            add(SearchResultFragment.getInstance(SEARCH.TYPE_MV))
            add(SearchResultFragment.getInstance(SEARCH.TYPE_RADIO))

        }
    }

    fun getList(): ArrayList<Fragment> = mList

    override fun getItem(position: Int): Fragment {
        return mList[position]
    }

    override fun getCount(): Int {
        return mList.size
    }

}