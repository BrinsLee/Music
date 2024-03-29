package com.brins.home.viewholder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.brins.baselib.utils.getCalendarDay
import com.brins.home.R
import com.brins.networklib.model.daily.DailyData
import com.chad.library.adapter.base.model.BaseData

class DailyRecommendViewHolder(itemView: View) : BaseEmptyViewHolder(itemView) {

    val tvDate = itemView.findViewById<TextView>(R.id.tv_date)
    val rlDate = itemView.findViewById<RelativeLayout>(R.id.daily_root)
    val rlHeart = itemView.findViewById<RelativeLayout>(R.id.list_root)
    val rlFm = itemView.findViewById<RelativeLayout>(R.id.fm_root)
    override fun setData(data: BaseData?) {
        super.setData(data)
        if (data is DailyData) {
            tvDate.text = getCalendarDay()
            rlDate.setOnClickListener {
                data.getListener()?.onDailyClick()
            }

            rlHeart.setOnClickListener {
                data.getListener()?.onHeartClick()
            }

            rlFm.setOnClickListener{
                data.getListener()?.onFmClick()
            }

        }


    }
}