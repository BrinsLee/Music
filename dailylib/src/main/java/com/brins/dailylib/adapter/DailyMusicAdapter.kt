package com.brins.dailylib.adapter

import android.widget.LinearLayout
import com.brins.baselib.module.*
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.dailylib.R
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import javax.inject.Inject

/**
 * Created by lipeilin
 * on 2020/11/2
 */
class DailyMusicAdapter @Inject constructor() : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

    init {
        addItemType(ITEM_DAILY_MUSIC, R.layout.daily_item_music)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        if (item.itemType == ITEM_DAILY_MUSIC) {
            helper.setText(R.id.tv_music_num, "${helper.adapterPosition}")
            helper.setText(R.id.tv_music_name, (item as BaseMusic).name)
            helper.setText(R.id.tv_music_artist, getArtists(item))

            helper.getView<LinearLayout>(R.id.music_list_ll)
                .setOnClickListener {
                    val musicList = mutableListOf<BaseMusic>()
                    data.forEach {
                        musicList.add(it as BaseMusic)
                    }
                    EventBusManager.post(
                        EventBusKey.KEY_EVENT_PERSONALIZED_MUSIC,
                        musicList,
                        "${helper.adapterPosition}"
                    )
                }
        }
    }

    private fun getArtists(data: BaseMusic): String {
        val builder = StringBuilder()
        data.song?.artists?.let {
            for (i in 0 until it.size) {
                builder.append(it[i].name)
                if (i != it.size - 1)
                    builder.append("，")
            }
        }
        return builder.toString()
    }
}