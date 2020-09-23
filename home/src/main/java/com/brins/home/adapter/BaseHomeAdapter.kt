package com.brins.home.adapter

import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentManager
import com.brins.baselib.module.*
import com.brins.home.R
import com.brins.home.viewholder.*
import com.chad.library.adapter.base.BaseNestedScrollViewQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.model.BaseData

class BaseHomeAdapter(
    view: NestedScrollView,
    onScrollChangeListener: NestedScrollView.OnScrollChangeListener? = null,
    var manager: FragmentManager
) : BaseNestedScrollViewQuickAdapter<BaseData, BaseViewHolder<out BaseData>>(
    view,
    onScrollChangeListener
) {

    override fun onCreateViewHolderByType(p0: ViewGroup?, p1: Int): BaseViewHolder<out BaseData> {
        when (p1) {
            ITEM_HOME_BANNER -> return BannerViewHolder(getItemView(R.layout.home_item_banner, p0))

            ITEM_HOME_SINGLE_TITLE -> return SingleTitleViewHolder(
                getItemView(
                    R.layout.home_item_single_title,
                    p0
                )
            )
            ITEM_HOME_SINGLE_TITLE_MORE -> return SingleTitleViewHolder(
                getItemView(
                    R.layout.home_item_single_title_more,
                    p0
                )
            )
            ITEM_HOME_PERSONALIZED -> return PersonalizedViewHolder(
                getItemView(
                    R.layout.home_item_peronalized,
                    p0
                )
            )
            ITEM_HOME_TOP_RECOMMEND -> return TopRecommendViewHolder(
                getItemView(
                    R.layout.home_item_peronalized,
                    p0
                )
            )

            ITEM_HOME_NEWEST -> return NewestAlbumViewHolder(
                getItemView(
                    R.layout.home_widget_item_album,
                    p0
                ), manager
            )
            ITEM_HOME_MUSIC_LIST -> return DailyRecommendViewHolder(
                getItemView(R.layout.home_item_daily_recommend, p0)
            )
            else -> throw IllegalStateException("invalid view type")
        }
    }

}