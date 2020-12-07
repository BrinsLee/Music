package com.brins.find.fragment


import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_FIND_FOLLOW
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.find.R
import com.brins.find.viewmodel.FindViewModel
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.follow.FollowData
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_find_follows.*

class FindFollowsFragment : BaseMvvmFragment<FindViewModel>() {

    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mFollowDataObserver: Observer<MutableList<FollowData>>? = null
    override fun getViewModel(): BaseViewModel<out IModel>? {
        return FindViewModel.getInstance(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_find_follows
    }

    override fun reLoad() {
    }

    override fun needParent(): Boolean {
        return false
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mAdapter = FindFollowsAdapter()
        rv_find_follow.adapter = mAdapter
        rv_find_follow.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_find_follow)
        mFollowDataObserver = Observer {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter!!.setNewData(list)
        }
        mViewModel?.getFollowLiveData()?.observe(this, mFollowDataObserver!!)

        launch({
            mViewModel?.getMyFollows(LoginCache.userAccount?.id!!)
        }, {})
    }

    class FindFollowsAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_FIND_FOLLOW, R.layout.find_item_follow)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            when (item.itemType) {
                ITEM_FIND_FOLLOW -> {
                    GlideHelper.setImageResource(
                        helper.getView(R.id.iv_avatar),
                        (item as FollowData).avatarUrl, R.drawable.base_icon_default_cover
                    )
                    helper.setText(R.id.tv_nickname, item.nickname)
                }
            }
        }

    }
}