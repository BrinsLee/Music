package com.brins.home.fragment

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.MusicStatus
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.home.R
import com.brins.home.adapter.PersonalizedMusicAdapter
import com.brins.home.viewmodel.HomeViewModel
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.personal.PersonalizedMusic
import com.chad.library.adapter.base2.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_personailzed_music.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class PersonalizedMusicFragment : BaseMvvmFragment<HomeViewModel>(),
    PersonalizedMusicAdapter.OnMusicClickListener {

    private lateinit var mAdapter: PersonalizedMusicAdapter
    private var mPersonalizedMusicDataObserver: Observer<MutableList<PersonalizedMusic>>? = null
    private var mLayoutManager: LinearLayoutManager? = null

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return HomeViewModel.getInstance(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_personailzed_music
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mPersonalizedMusicDataObserver = Observer {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter = PersonalizedMusicAdapter(list)
            mAdapter.setListener(this)
            mAdapter.animationEnable = true
            mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
            mAdapter.isAnimationFirstOnly = false
            mLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rv_personalized_music.layoutManager = mLayoutManager
            rv_personalized_music.adapter = mAdapter
        }
        mViewModel?.getMutablePersonalizedMusicData()
            ?.observe(this, mPersonalizedMusicDataObserver!!)
        launch({ mViewModel?.loadPersonalizedMusic() }, {})
    }

    override fun needParent(): Boolean {
        return false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateSong(params: EventBusParams) {
        Log.d(TAG, "${params.key}")
        when (params.key) {
            EventBusKey.KEY_EVENT_UPDATE_MUSIC -> {
                updatePlaystatus((params.`object` as BaseMusic))
            }
        }
    }

    override fun onMusicClick(music: BaseMusic, position: Int) {
        when ((music as PersonalizedMusic).playStatus) {

            MusicStatus.FIRST_PLAY -> {
                showLoading()
                val musicList = mutableListOf<BaseMusic>()
                mViewModel?.getMutablePersonalizedMusicData()?.value?.forEach {
                    musicList.add(it as BaseMusic)
                }
                EventBusManager.post(
                    EventBusKey.KEY_EVENT_PERSONALIZED_MUSIC, musicList,
                    "$position"
                )
            }

            MusicStatus.RESUME -> {
                EventBusManager.post(
                    EventBusKey.KEY_EVENT_PAUSE_MUSIC
                )
                music.playStatus = MusicStatus.PAUSE
                mLayoutManager?.findViewByPosition(position)?.findViewById<ImageView>(R.id.iv_play)
                    ?.setImageResource(R.drawable.home_ic_play)
            }

            MusicStatus.PAUSE -> {
                EventBusManager.post(
                    EventBusKey.KEY_EVENT_RESUME_MUSIC
                )
                music.playStatus = MusicStatus.RESUME
                mLayoutManager?.findViewByPosition(position)?.findViewById<ImageView>(R.id.iv_play)
                    ?.setImageResource(R.drawable.home_ic_pause)
            }
        }
    }

    private fun updatePlaystatus(music: BaseMusic) {
        mViewModel?.getMutablePersonalizedMusicData()?.value?.let {
            hideLoading()
            for (i in it.indices) {
                it[i].playStatus = MusicStatus.FIRST_PLAY
                if (it[i].id == music.id) {
                    it[i].playStatus = MusicStatus.RESUME
                }
                when (it[i].playStatus) {
                    MusicStatus.FIRST_PLAY, MusicStatus.PAUSE -> {
                        mLayoutManager?.findViewByPosition(i)?.findViewById<ImageView>(R.id.iv_play)
                            ?.setImageResource(R.drawable.home_ic_play)
                    }
                    MusicStatus.RESUME -> {
                        mLayoutManager?.findViewByPosition(i)?.findViewById<ImageView>(R.id.iv_play)
                            ?.setImageResource(R.drawable.home_ic_pause)
                    }
                }
            }
        }
    }
}