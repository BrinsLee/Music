package com.brins.musiclistlib.adapter

import android.widget.LinearLayout
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE
import com.brins.baselib.module.ITEM_MUSIC_LIST_TRACK_MUSIC
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusKey.KEY_EVENT_PERSONALIZED_MUSIC
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.musiclistlib.R
import com.brins.networklib.model.musiclist.MusicList
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder

class MusicListAdapter(data: MutableList<BaseData>) :
    BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>(data) {

    init {
        addItemType(ITEM_MUSIC_LIST_TRACK_MUSIC, R.layout.musiclist_item_music)
        addItemType(ITEM_HOME_SINGLE_TITLE, R.layout.musiclist_item_title)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        if (item.itemType == ITEM_MUSIC_LIST_TRACK_MUSIC) {
            helper.setText(R.id.tv_music_num, "${helper.adapterPosition}")

            helper.setText(R.id.tv_music_name, (item as MusicList.Track).name)

            helper.setText(R.id.tv_music_artist, getArtists(item))

            helper.getView<LinearLayout>(R.id.music_list_ll)
                .setOnClickListener {
                    val musicList = mutableListOf<BaseMusic>()
                    data.forEach {
                        if (it.itemType == ITEM_MUSIC_LIST_TRACK_MUSIC) {
                            musicList.add(it as BaseMusic)
                        }
                    }
                    EventBusManager.post(
                        KEY_EVENT_PERSONALIZED_MUSIC,
                        musicList,
                        "${helper.adapterPosition - 1}"
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