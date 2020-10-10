package com.brins.mine.adapter

import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.brins.baselib.module.ITEM_MINE_UNLOGIN
import com.brins.mine.R
import com.brins.mine.viewholder.UnLoginViewHolder
import com.chad.library.adapter.base.BaseNestedScrollViewQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.model.BaseData

class BaseMineAdapter(
    view: NestedScrollView,
    onScrollChangeListener: NestedScrollView.OnScrollChangeListener? = null
) : BaseNestedScrollViewQuickAdapter<BaseData, BaseViewHolder<out BaseData>>(
    view,
    onScrollChangeListener
) {
    override fun onCreateViewHolderByType(p0: ViewGroup?, p1: Int): BaseViewHolder<out BaseData> {
        when (p1) {
            ITEM_MINE_UNLOGIN -> return UnLoginViewHolder(
                getItemView(
                    R.layout.mine_item_unlogin_view,
                    p0
                )
            )
            else -> throw IllegalStateException("invalid view type")
        }
    }


}