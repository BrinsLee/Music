package com.brins.home.viewholder

import android.view.View
import android.widget.TextView
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.home.R
import com.brins.networklib.model.title.SingleTitleData
import com.chad.library.adapter.base.BaseViewHolder

class SingleTitleViewHolder(itemView: View) : BaseViewHolder<SingleTitleData>(itemView) {

    private val textView: TextView = itemView.findViewById(R.id.item_text_view)

    override fun setData(data: SingleTitleData?) {
        super.setData(data)
        data?.apply {
            textView.text = this.getTitle()
        }
        textView.setPadding(dp2px(15f), dp2px(15f), 0, dp2px(15f))
    }
}