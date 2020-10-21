package com.brins.mine.fragment

import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_TOP_RECOMMEND
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.mine.R
import com.brins.mine.viewmodel.MineViewModel
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.model.musiclist.MusicList
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_my_music_list.*

class MyMusicListFragment : BaseMvvmFragment<MineViewModel>() {

    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mMusicListDataObserver: Observer<MutableList<MusicList>>? = null


    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MineViewModel.getInstance(mActivity!!.application)

    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_my_music_list
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mMusicListDataObserver = Observer {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter = MyMusicListAdapter()
            mAdapter!!.animationEnable = true
            mAdapter!!.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
            rv_my_list.adapter = mAdapter
            rv_my_list.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            mAdapter!!.setNewData(list)
        }

        if (LoginCache.isLogin && LoginCache.userProfile != null) {
            mViewModel?.getMutableMusicListData()?.observe(this, mMusicListDataObserver!!)
            ApiHelper.launch({
                mViewModel?.getMyMusicLists(LoginCache.userProfile!!.userId.toString())
            }, {})
        }
    }

    override fun needParent(): Boolean {
        return false
    }

    class MyMusicListAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_HOME_TOP_RECOMMEND, R.layout.mine_item_music)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {

            helper.setText(R.id.name, (item as MusicList).name)
            GlideHelper.setRoundImageResource(
                helper.getView<ImageView>(R.id.cover),
                item.coverImgUrl, 10
            )

            helper.setVisible(R.id.iv_play, true)
            helper.getView<ConstraintLayout>(R.id.rootLayout).setOnClickListener {

                val bundle = Bundle()
                bundle.putString(KEY_ID, item.id)
                ARouterUtils.go(RouterHub.MUSICLISTACTIVITY, bundle)

            }
        }

    }
}