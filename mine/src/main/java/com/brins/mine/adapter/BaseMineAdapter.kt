package com.brins.mine.adapter

import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.brins.baselib.module.*
import com.brins.mine.R
import com.brins.mine.viewholder.*
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
            ITEM_MINE_UNLOGIN -> return LoginViewHolder(
                getItemView(
                    R.layout.mine_item_login_view,
                    p0
                )
            )
            ITEM_MINE_LIKE -> return LikeViewHolder(getItemView(R.layout.mine_item_like_view, p0))
            ITEM_HOME_SINGLE_TITLE -> return SingleTitleViewHolder(
                getItemView(
                    R.layout.mine_item_single_title,
                    p0
                )
            )
            ITEM_MINE_RECENTLY_MUSIC -> return RecentlyMusicViewHolder(
                getItemView(
                    R.layout.mine_item_music,
                    p0
                )
            )
            ITEM_MINE_MUSIC_LIST -> return MusicListViewHolder(
                getItemView(R.layout.mine_item_music_list_view, p0)
            )
            ITEM_HOME_MUSIC_LIST -> return MusicListViewHolder(
                getItemView(R.layout.mine_item_recommend_music_list_view, p0)
            )
            else -> throw IllegalStateException("invalid view type")
        }
    }


}