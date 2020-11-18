package com.brins.searchlib.adapter

import com.brins.baselib.module.*
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.searchlib.R
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder

/**
 * Created by lipeilin
 * on 2020/11/17
 */
class SearchAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

    init {
        addItemType(0, R.layout.search_item_music)
        addItemType(ITEM_SEARCH_ALBUM, R.layout.search_item_album)
        addItemType(ITEM_SEARCH_ARTIST, R.layout.search_item_artist)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        when (item.itemType) {
            0 -> {
                helper.setText(R.id.tv_music_num, "${helper.adapterPosition + 1}")
                helper.setText(R.id.tv_music_name, (item as BaseMusic).name)
                helper.setText(R.id.tv_music_artist, getArtists(item))

            }
            ITEM_SEARCH_ALBUM -> {
                helper.setText(R.id.tv_album_name, (item as BaseMusic.Album).name)
                helper.setText(R.id.tv_album_artist, item.artist?.name)
                GlideHelper.setRoundImageResource(
                    helper.getView(R.id.ri_album_cover),
                    item.picUrl,
                    10,
                    R.drawable.base_icon_default_cover,
                    200,
                    200
                )
            }
            ITEM_SEARCH_ARTIST -> {
                helper.setText(R.id.tv_artist_name, (item as BaseMusic.Artist).name)
                GlideHelper.setCircleImageResource(
                    helper.getView(R.id.iv_avatar),
                    item.picUrl,
                    R.drawable.search_icon_default_avatar, 300, 300
                )
            }
        }
    }

    private fun getArtists(data: BaseMusic): String {
        val builder = StringBuilder()
        data.artists?.let {
            for (i in 0 until it.size) {
                builder.append(it[i].name)
                if (i != it.size - 1)
                    builder.append("，")
            }
        }
        return builder.toString()
    }
}