package com.brins.radiolib.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_RADIO_RECOMMEND_DATA
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.utils.convertNumMillion
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.model.follow.FollowData
import com.brins.networklib.model.personal.PersonalizedMusicList
import com.brins.networklib.model.radio.Radio
import com.brins.networklib.model.radio.RadioData
import com.brins.radiolib.R
import com.brins.radiolib.viewmodel.RadioViewModel
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_radio_rec.*

class RadioRecFragment : BaseMvvmFragment<RadioViewModel>() {

    private var mRadioDataObserver: Observer<MutableList<Radio>>? = null
    private var mAdapter: RadioAdapter? = null

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return RadioViewModel.getInstance(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_radio_rec
    }

    override fun reLoad() {
    }

    override fun needParent(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mAdapter = RadioAdapter()
        rv_recommend_radio.adapter = mAdapter
        rv_recommend_radio.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rv_recommend_radio)
        mRadioDataObserver = Observer {
            val list = mutableListOf<BaseData>()
            list.addAll(it)
            mAdapter!!.setNewData(list)
        }
        mViewModel?.getRadioLiveData()?.observe(this, mRadioDataObserver!!)

        ApiHelper.launch({
            mViewModel?.getRecommendRadio()
        }, {})
    }


    class RadioAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_RADIO_RECOMMEND_DATA, R.layout.radio_item_recommend_radio)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            when (item.itemType) {

                ITEM_RADIO_RECOMMEND_DATA -> {
                    helper.setText(R.id.name, (item as Radio).name)
                    helper.setText(R.id.playCount, "${convertNumMillion(item.playCount)}")
                    GlideHelper.setRoundImageResource(helper.getView(R.id.cover), item.picUrl, 10)
                    helper.getView<View>(R.id.rootLayout).setOnClickListener {
                        val bundle = Bundle()
                        bundle.putInt(
                            "mPos", helper.adapterPosition
                        )
                        bundle.putString(
                            "mRadioId", item.id
                        )
                        ARouterUtils.go(RouterHub.RADIODETAILACTIVITY, bundle)
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}