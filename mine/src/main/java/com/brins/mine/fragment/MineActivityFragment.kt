package com.brins.mine.fragment

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_MINE_EVENT_DATA
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.*
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.CircleImageView
import com.brins.baselib.widget.MultiImageView
import com.brins.mine.R
import com.brins.mine.viewmodel.MineViewModel
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.event.EventData
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_mine_activity.*


class MineActivityFragment : BaseMvvmFragment<MineViewModel>() {

    private var mEventDataObserver: Observer<MutableList<EventData>>? = null
    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MineViewModel.getInstance(mActivity!!.application)

    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_mine_activity
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mEventDataObserver = Observer {
            if (it.isEmpty()) {
                tv_empty_event.visibility = View.VISIBLE
                rv_event.visibility = View.GONE
            } else {
                tv_empty_event.visibility = View.GONE
                rv_event.visibility = View.VISIBLE
                val list = mutableListOf<BaseData>()
                list.addAll(it)
                mAdapter = MyEventAdapter()
                mAdapter!!.animationEnable = true
                mAdapter!!.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
                rv_event.adapter = mAdapter
                rv_event.layoutManager = LinearLayoutManager(context)
                mAdapter!!.setNewData(list)
            }

        }
        mViewModel?.getMutableEventData()?.observe(this, mEventDataObserver!!)
        launch({
            mViewModel?.getEventData(LoginCache.userAccount!!.id)
        }, {
            ToastUtils.show(R.string.network_error, Toast.LENGTH_SHORT)
        })

    }

    class MyEventAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_MINE_EVENT_DATA, R.layout.mine_item_event)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            when (item.itemType) {
                ITEM_MINE_EVENT_DATA -> {
                    GlideHelper.setImageResource(
                        helper.getView<CircleImageView>(R.id.iv_avatar),
                        (item as EventData).user?.avatarUrl
                    )
                    helper.setText(R.id.tv_name, createNickName(item))
                    helper.setText(R.id.tv_date, getDateToString(item.eventTime))
                    if (item.jsonData == null) {
                        val jsonData: EventData.EventJson = GsonUtils.fromJson<EventData.EventJson>(
                            item.json,
                            EventData.EventJson::class.java
                        )
                        helper.setText(
                            R.id.tv_content,
                            jsonData.msg
                        )
                        item.jsonData = jsonData
                    } else {
                        helper.setText(
                            R.id.tv_content,
                            item.jsonData?.msg
                        )
                    }

                    item.pics?.let {
                        val list = mutableListOf<String>()
                        it.forEach { image ->
                            list.add(image.squareUrl)
                        }
                        helper.getView<MultiImageView>(R.id.mi_event_images).setList(list)
                    }
                }
            }
        }

        private fun createNickName(item: EventData): SpannableStringBuilder? {
            var sub = when (item.type) {
                18 -> context.getString(R.string.share_song)
                19 -> context.getString(R.string.share_album)
                17, 28 -> context.getString(R.string.share_radio)
                22 -> context.getString(R.string.forward)
                39 -> context.getString(R.string.publish_video)
                35, 13 -> context.getString(R.string.share_music_list)
                24 -> context.getString(R.string.share_article)
                41, 21 -> context.getString(R.string.share_video)
                else -> ""
            }
            val strBuilder = SpanUtils().append(item.user?.nickname ?: "")
                .setForegroundColor(UIUtils.getColor(R.color.blue_4f7daf))
                .append(" ")
                .append(sub)
                .setForegroundColor(UIUtils.getColor(R.color.default_btn_text)).create()
            return strBuilder

        }

    }
}