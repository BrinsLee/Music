package com.brins.searchlib.adapter

import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.module.*
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.utils.convertNumMillion
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.baselib.utils.formatDuration
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.AlphaConstraintLayout
import com.brins.baselib.widget.AlphaLinearLayout
import com.brins.bridgelib.musiclist.MusicListBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.searchlib.R
import com.brins.searchlib.widget.FollowTextView
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
        addItemType(ITEM_SEARCH_MUSIC_LIST, R.layout.search_item_album)
        addItemType(ITEM_SEARCH_MUSIC_VIDEO, R.layout.search_item_music_video)
        addItemType(ITEM_SEARCH_DJRADIO, R.layout.search_item_album)
        addItemType(ITEM_SEARCH_USER, R.layout.search_item_user)

    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        when (item.itemType) {
            0 -> {
                helper.setText(R.id.tv_music_num, "${helper.adapterPosition + 1}")
                helper.setText(R.id.tv_music_name, (item as BaseMusic).name)
                helper.setText(R.id.tv_music_artist, getArtists(item))
                helper.getView<AlphaLinearLayout>(R.id.music_list_ll).setOnClickListener {
                    item.let {
                        EventBusManager.post(
                            EventBusKey.KEY_EVENT_BANNER_MUSIC, it
                        )
                    }
                }

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
                helper.getView<ConstraintLayout>(R.id.album_list_cl).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(KEY_ID, item.id)
                    ARouterUtils.go(RouterHub.ALBUMLISTACTIVITY, bundle)
                }
            }
            ITEM_SEARCH_ARTIST -> {
                helper.setText(R.id.tv_artist_name, (item as BaseMusic.Artist).name)
                GlideHelper.setCircleImageResource(
                    helper.getView(R.id.iv_avatar),
                    item.picUrl,
                    R.drawable.search_icon_default_avatar, 300, 300
                )
            }
            ITEM_SEARCH_MUSIC_LIST -> {
                helper.setText(R.id.tv_album_name, (item as MusicList).name)
                helper.setVisible(R.id.vinyl_record, false)
                helper.setText(
                    R.id.tv_album_artist,
                    "${item.trackCount}首 by ${item.creator?.nickname}，播放${convertNumMillion(item.playCount)}次"
                )
                helper.getView<TextView>(R.id.tv_album_artist)
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                GlideHelper.setRoundImageResource(
                    helper.getView(R.id.ri_album_cover),
                    item.coverImgUrl,
                    10,
                    R.drawable.base_icon_default_cover,
                    200,
                    200
                )
                helper.getView<AlphaConstraintLayout>(R.id.album_list_cl).setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(KEY_ID, item.id)
                    BridgeProviders.instance.getBridge(MusicListBridgeInterface::class.java)
                        .toMusicListActivity(bundle)
                }
            }
            ITEM_SEARCH_MUSIC_VIDEO -> {
                helper.setText(R.id.tv_music_video_name, (item as BaseMusicVideo).name)
                helper.setText(
                    R.id.tv_music_video_artist,
                    "${formatDuration(item.duration)} by ${getArtists(item.artists!!)}"
                )
                helper.getView<TextView>(R.id.tv_music_video_artist)
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                helper.setText(R.id.tv_play_count, convertNumMillion(item.playCount.toLong()))
                GlideHelper.setRoundImageResource(
                    helper.getView(R.id.ri_music_video_cover),
                    item.cover,
                    10,
                    R.drawable.base_icon_default_cover,
                    200,
                    200
                )
            }
            ITEM_SEARCH_DJRADIO -> {
                helper.setText(R.id.tv_album_name, (item as DjRadio).name)
                helper.setVisible(R.id.vinyl_record, false)
                helper.setText(R.id.tv_album_artist, item.dj?.nickname)
                helper.getView<TextView>(R.id.tv_album_artist)
                    .setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                GlideHelper.setRoundImageResource(
                    helper.getView(R.id.ri_album_cover),
                    item.picUrl,
                    10,
                    R.drawable.base_icon_default_cover,
                    200,
                    200
                )
            }

            ITEM_SEARCH_USER -> {
                if ((item as UserProfile).gender == 1) {
                    helper.getView<ImageView>(R.id.iv_gender)
                        .setImageResource(R.drawable.search_icon_male)
                } else {
                    helper.getView<ImageView>(R.id.iv_gender)
                        .setImageResource(R.drawable.search_icon_female)
                }
                helper.setText(R.id.tv_user_name, item.nickname)
                GlideHelper.setCircleImageResource(
                    helper.getView(R.id.iv_avatar),
                    item.avatarUrl,
                    R.drawable.search_icon_default_avatar, 300, 300
                )
                val followTextView = helper.getView<FollowTextView>(R.id.ft_follow)
                if (item.followed) {
                    followTextView.followSuccess()
                } else {
                    followTextView.setOnClickListener {
                        followTextView.startLoading()
                        item.onFollowListener?.get()?.onFollow(item, helper.adapterPosition)
                    }
                }
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

    private fun getArtists(data: ArrayList<BaseMusicVideo.Artist>): String {
        val builder = StringBuilder()
        data.let {
            for (i in 0 until it.size) {
                builder.append(it[i].name)
                if (i != it.size - 1)
                    builder.append("/")
            }
        }
        return builder.toString()
    }
}