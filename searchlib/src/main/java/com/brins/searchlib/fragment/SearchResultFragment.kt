package com.brins.searchlib.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.baselib.config.SEARCH
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.*
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.search.SearchResult
import com.brins.searchlib.R
import com.brins.searchlib.adapter.SearchAdapter
import com.brins.searchlib.viewmodel.SearchViewModel
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_search_result.*

/**
 * Created by lipeilin
 * on 2020/11/17
 */
class SearchResultFragment private constructor() :
    BaseMvvmFragment<SearchViewModel>(), UserProfile.OnFollowListener {
    val type: Int by lazy { mBundle?.getInt("SEARCH_TYPE") ?: 1 }

    //    private var mSearchResult: SearchResult<T>? = null
    private var mMusicDataObserver: Observer<SearchResult<BaseMusic>>? = null
    private var mAlbumDataObserver: Observer<SearchResult<BaseMusic.Album>>? = null
    private var mArtistDataObserver: Observer<SearchResult<BaseMusic.Artist>>? = null
    private var mMusicListDataObserver: Observer<SearchResult<MusicList>>? = null
    private var mUserDataObserver: Observer<SearchResult<UserProfile>>? = null
    private var mMusicVideoDataObserver: Observer<SearchResult<BaseMusicVideo>>? = null
    private var mRadioDataObserver: Observer<SearchResult<DjRadio>>? = null

    private var mAdapter: BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mNeedSearch = true
    var keyWords: String = ""
        set(value) {
            if (value == field) {
                return
            } else {
                field = value
                mNeedSearch = true
                if (isResumed) {
                    loadSearchResult()
                }
            }
        }

    companion object {
        fun getInstance(type: Int): Fragment {
            val fragment: Fragment
            when (type) {
                SEARCH.TYPE_MUSIC ->
                    fragment = SearchResultFragment()
                SEARCH.TYPE_ALBUM ->
                    fragment = SearchResultFragment()
                SEARCH.TYPE_ARTIST ->
                    fragment = SearchResultFragment()
                SEARCH.TYPE_MUSIC_LIST ->
                    fragment = SearchResultFragment()
                SEARCH.TYPE_USER ->
                    fragment = SearchResultFragment()
                SEARCH.TYPE_MV ->
                    fragment = SearchResultFragment()
                SEARCH.TYPE_RADIO ->
                    fragment = SearchResultFragment()
                else -> {
                    throw Exception("no type match")
                }

            }
            val bundle = Bundle()
            bundle.putInt("SEARCH_TYPE", type)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun useEventBus(): Boolean {
        return false
    }

    override fun needParent(): Boolean {
        return false
    }

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return SearchViewModel.getInstance(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_search_result
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mAdapter = SearchAdapter()
        rv_searchResult.adapter = mAdapter
        rv_searchResult.layoutManager = LinearLayoutManager(getMyContext())
        when (type) {
            SEARCH.TYPE_MUSIC -> {
                mMusicDataObserver = Observer {
                    hideLoading()
                    val list = mutableListOf<BaseData>()
                    list.addAll(it.dataBean?.data!!)
                    mAdapter?.setNewData(list)
                }
                mViewModel?.getMutableMusicLiveData()
                    ?.observe(this@SearchResultFragment, mMusicDataObserver!!)
            }
            SEARCH.TYPE_ALBUM -> {
                mAlbumDataObserver = Observer {
                    hideLoading()
                    val list = mutableListOf<BaseData>()
                    list.addAll(it.dataBean?.data!!)
                    mAdapter?.setNewData(list)
                }
                mViewModel?.getMutableAlbumLiveData()
                    ?.observe(this@SearchResultFragment, mAlbumDataObserver!!)
            }
            SEARCH.TYPE_ARTIST -> {
                mArtistDataObserver = Observer {
                    hideLoading()
                    val list = mutableListOf<BaseData>()
                    list.addAll(it.dataBean?.data!!)
                    mAdapter?.setNewData(list)
                }
                mViewModel?.getMutableArtistLiveData()
                    ?.observe(this@SearchResultFragment, mArtistDataObserver!!)
            }
            SEARCH.TYPE_MUSIC_LIST -> {
                mMusicListDataObserver = Observer {
                    hideLoading()
                    val list = mutableListOf<BaseData>()
                    list.addAll(it.dataBean?.data!!)
                    mAdapter?.setNewData(list)
                }
                mViewModel?.getMutableMusicListLiveData()
                    ?.observe(this@SearchResultFragment, mMusicListDataObserver!!)
            }
            SEARCH.TYPE_MV -> {
                mMusicVideoDataObserver = Observer {
                    hideLoading()
                    val list = mutableListOf<BaseData>()
                    list.addAll(it.dataBean?.data!!)
                    mAdapter?.setNewData(list)
                }
                mViewModel?.getMutableMusicVideoLiveData()
                    ?.observe(this@SearchResultFragment, mMusicVideoDataObserver!!)
            }
            SEARCH.TYPE_RADIO -> {
                mRadioDataObserver = Observer {
                    hideLoading()
                    val list = mutableListOf<BaseData>()
                    list.addAll(it.dataBean?.data!!)
                    mAdapter?.setNewData(list)
                }
                mViewModel?.getMutableRadioLiveData()
                    ?.observe(this@SearchResultFragment, mRadioDataObserver!!)
            }
            SEARCH.TYPE_USER -> {
                mUserDataObserver = Observer {
                    hideLoading()
                    val list = mutableListOf<BaseData>()
                    it.dataBean?.data?.forEach {
                        it.setFollowListener(this)
                    }
                    list.addAll(it.dataBean?.data!!)
                    mAdapter?.setNewData(list)
                }
                mViewModel?.getMutableUserLiveData()
                    ?.observe(this@SearchResultFragment, mUserDataObserver!!)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "type: $type,onResume")
        loadSearchResult()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "type: $type,onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "type: $type,onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "type: $type,onPause")
    }


    private fun loadSearchResult() {
        if (mNeedSearch && keyWords.isNotEmpty()) {
            Log.d("SearchViewModel", "${type}调用了loadSearchResult")
            launch({
                showLoading()
                mViewModel!!.loadSearchResult(keyWords, type)
                mNeedSearch = false
            }, {
                hideLoading()
                showError()
                mNeedSearch = true
            })
        } else {
            return
        }
    }

    override fun onFollow(user: UserProfile, pos: Int) {
        mViewModel?.followUser(user) {

            /*user.followed = !user.followed
            mAdapter?.notifyItemChanged(pos)*/
        }
    }
}