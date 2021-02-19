package com.brins.loacl.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.loacl.R
import com.brins.loacl.adapter.LocalMusicAdapter
import com.brins.loacl.viewmodel.LocalMusicViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_local_music.*
import javax.inject.Inject

/**
 * Created by lipeilin
 * on 2021/2/18
 */
@AndroidEntryPoint
class LocalMusicFragment private constructor() : BaseMvvmFragment<LocalMusicViewModel>() {

    val type: Int by lazy { mBundle?.getInt("MUSIC_TYPE") ?: TYPE_MUSIC }
    private var mSingleMusicObserver: Observer<MutableList<BaseMusic>>? = null
    lateinit var mAdapter: LocalMusicAdapter

    companion object {
        const val TYPE_MUSIC = 1
        const val TYPE_ARTIST = 2
        const val TYPE_ALBUM = 3

        fun getInstance(type: Int): Fragment {
            val fragment: Fragment = LocalMusicFragment()
            val bundle = Bundle()
            bundle.putInt("MUSIC_TYPE", type)
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
        return LocalMusicViewModel.getInstance(mActivity!!.application)

    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_local_music
    }

    override fun reLoad() {

    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mAdapter = LocalMusicAdapter(mutableListOf())
        rv_local_music.adapter = mAdapter
        rv_local_music.layoutManager = LinearLayoutManager(context)
        when (type) {
            TYPE_MUSIC -> {
                mSingleMusicObserver = Observer {
                    val list = mutableListOf<BaseData>()
                    list.addAll(it)
                    mAdapter.setNewData(list)
                }
                mViewModel?.getSingleMusicLiveData()?.observe(this, mSingleMusicObserver!!)
                mViewModel?.loadLocalMusic(LoaderManager.getInstance(this))
            }
        }
    }

}