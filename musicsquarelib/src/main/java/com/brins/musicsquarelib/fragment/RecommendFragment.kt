package com.brins.musicsquarelib.fragment

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.utils.createRadialGradientBitmap
import com.brins.bridgelib.musiclist.MusicListBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.musicsquarelib.R
import com.brins.musicsquarelib.activity.MusicListSquareActivity
import com.brins.musicsquarelib.adapter.MusicListGridAdapter
import com.brins.musicsquarelib.contract.MusicSquareContract
import com.brins.musicsquarelib.presenter.MusicListSquarePresenter
import com.brins.musicsquarelib.widget.PlayListCover
import com.brins.musicsquarelib.widget.PlayListPager
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.musiclist.MusicListsResult
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

class RecommendFragment : BaseMvpFragment<MusicListSquarePresenter>(),
    MusicSquareContract.View {

    private var mResult: MusicListsResult? = null
    private var mAdapter: MusicListGridAdapter? = null
    override fun getLayoutResID(): Int {
        return R.layout.fragment_recommend
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun init(savedInstanceState: Bundle?) {
        mAdapter = MusicListGridAdapter()
        val manager = GridLayoutManager(context, 3)
        manager.isSmoothScrollbarEnabled = true
        rv_music_list.layoutManager = manager
        rv_music_list.adapter = mAdapter
        rv_music_list.isNestedScrollingEnabled = false
        rv_music_list.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        changeBackground(pager.getCurentIndex())
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        if (mResult == null) {
            showLoading()
            launch({ mPresenter?.loadMusicList() }, {})
        } else {
            onMusicListLoad(mResult)
        }
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun needParent(): Boolean {
        return false
    }

    override fun onMusicListLoad(result: MusicListsResult?) {
        if (mResult == null) {
            mResult = result
            hideLoading()
        }
        result?.playlists?.let {

            for (i in 0..min(it.size, 2)) {
                val cover = PlayListCover(context!!)
                cover.apply {
                    setCover(it[i].coverImgUrl)
                    setText(it[i].name)
                    setCoverListener(object : PlayListCover.onCoverLoadListener {
                        override fun onCoverLoad(drawable: Drawable) {
                            it[i].cover = drawable
                        }
                    })
                    pager.addView(cover)
                    val lp: PlayListPager.PlayLayoutParams =
                        cover.layoutParams as PlayListPager.PlayLayoutParams
                    lp.from = i
                    lp.to = i
                    lp.index = i
                }
                pager.setPageChangeListener(object : PlayListPager.OnPageChangeListener {
                    override fun onPageChange(position: Int) {
                        changeBackground(position)
                    }

                })
                pager.setPageClickListener(object : PlayListPager.OnPlayListClickListener {
                    override fun onPlayListClick(position: Int) {
                        mResult?.playlists?.let { musiclist ->
                            val bundle = Bundle()
                            bundle.putString(KEY_ID, musiclist[position].id)
                            BridgeProviders.instance.getBridge(MusicListBridgeInterface::class.java)
                                .toMusicListActivity(bundle)
                        }

                    }

                })
            }
            if (it.size > 2) {
                val list = mutableListOf<BaseData>()
                for (i in 3 until it.size) {
                    list.add(it[i])
                }
                mAdapter?.setNewData(list)
            }
        }
    }

    private fun changeBackground(i: Int) {
        mResult?.playlists?.let {
            if (it[i].coverGradientDrawable == null) {
                val music = it[i]
                music.cover?.let {
                    Palette.from((it as BitmapDrawable).bitmap).generate {
                        it?.let {
                            when {
                                it.getDarkVibrantColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                    launch({
                                        music.coverGradientDrawable = withContext(Dispatchers.IO){
                                            createRadialGradientBitmap(context!!,
                                                it.getDarkVibrantColor(Color.TRANSPARENT),
                                                it.getVibrantColor(Color.TRANSPARENT))
                                        }
                                        (mActivity as MusicListSquareActivity).setBackground(music.coverGradientDrawable!!)

                                    },{})
                                }
                                it.getDarkMutedColor(Color.TRANSPARENT) != Color.TRANSPARENT -> {
                                    launch({
                                        music.coverGradientDrawable = withContext(Dispatchers.IO){
                                            createRadialGradientBitmap(
                                                context!!,
                                                it.getDarkMutedColor(Color.TRANSPARENT),
                                                it.getMutedColor(Color.TRANSPARENT))
                                        }
                                        (mActivity as MusicListSquareActivity).setBackground(music.coverGradientDrawable!!)

                                    },{})
                                }
                                else -> {
                                    launch({
                                        music.coverGradientDrawable = withContext(Dispatchers.IO){
                                            createRadialGradientBitmap(
                                                context!!,
                                                it.getLightMutedColor(Color.TRANSPARENT),
                                                it.getLightVibrantColor(Color.TRANSPARENT))
                                        }
                                        (mActivity as MusicListSquareActivity).setBackground(music.coverGradientDrawable!!)

                                    },{})
                                }
                            }
                        }
                    }
                }

            } else {
                (mActivity as MusicListSquareActivity).setBackground(it[i].coverGradientDrawable!!)
            }
        }

    }
}