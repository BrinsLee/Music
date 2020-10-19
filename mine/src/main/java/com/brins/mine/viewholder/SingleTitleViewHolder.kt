package com.brins.mine.viewholder

import android.view.View
import android.widget.TextView
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE_MORE
import com.brins.baselib.utils.SizeUtils
import com.brins.mine.R
import com.brins.networklib.model.title.SingleTitleData
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lipeilin
 * on 2020/10/19
 */
class SingleTitleViewHolder(itemView: View) : BaseViewHolder<SingleTitleData>(itemView) {

    private val textView: TextView = itemView.findViewById(R.id.item_text_view)

    override fun setData(data: SingleTitleData?) {
        super.setData(data)
        data?.apply {
            textView.text = this.getTitle()
        }
        if (data?.itemType == ITEM_HOME_SINGLE_TITLE_MORE) {
            itemView.findViewById<TextView>(R.id.tv_view_more)
                .setOnClickListener(data.getListener())
        }
        textView.setPadding(SizeUtils.dp2px(15f), SizeUtils.dp2px(15f), 0, SizeUtils.dp2px(15f))
    }
}