package com.brins.musiclistlib.adapter

import android.widget.LinearLayout
import com.brins.baselib.module.*
import com.brins.baselib.utils.eventbus.EventBusKey.KEY_EVENT_PERSONALIZED_MUSIC
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.musiclistlib.R
import com.brins.networklib.model.album.AlbumListResult
import com.brins.baselib.module.MusicList
import com.brins.baselib.utils.eventbus.EventBusKey
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.module.LoadMoreModule
import com.chad.library.adapter.base2.viewholder.BaseViewHolder

class MusicListAdapter(data: MutableList<BaseData>) :
    BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>(data), LoadMoreModule {

    init {
        addItemType(ITEM_MUSIC_LIST_TRACK_MUSIC, R.layout.musiclist_item_music)
        addItemType(ITEM_ALBUM_LIST_MUSIC, R.layout.musiclist_item_music)
        addItemType(ITEM_HOME_SINGLE_TITLE, R.layout.musiclist_item_title)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {

        if (item.itemType == ITEM_HOME_SINGLE_TITLE) {
            helper.getView<LinearLayout>(R.id.ll_play_all).setOnClickListener {
                playAllMusic()
            }
        }
        if (item.itemType == ITEM_MUSIC_LIST_TRACK_MUSIC) {
            helper.setText(R.id.tv_music_num, "${helper.adapterPosition}")

            helper.setText(R.id.tv_music_name, (item as MusicList.Track).name)

            helper.setText(R.id.tv_music_artist, getArtists(item))

            helper.getView<LinearLayout>(R.id.music_list_ll)
                .setOnClickListener {
                    EventBusManager.post(EventBusKey.KEY_EVENT_CHANGE_PLAYMODE, PlayMode.LOOP)
                    playTrackMusic(helper.adapterPosition - 1)
                }
        }
        if (item.itemType == ITEM_ALBUM_LIST_MUSIC) {
            helper.setText(R.id.tv_music_num, "${helper.adapterPosition}")
            helper.setText(R.id.tv_music_name, (item as AlbumListResult.Song).name)
            helper.setText(R.id.tv_music_artist, getArtists(item))

            helper.getView<LinearLayout>(R.id.music_list_ll)
                .setOnClickListener {
                    EventBusManager.post(EventBusKey.KEY_EVENT_CHANGE_PLAYMODE, PlayMode.LOOP)
                    playAlbumMusic(helper.adapterPosition)
                }
        }

    }

    private fun playAllMusic() {
        val musicList = mutableListOf<BaseMusic>()
        data.forEach {
            if (it.itemType == ITEM_MUSIC_LIST_TRACK_MUSIC) {
                musicList.add(it as BaseMusic)
            }
        }
        EventBusManager.post(
            KEY_EVENT_PERSONALIZED_MUSIC,
            musicList,
            "0"
        )
    }

    private fun playTrackMusic(pos: Int) {
        val musicList = mutableListOf<BaseMusic>()
        data.forEach {
            if (it.itemType == ITEM_MUSIC_LIST_TRACK_MUSIC) {
                musicList.add(it as BaseMusic)
            }
        }
        EventBusManager.post(
            KEY_EVENT_PERSONALIZED_MUSIC,
            musicList,
            "$pos"
        )
    }

    private fun playAlbumMusic(pos: Int) {
        val musicList = mutableListOf<BaseMusic>()
        data.forEach {
            musicList.add(it as BaseMusic)
        }
        EventBusManager.post(
            KEY_EVENT_PERSONALIZED_MUSIC,
            musicList,
            "$pos"
        )
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