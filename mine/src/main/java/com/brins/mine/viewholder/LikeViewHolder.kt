package com.brins.mine.viewholder

import android.view.View
import com.brins.baselib.utils.eventbus.EventBusKey.KEY_EVENT_CLICK_MY_LIKE
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.mine.R
import com.brins.mine.widget.IconTextVerticalView
import com.brins.networklib.login.LikeData
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lipeilin
 * on 2020/10/15
 */
class LikeViewHolder(itemView: View) : BaseViewHolder<LikeData>(itemView) {

    val itLike: IconTextVerticalView = itemView.findViewById(R.id.it_like)

    override fun setData(data: LikeData?, dataPosition: Int) {
        super.setData(data, dataPosition)
        itLike.setOnClickListener {
            EventBusManager.post(KEY_EVENT_CLICK_MY_LIKE)
        }
    }
}