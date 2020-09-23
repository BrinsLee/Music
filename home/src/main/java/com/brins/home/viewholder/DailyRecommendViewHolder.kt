package com.brins.home.viewholder

import android.view.View
import android.widget.TextView
import com.brins.baselib.utils.getCalendarDay
import com.brins.home.R
import com.brins.networklib.model.daily.DailyData
import com.chad.library.adapter.base.model.BaseData

class DailyRecommendViewHolder(itemView: View) : BaseEmptyViewHolder(itemView) {

    val tvDate = itemView.findViewById<TextView>(R.id.tv_date)

    override fun setData(data: BaseData?) {
        super.setData(data)
        if (data is DailyData) {
            tvDate.text = getCalendarDay()
        }
    }
}