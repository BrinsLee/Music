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
import com.brins.mine.viewmodel.MineViewModel.Companion.TYPE_MUSIC_LIST
import com.brins.networklib.helper.ApiHelper
import com.brins.baselib.module.MusicList
import com.brins.bridgelib.login.LoginBridgeInterface
import com.brins.bridgelib.musiclist.MusicListBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_my_music_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        mViewModel?.getMutableMusicListData()?.observe(this, mMusicListDataObserver!!)
        if (LoginCache.isLogin && LoginCache.userProfile != null) {
            ApiHelper.launch({
                mViewModel?.getMyMusicLists(LoginCache.userProfile!!.userId.toString())
            }, {})
        } else {
            mViewModel?.createDefaultRecommend(TYPE_MUSIC_LIST)
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
                        mViewModel?.getMyMusicLists(LoginCache.userProfile!!.userId.toString())
                    }, {})
                }
            }
            EventBusKey.KEY_EVENT_CLICK_MY_LIKE -> {
                (mAdapter as MyMusicListAdapter).clickMyMusicList(mAdapter!!.data.get(0) as MusicList)
            }

        }
    }

    class MyMusicListAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_HOME_TOP_RECOMMEND, R.layout.mine_item_music)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {

            helper.setText(R.id.name, (item as MusicList).name)
            if (item.coverImgUrl.isNotEmpty()) {
                GlideHelper.setRoundImageResource(
                    helper.getView(R.id.cover),
                    item.coverImgUrl, 10
                )
            }

            helper.setVisible(R.id.iv_play, true)
            helper.getView<ConstraintLayout>(R.id.rootLayout).setOnClickListener {
                clickMyMusicList(item)
            }
        }

        fun clickMyMusicList(item: MusicList) {
            if (item.id.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putString(KEY_ID, item.id)
                BridgeProviders.instance.getBridge(MusicListBridgeInterface::class.java)
                    .toMusicListActivity(bundle)
            } else {
                BridgeProviders.instance.getBridge(LoginBridgeInterface::class.java)
                    .toLoginSelectActivity()
            }
        }

    }
}