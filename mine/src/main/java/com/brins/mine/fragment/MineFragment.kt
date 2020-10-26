package com.brins.mine.fragment

import android.os.Bundle
import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.route.RouterHub.Companion.MINEFRAGMENT
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.mine.R
import com.brins.mine.adapter.BaseMineAdapter
import com.brins.mine.presenter.MinePresenter
import com.brins.networklib.login.LikeData
import com.brins.networklib.login.LoginData
import com.brins.networklib.model.daily.DailyData
import com.brins.networklib.model.music.RecentlyMusic
import com.brins.networklib.model.musiclist.MusicListData
import com.brins.networklib.model.title.SingleTitleData
import com.chad.library.adapter.base.model.BaseData
import kotlinx.android.synthetic.main.fragment_mine.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = MINEFRAGMENT)
class MineFragment : BaseMvpFragment<MinePresenter>(), NestedScrollView.OnScrollChangeListener {

    private var mAdapter: BaseMineAdapter? = null

    override fun getLayoutResID(): Int {
        return R.layout.fragment_mine
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        mAdapter = BaseMineAdapter(base_scroll_view, this)
    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {

    }


    override fun onLazyLoad() {
        super.onLazyLoad()
        mAdapter?.setOnLoadDataListener { p0, p1, p2 ->
            run {
                val list = mutableListOf<BaseData>()
                list.add(LoginData())
                list.add(LikeData())
                list.add(SingleTitleData().setTitle(getString(R.string.recently_play)))
                list.add(RecentlyMusic())
                list.add(SingleTitleData().setTitle(getString(R.string.my_list)))
                list.add(MusicListData())
                list.add(SingleTitleData().setTitle(getString(R.string.may_like)))
                list.add(DailyData())
                p2.onLoadDataSuccess(list)
            }
        }
        rv_mine.adapter = mAdapter
        rv_mine.layoutManager = LinearLayoutManager(getMyContext())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateSong(params: EventBusParams) {
        Log.d(TAG, "${params.key}")
        when (params.key) {
            EventBusKey.KEY_EVENT_UPDATE_MUSIC -> {
                mAdapter?.notifyItemChanged(3)
            }
        }
    }
}