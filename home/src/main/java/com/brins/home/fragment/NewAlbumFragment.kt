package com.brins.home.fragment

import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_NEWEST_ALBUM
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.home.R
import com.brins.home.viewmodel.HomeViewModel
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.album.NewestAlbum
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_new_album.*

class NewAlbumFragment : BaseMvvmFragment<HomeViewModel>() {

    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mAlbumDataObserver: Observer<MutableList<NewestAlbum>>? = null
    override fun getLayoutResID(): Int {
        return R.layout.fragment_new_album
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mAdapter = NewestAlbumAdapter()
        rv_new_album.adapter = mAdapter
        rv_new_album.layoutManager = GridLayoutManager(
            mActivity, 3,
            RecyclerView.HORIZONTAL,
            false
        )
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_new_album)
//        rv_new_album.layoutManager = LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false)
        mAlbumDataObserver = Observer {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter!!.setNewData(list)
        }
        mViewModel?.getMutableAlbumData()?.observe(this, mAlbumDataObserver!!)
        launch({
            mViewModel?.loadNewestAlbum()
        }, {})
    }

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return HomeViewModel.getInstance(mActivity!!.application)
    }


    class NewestAlbumAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_HOME_NEWEST_ALBUM, R.layout.home_item_newest_album)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            val name = getNameAndArtists(item as NewestAlbum)
            helper.setText(R.id.tv_name_artist, name)
            GlideHelper.setRoundImageResource(
                helper.getView<ImageView>(R.id.ri_album_cover),
                item.picUrl,
                10
            )
        }

        private fun getNameAndArtists(data: NewestAlbum): String {
            val builder = StringBuilder()
            builder.append(data.name)
            data.artists?.let {
                for (artist in it) {
                    builder.append("/")
                    builder.append(artist.name)
                }
            }
            return builder.toString()
        }

    }
}