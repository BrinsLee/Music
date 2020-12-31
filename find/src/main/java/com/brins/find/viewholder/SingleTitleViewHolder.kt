package com.brins.find.viewholder

import android.view.View
import android.widget.TextView
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.find.R
import com.brins.networklib.model.title.SingleTitleData
import com.chad.library.adapter.base.BaseViewHolder

class SingleTitleViewHolder(itemView: View) : BaseViewHolder<SingleTitleData>(itemView) {

    private val textView: TextView = itemView.findViewById(R.id.item_text_view)

    override fun setData(data: SingleTitleData?) {
        super.setData(data)

    }
}