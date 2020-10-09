package com.brins.home.fragment

import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_TOP_MUSIC
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.home.R
import com.brins.home.viewmodel.HomeViewModel
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.model.music.TopMusic
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_top_song.*

class TopSongFragment : BaseMvvmFragment<HomeViewModel>() {

    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mMusicDataObserver: Observer<MutableList<TopMusic>>? = null

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return HomeViewModel.getInstance(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_top_song
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mAdapter = TopMusicAdapter()
        rv_top_song.adapter = mAdapter
        rv_top_song.layoutManager = GridLayoutManager(
            mActivity, 3,
            RecyclerView.HORIZONTAL,
            false
        )
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_top_song)
        mMusicDataObserver = Observer {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter!!.setNewData(list)
        }
        mViewModel?.getMutableTopMusicData()?.observe(this, mMusicDataObserver!!)
        ApiHelper.launch({
            mViewModel?.loadTopMusic()
        }, {})
    }

    class TopMusicAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_HOME_TOP_MUSIC, R.layout.home_item_newest_album)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            val name = getNameAndArtists(item as TopMusic)
            helper.setText(R.id.tv_name_artist, name)
            GlideHelper.setRoundImageResource(
                helper.getView<ImageView>(R.id.ri_album_cover),
                item.song!!.picUrl,
                10, R.drawable.base_icon_default_cover
            )
        }

        private fun getNameAndArtists(data: TopMusic): String {
            val builder = StringBuilder()
            builder.append(data.name)
            /*data.artists?.let {
                for (artist in it) {
                    builder.append("/")
                    builder.append(artist.name)
                }
            }*/
            return builder.toString()
        }

    }
}