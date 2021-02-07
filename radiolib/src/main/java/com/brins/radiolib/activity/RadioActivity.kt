package com.brins.radiolib.activity

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.BaseData
import com.brins.baselib.route.RouterHub.Companion.RADIOACTIVITY
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.widget.CommonHeaderView
import com.brins.networklib.model.radio.RadioData
import com.brins.networklib.model.title.SingleTitleData2
import com.brins.radiolib.R
import com.brins.radiolib.adapter.BaseRadioAdapter
import com.brins.radiolib.presenter.RadioPresenter
import kotlinx.android.synthetic.main.activity_radio.*

@Route(path = RADIOACTIVITY)
class RadioActivity : BaseMvpActivity<RadioPresenter>() {

    private lateinit var mAdapter: BaseRadioAdapter

    override fun getLayoutResId(): Int {
        return R.layout.activity_radio
    }


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        val list = mutableListOf<BaseData>()
        list.add(SingleTitleData2().setTitle(getString(R.string.gauss_like)))
        list.add(RadioData())
        mAdapter = BaseRadioAdapter(list)
        rv_radio.adapter = mAdapter
        rv_radio.layoutManager = LinearLayoutManager(this)
        val decoration =
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        val drawable =
            GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(UIUtils.getColor(R.color.translucent_000000))
        drawable.setSize(10, 40)
        decoration.setDrawable(drawable)
        rv_radio.addItemDecoration(decoration)
    }
}