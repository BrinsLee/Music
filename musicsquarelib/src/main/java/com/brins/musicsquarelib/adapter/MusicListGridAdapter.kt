package com.brins.musicsquarelib.adapter

import android.widget.ImageView
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_TOP_RECOMMEND
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.musicsquarelib.R
import com.brins.networklib.model.musiclist.MusicList
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder

class MusicListGridAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

    init {
        addItemType(ITEM_HOME_TOP_RECOMMEND, R.layout.music_square_tem_playlist)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        val imageView = helper.getView<ImageView>(R.id.iv_playlist)
        GlideHelper.setRoundImageResource(imageView, (item as MusicList).coverImgUrl, 10)
        helper.setText(R.id.tv_playlist_name, item.name)
    }
}