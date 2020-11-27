package com.brins.mine.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.*
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.CircleImageView
import com.brins.mine.R
import com.brins.mine.viewmodel.MineViewModel
import com.brins.networklib.model.title.SingleTitleData2
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_mine_home.*

class MineHomeFragment : BaseMvvmFragment<MineViewModel>() {
    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mMusicListDataObserver: Observer<MutableList<MusicList>>? = null

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MineViewModel.getInstance(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_mine_home
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mMusicListDataObserver = Observer {
            val list = mutableListOf<BaseData>()
            list.add(SingleTitleData2().setTitle(getString(R.string.created_music_list)))
            list.addAll(it)
            list.add(SingleTitleData2().setTitle(getString(R.string.submited_album)))
            mAdapter = MyMusicListAdapter()
            mAdapter!!.animationEnable = true
            mAdapter!!.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
            rv_home.adapter = mAdapter
            rv_home.layoutManager =
                LinearLayoutManager(context)
            mAdapter!!.setNewData(list)
        }
        mViewModel?.getMutableMusicListData()?.observe(this, mMusicListDataObserver!!)

        if (mViewModel?.getMusicListData() != null) {

        }
    }

    class MyMusicListAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_MINE_SINGLE_TITLE, R.layout.mine_item_single_title)
            addItemType(ITEM_HOME_TOP_RECOMMEND, R.layout.mine_item_musiclist)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            when (helper.itemViewType) {
                ITEM_MINE_SINGLE_TITLE -> {
                    helper.setText(R.id.item_text_view, (item as SingleTitleData2).getTitle())
                }

                ITEM_HOME_TOP_RECOMMEND -> {
                    val imageView = helper.getView<CircleImageView>(R.id.ri_album_cover)
                    GlideHelper.setImageResource(imageView, (item as MusicList).coverImgUrl)
                    helper.setText(R.id.tv_album_name, item.name)
                    helper.setText(R.id.tv_album_artist, "上次更新：")
                }
            }
        }

    }
}