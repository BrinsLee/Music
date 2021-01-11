package com.brins.find.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.config.TYPE_EVENT
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.route.RouterHub
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.find.R
import com.brins.find.adapter.BaseFindAdapter
import com.brins.find.contract.FindContract
import com.brins.find.presenter.FindPresenter
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.event.EventsResult
import com.brins.networklib.model.follow.MyFollowsData
import com.brins.networklib.model.title.SingleTitleData2
import kotlinx.android.synthetic.main.fragment_find.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = RouterHub.FINDFRAGMENT)
class FindFragment : BaseMvpFragment<FindPresenter>(), FindContract.View {

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
        val list = mutableListOf<BaseData>()
        mAdapter = BaseFindAdapter(list)
    }

    override fun onLazyLoad() {
        super.onLazyLoad()
        mAdapter?.apply {
            addData(SingleTitleData2().setTitle(getString(R.string.my_follows)))
            addData(MyFollowsData())
        }
        rv_find.adapter = mAdapter
        rv_find.layoutManager = LinearLayoutManager(getMyContext())
        rv_find.setHasFixedSize(true)
        rv_find.setNestedScrollingEnabled(false)
        launch({
            mPresenter?.loadEvent()
        }, {})
    }

    override fun onUserEventLoad(result: EventsResult) {
        result.events?.let {
            val iterator = it.iterator()
            while (iterator.hasNext()) {
                val eventData = iterator.next()
                if (!TYPE_EVENT.contains(eventData.type)) {
                    iterator.remove()
                }
            }
/*            it.forEach { eventData ->
                if (TYPE_EVENT.contains(eventData.type)) {
                    arrayList.add(eventData)
                }
            }*/
            mAdapter?.addData(it)
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginSuccess(params: EventBusParams) {
        when (params.key) {
            EventBusKey.KEY_EVENT_LOGIN_SUCCESS -> {
                launch({
                    mPresenter?.loadEvent()
                }, {})
            }

        }
    }
}