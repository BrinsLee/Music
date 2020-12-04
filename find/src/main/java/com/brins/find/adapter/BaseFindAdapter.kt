package com.brins.find.adapter

import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.brins.baselib.module.ITEM_FIND_FOLLOW
import com.brins.baselib.module.ITEM_FIND_SINGLE_TITLE
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE
import com.brins.find.R
import com.brins.find.viewholder.FindFollowsViewHolder
import com.brins.find.viewholder.SingleTitleViewHolder
import com.chad.library.adapter.base.BaseNestedScrollViewQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.model.BaseData
import java.lang.Exception

/**
 * Created by lipeilin
 * on 2020/12/2
 */
class BaseFindAdapter(
    view: NestedScrollView?,
    onScrollChangeListener: NestedScrollView.OnScrollChangeListener? = null
) : BaseNestedScrollViewQuickAdapter<BaseData, BaseViewHolder<out BaseData>>(
    view,
    onScrollChangeListener
) {
    override fun onCreateViewHolderByType(p0: ViewGroup?, p1: Int): BaseViewHolder<out BaseData> {

        when (p1) {
            ITEM_FIND_FOLLOW -> {
                return FindFollowsViewHolder(getItemView(R.layout.find_item_my_follow, p0))
            }
            ITEM_FIND_SINGLE_TITLE -> {
                return SingleTitleViewHolder(getItemView(R.layout.find_item_single_title, p0))
            }
            else -> {
                throw Exception("no item match")
            }
        }
    }

}