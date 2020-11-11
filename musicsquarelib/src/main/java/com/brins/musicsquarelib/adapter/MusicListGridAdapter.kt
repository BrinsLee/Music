package com.brins.musicsquarelib.adapter

import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_TOP_RECOMMEND
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.musicsquarelib.R
import com.brins.baselib.module.MusicList
import com.brins.bridgelib.musiclist.MusicListBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
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
        helper.getView<RelativeLayout>(R.id.rl_playlist).setOnClickListener {
            val bundle = Bundle()
            bundle.putString(KEY_ID, item.id)
            BridgeProviders.instance.getBridge(MusicListBridgeInterface::class.java)
                .toMusicListActivity(bundle)
        }
    }
}