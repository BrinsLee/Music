package com.brins.video.adapter

import android.widget.ImageView
import com.brins.baselib.module.BaseData
import com.brins.networklib.model.musicvideo.Mv
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import com.brins.baselib.module.ITEM_VIDEO_MUSIC_VIDEO
import com.brins.baselib.utils.convertNumMillion
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.video.R

/**
 * Created by lipeilin
 * on 2021/1/29
 */
class VideoAdapter(data: MutableList<BaseData>) :
    BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>(data) {

    init {
        addItemType(ITEM_VIDEO_MUSIC_VIDEO, R.layout.video_item_video_list)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        when (item.itemType) {
            ITEM_VIDEO_MUSIC_VIDEO -> {
                helper.setText(R.id.tv_title, (item as Mv).dataBean.name)
                helper.setText(R.id.tv_author, item.dataBean.artistName)
                helper.setText(
                    R.id.tv_watch_count,
                    "${convertNumMillion(item.dataBean.playCount.toLong())}播放"
                )
                GlideHelper.setRoundImageResource(
                    helper.getView<ImageView>(R.id.video_player),
                    item.dataBean.cover,
                    12
                )
                GlideHelper.setCircleImageResource(
                    helper.getView<ImageView>(R.id.iv_avatar),
                    item.dataBean.cover,
                    R.drawable.base_icon_default_avatar
                )
            }
        }
    }


}