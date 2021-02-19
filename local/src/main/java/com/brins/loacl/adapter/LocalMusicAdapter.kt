package com.brins.loacl.adapter

import android.widget.LinearLayout
import com.brins.baselib.module.*
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.loacl.R
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import javax.inject.Inject

/**
 * Created by lipeilin
 * on 2021/2/19
 */
class LocalMusicAdapter constructor(data: MutableList<BaseData>) :
    BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>(data) {

    init {
        addItemType(ITEM_BASE_MUSIC, R.layout.local_item_music)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        when (item.itemType) {
            ITEM_BASE_MUSIC -> {
                helper.setText(R.id.tv_music_num, "${helper.adapterPosition}")
                helper.setText(R.id.tv_music_name, (item as BaseMusic).name)
                helper.setText(R.id.tv_music_artist, item.artists?.get(0)?.name)
                helper.getView<LinearLayout>(R.id.music_list_ll)
                    .setOnClickListener {
                        val musicList = mutableListOf<BaseMusic>()
                        data.forEach {
                            musicList.add(it as BaseMusic)
                        }
                        EventBusManager.post(EventBusKey.KEY_EVENT_CHANGE_PLAYMODE, PlayMode.LOOP)
                        EventBusManager.post(
                            EventBusKey.KEY_EVENT_LOCAL_MUSIC,
                            musicList,
                            "${helper.adapterPosition}"
                        )
                    }
            }
        }
    }
}