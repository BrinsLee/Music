package com.brins.mine.fragment

import android.os.Bundle
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
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.mine.R
import com.brins.mine.viewmodel.MineViewModel
import com.brins.mine.viewmodel.MineViewModel.Companion.TYPE_RECOMMEND_MUSIC_LIST
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.model.musiclist.MusicList
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_recommend_music_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RecommendMusicListFragment : BaseMvvmFragment<MineViewModel>() {

    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mRecommendMusicListDataObserver: Observer<MutableList<MusicList>>? = null

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MineViewModel.getInstance(mActivity!!.application)

    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_recommend_music_list
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mRecommendMusicListDataObserver = Observer {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter = RecommendMusicListAdapter()
            rv_recommend_list.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
            mAdapter?.apply {
                animationEnable = true
                setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
                setNewData(list)
            }
        }
        mViewModel?.getRecommendMutableMusicListData()
            ?.observe(this, mRecommendMusicListDataObserver!!)

        if (LoginCache.isLogin && LoginCache.userProfile != null) {
            ApiHelper.launch({
                mViewModel?.getRecommendMusicLists()
            }, {})
        } else {
            mViewModel?.createDefaultRecommend(TYPE_RECOMMEND_MUSIC_LIST)
        }
    }

    override fun needParent(): Boolean {
        return false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginSuccess(params: EventBusParams) {
        when (params.key) {
            EventBusKey.KEY_EVENT_LOGIN_SUCCESS -> {
                LoginCache.userProfile?.let {
                    ApiHelper.launch({
                        mViewModel?.getRecommendMusicLists()
                    }, {})
                }
            }

        }
    }

    class RecommendMusicListAdapter :
        BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_HOME_TOP_RECOMMEND, R.layout.mine_item_music)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            helper.setText(R.id.name, (item as MusicList).name)
            helper.setVisible(R.id.playCount, true)
            helper.setText(R.id.playCount, "${item.playCount}")
            if (item.coverImgUrl.isNotEmpty()) {
                GlideHelper.setRoundImageResource(
                    helper.getView(R.id.cover),
                    item.coverImgUrl, 10
                )
            }

            helper.getView<ConstraintLayout>(R.id.rootLayout).setOnClickListener {

                if (item.id.isNotEmpty()) {
                    val bundle = Bundle()
                    bundle.putString(KEY_ID, item.id)
                    ARouterUtils.go(RouterHub.MUSICLISTACTIVITY, bundle)
                } else {
                    ARouterUtils.go(RouterHub.LOGINSELECTACTIVITY)
                }
            }
        }
    }
}