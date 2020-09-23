package com.brins.musicdetail.activity


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.*
import com.brins.baselib.route.RouterHub.Companion.PLAYLISTACTIVITY
import com.brins.baselib.utils.hideStatusBar
import com.brins.musicdetail.R
import com.brins.playerlib.contract.PlayerContract
import com.brins.playerlib.model.PlayBackService
import com.brins.playerlib.presenter.PlayerPresenter
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_play_list.*
import kotlinx.android.synthetic.main.activity_play_list.iv_play_mode

@Route(path = PLAYLISTACTIVITY)
class PlayListActivity : BaseMvpActivity<PlayerPresenter>(), PlayerContract.View,
    View.OnClickListener {

    private var mPlayer: PlayBackService? = null
    private var mAdapter: PlayListAdapter? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_play_list
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mAdapter = PlayListAdapter()
        rv_playlist.layoutManager = LinearLayoutManager(this)
        rv_playlist.adapter = mAdapter
        mPresenter?.bindPlaybackService()
        view.setOnClickListener(this)
        iv_play_mode.setOnClickListener(this)
        iv_trash_can.setOnClickListener(this)

    }

    override fun onPlaybackServiceBound(service: PlayBackService) {
        mPlayer = service
        updatePlayMode(mPlayer?.getCurrentPlayMode() ?: PlayMode.LOOP)
        val list = mutableListOf<BaseData>()
        list.addAll(mPlayer!!.getPlayList().getSong())
        mAdapter?.setNewData(list)
    }

    override fun onPlaybackServiceUnbound() {
        mPlayer = null
    }

    override fun onSongUpdated(song: BaseMusic?) {
        TODO("Not yet implemented")
    }

    override fun onSongPlay() {
        TODO("Not yet implemented")
    }

    override fun onSongPause() {
        TODO("Not yet implemented")
    }

    override fun updatePlayMode(playMode: PlayMode) {
        when (playMode) {
            PlayMode.LOOP -> {
                iv_play_mode.setImageResource(R.drawable.base_icon_play_cycle_gery)
            }

            PlayMode.SINGLE -> {
                iv_play_mode.setImageResource(R.drawable.base_icon_play_single_grey)
            }
        }
    }

    override fun onMusicDelete() {
        val list = mutableListOf<BaseData>()
        list.addAll(mPlayer!!.getPlayList().getSong())
        mAdapter?.setNewData(list)
    }

    override fun setStatusBar() {
        hideStatusBar(this)
    }

    inner class PlayListAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        init {
            addItemType(ITEM_BANNER_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_HOME_PERSONALIZED_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_HOME_TOP_MUSIC, R.layout.music_detail_item_playlist)
            addItemType(ITEM_MUSIC_LIST_TRACK_MUSIC, R.layout.music_detail_item_playlist)

        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            helper.setText(R.id.tv_music_name, (item as BaseMusic).name)
            helper.setText(R.id.tv_link, getNameAndArtists(item))
            if (item == mPlayer?.getPlayingSong()) {
                helper.setVisible(R.id.iv_horn, isVisible = true)
            } else {
                helper.setVisible(R.id.iv_horn, isVisible = false)
            }
            helper.getView<ImageView>(R.id.iv_del).setOnClickListener {
                mPresenter?.delete(item)
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

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.view -> finish()
            R.id.iv_play_mode -> {
                changePlayMode()
            }
            R.id.iv_trash_can -> {
                mPresenter?.deleteAll()
            }

        }
    }

    private fun changePlayMode() {
        mPlayer?.let {
            when (it.getCurrentPlayMode()) {
                PlayMode.LOOP -> {
                    mPresenter?.changePlayMode(PlayMode.SINGLE)
                }

                PlayMode.SINGLE -> {
                    mPresenter?.changePlayMode(PlayMode.LOOP)
                }
                else -> {
                }
            }
        }
    }

}