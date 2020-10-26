package com.brins.musicdetail.fragment

import android.os.Bundle
import android.os.Message
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.utils.WeakHandler
import com.brins.musicdetail.R
import com.brins.musicdetail.activity.MusicDetailActivity
import com.brins.musicdetail.contract.MusicDetailContract
import com.brins.musicdetail.presenter.MusicDetailPresenter
import com.brins.musicdetail.widget.LrcView
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.music.MusicLrc
import com.brins.playerlib.model.PlayBackService
import kotlinx.android.synthetic.main.fragment_music_lrc.*

class MusicLrcFragment : BaseMvpFragment<MusicDetailPresenter>(), MusicDetailContract.View,
    WeakHandler.IHandler {

    private val mHandler: WeakHandler by lazy { WeakHandler(this) }
    private val MESSAGE_UPDATE_PROGRESS = 0X10
    private val UPDATE_PROGRESS_INTERVAL: Long = 300

    private var mPlayer: PlayBackService? = null

    override fun getLayoutResID(): Int {
        return R.layout.fragment_music_lrc
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun init(savedInstanceState: Bundle?) {
        if (mActivity is MusicDetailActivity) {
            mPlayer = (mActivity as MusicDetailActivity).getPlayBackService()
            initMusicLrc()
        }
    }

    fun initMusicLrc() {
        val music = mPlayer?.getPlayingSong()
        music?.let {
            launch({
                mPresenter?.loadMusicLrc(it.id)
            }, {})
            lrc_view.setDraggable(true, object : LrcView.OnPlayClickListener {
                override fun onPlayClick(view: LrcView?, time: Long): Boolean {
                    mPlayer!!.seekTo(time.toInt())
                    if (!mPlayer!!.isPlaying()) {
                        mPlayer!!.play()
                        mHandler.sendEmptyMessage(MESSAGE_UPDATE_PROGRESS)
                    }
                    return true
                }

            })
        }
    }

    override fun onLikeMusic(isSuccess: Boolean, id: String) {

    }

    override fun onDislikeMusic(isSuccess: Boolean, id: String) {
        TODO("Not yet implemented")
    }

    override fun onMusicDetialLoad() {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS)
        mPlayer = null
    }

    override fun onMusicLyricsLoad(lrc: MusicLrc?) {
        lrc?.let {
            lrc_view.loadLrc(it.lyric)
            mHandler.sendEmptyMessage(MESSAGE_UPDATE_PROGRESS)
        }
    }

    override fun handleMsg(msg: Message) {
        when (msg.what) {
            MESSAGE_UPDATE_PROGRESS -> {
                if (mPlayer != null) {
                    val time = mPlayer!!.getProgress().toLong()
                    lrc_view.updateTime(time)
                }
                mHandler.sendEmptyMessageDelayed(
                    MESSAGE_UPDATE_PROGRESS,
                    UPDATE_PROGRESS_INTERVAL
                )
            }
        }
    }

}