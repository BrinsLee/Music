package com.brins.musicdetail.widget

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brins.baselib.module.*
import com.brins.baselib.utils.SpanUtils
import com.brins.baselib.utils.UIUtils
import com.brins.musicdetail.R
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.util.XPopupUtils

/**
 * Created by lipeilin
 * on 2021/2/2
 */
class PlayListPopup(
    context: Context,
    var mSongList: MutableList<BaseData> = mutableListOf(),
    var mCurrent: String = ""
) : BottomPopupView(context), View.OnClickListener {


    private var mRecyclerView: RecyclerView? = null
    private var mIvPlayMode: ImageView? = null
    private var mIvTrashCan: ImageView? = null
    private var mAdapter: PlayListAdapter? = null
    private var mTvPlayMode: TextView? = null
    private var mClickListener: OnClickListener? = null
    private var mCurrentPos: Int = -1

    interface OnClickListener {
        fun onPlayModeClick()

        fun onTrashCanClick()

        fun onDeleteItem(item: BaseMusic, adapterPosition: Int)

        fun onItemClick(item: BaseMusic, adapterPosition: Int)
    }

    fun setClickListener(clickListener: OnClickListener) {
        this.mClickListener = clickListener
    }

    override fun getImplLayoutId(): Int {
        return R.layout.music_detail_play_list_popup
    }

    override fun onCreate() {
        super.onCreate()
        mAdapter = PlayListAdapter()
        mRecyclerView = findViewById(R.id.rv_playlist)
        mIvPlayMode = findViewById(R.id.iv_play_mode)
        mIvTrashCan = findViewById(R.id.iv_trash_can)
        mTvPlayMode = findViewById(R.id.tv_play_mode)
        mRecyclerView?.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        mAdapter?.setNewData(mSongList)
        initListener()
    }

    fun updateData(songList: MutableList<BaseData>) {
        this.mSongList.clear()
        mSongList.addAll(songList)
        mAdapter?.notifyDataSetChanged()
    }

    fun updateData(item: BaseMusic, position: Int) {
        mCurrent = item.id
        if (mCurrentPos >= 0) {
            mAdapter?.notifyItemChanged(mCurrentPos)
        }
        mCurrentPos = position
        mAdapter?.notifyItemChanged(position)
    }

    fun deleteItem(item: BaseMusic, position: Int) {
        mSongList.remove(item)
        mAdapter?.notifyItemRemoved(position)
    }

    fun deleteItem(position: Int) {
        mSongList.removeAt(position)
        mAdapter?.notifyItemRemoved(position)
    }

    private fun initListener() {
        mIvPlayMode?.setOnClickListener(this)
        mIvTrashCan?.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_play_mode -> {
                mClickListener?.onPlayModeClick()
            }
            R.id.iv_trash_can -> {
                mClickListener?.onTrashCanClick()
                dismiss()
            }
        }
    }

    inner class PlayListAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(0, R.layout.music_detail_item_playlist)
            addItemType(ITEM_BANNER_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_HOME_PERSONALIZED_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_HOME_TOP_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_ALBUM_LIST_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_MUSIC_LIST_TRACK_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_DAILY_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_HOME_MUSIC_LIST_INTELLIGENCE, R.layout.music_detail_item_playlist)

        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            helper.setText(R.id.tv_music_name, (item as BaseMusic).name)
            helper.setText(R.id.tv_link, getNameAndArtists(item))
            if (item.id == mCurrent) {
                helper.setVisible(R.id.iv_horn, isVisible = true)
                mCurrentPos = helper.adapterPosition
            } else {
                helper.setVisible(R.id.iv_horn, isVisible = false)
            }
            helper.getView<ImageView>(R.id.iv_del).setOnClickListener {
                if (item.id == mCurrent){
                    return@setOnClickListener
                }
                mClickListener?.onDeleteItem(item, helper.adapterPosition)
            }
            helper.getView<RelativeLayout>(R.id.rl_song_play).setOnClickListener {
                mClickListener?.onItemClick(item, helper.adapterPosition)
            }

        }


        private fun getNameAndArtists(data: BaseMusic): String {
            val builder = StringBuilder()
            data.artists?.let {
                for (artist in it) {
                    builder.append(artist.name)
                    builder.append(" ")
                }
            }
            if (data.artists == null) {
                data.song?.artists?.let {
                    for (artist in it) {
                        builder.append(artist.name)
                        builder.append(" ")
                    }
                }
            }
            return builder.toString()
        }

    }

    override fun getMaxHeight(): Int {
        return (XPopupUtils.getWindowHeight(context) * .5f).toInt()
    }

    fun setPlayModel(currentPlayMode: PlayMode?) {
        updatePlayModel(currentPlayMode ?: PlayMode.LOOP)
    }

    private fun updatePlayModel(playMode: PlayMode) {
        when (playMode) {
            PlayMode.LOOP -> {
                mIvPlayMode?.setImageResource(R.drawable.base_icon_play_cycle_gery)
                mTvPlayMode?.text = "列表循环"
            }
            PlayMode.HEART -> {
                val strBuilder = SpanUtils().append(UIUtils.getString(R.string.current_mode))
                    .setForegroundColor(UIUtils.getColor(R.color.gery_333333))
                    .append(UIUtils.getString(R.string.heart_mode))
                    .setForegroundColor(UIUtils.getColor(R.color.red_ED5050)).create()
                mTvPlayMode?.text = strBuilder
                mIvPlayMode?.setImageResource(R.drawable.base_icon_play_cycle_gery)

            }
            PlayMode.SINGLE -> {
                mIvPlayMode?.setImageResource(R.drawable.base_icon_play_single_grey)
                mTvPlayMode?.text = "单曲循环"
            }
        }
    }
}