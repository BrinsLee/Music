package com.brins.find.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.route.RouterHub
import com.brins.find.R
import com.brins.find.adapter.BaseFindAdapter
import com.brins.find.presenter.FindPresenter
import com.brins.networklib.model.follow.FollowData
import com.brins.networklib.model.follow.MyFollowsData
import com.brins.networklib.model.title.SingleTitleData
import com.chad.library.adapter.base.OnLoadDataCompleteCallback
import com.chad.library.adapter.base.OnLoadDataListener
import com.chad.library.adapter.base.model.BaseData
import kotlinx.android.synthetic.main.fragment_find.*

@Route(path = RouterHub.FINDFRAGMENT)
class FindFragment : BaseMvpFragment<FindPresenter>() {

    private var mAdapter: BaseFindAdapter? = null

    companion object {
        fun getInstance(): FindFragment {
            return FindFragment()
        }
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_find
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        mAdapter = BaseFindAdapter(null)

    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        mAdapter?.setOnLoadDataListener { p0, p1, listener ->
            run {
                val list = mutableListOf<BaseData>()
                list.add(SingleTitleData().setTitle(getString(R.string.my_follows)))
                list.add(MyFollowsData())
                listener.onLoadDataSuccess(list)
            }
        }

        rv_find.adapter = mAdapter
        rv_find.layoutManager = LinearLayoutManager(getMyContext())
        rv_find.setHasFixedSize(true)
        rv_find.setNestedScrollingEnabled(false)
    }
}