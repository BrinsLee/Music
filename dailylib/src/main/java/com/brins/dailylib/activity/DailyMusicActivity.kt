package com.brins.dailylib.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.BaseData
import com.brins.baselib.route.RouterHub.Companion.DAILYMUSICACTIVITY
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.setTranslucent
import com.brins.baselib.widget.CommonHeaderView
import com.brins.dailylib.R
import com.brins.dailylib.adapter.DailyMusicAdapter
import com.brins.dailylib.contract.DailyContract
import com.brins.dailylib.presenter.DailyMusicPresenter
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.daily.DailyMusicResult
import kotlinx.android.synthetic.main.activity_daily_music.*


@Route(path = DAILYMUSICACTIVITY)
class DailyMusicActivity : BaseMvpActivity<DailyMusicPresenter>(), DailyContract.View {

    private val mAdapter: DailyMusicAdapter = DailyMusicAdapter()

    override fun getLayoutResId(): Int {
        return R.layout.activity_daily_music
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.setContentInsetsAbsolute(0, 0)
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        musicRecycler.adapter = mAdapter
        val manager = LinearLayoutManager(this)
        manager.isSmoothScrollbarEnabled = true
        musicRecycler.setHasFixedSize(true)
        musicRecycler.isNestedScrollingEnabled = true
        musicRecycler.layoutManager = manager
        launch({
            showLoading()
            mPresenter?.getDailyMusic()
        }, {
            hideLoading()
            ToastUtils.show(R.string.network_error, Toast.LENGTH_SHORT)
        })
    }

    override fun setStatusBar() {
        setTranslucent(this)
    }

    override fun onDailyMusicLoad(result: DailyMusicResult) {
        hideLoading()
        result.recommend?.let {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter.setNewData(list)
        }
    }
}