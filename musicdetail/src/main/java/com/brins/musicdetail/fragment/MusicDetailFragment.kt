package com.brins.musicdetail.fragment

import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.module.PlayMode
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.utils.*
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.musicdetail.R
import com.brins.musicdetail.activity.MusicDetailActivity
import com.brins.musicdetail.contract.MusicDetailContract
import com.brins.musicdetail.presenter.MusicDetailPresenter
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.music.MusicLrc
import com.brins.playerlib.model.PlayBackService
import kotlinx.android.synthetic.main.fragment_music_detail.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MusicDetailFragment : BaseMvpFragment<MusicDetailPresenter>(), WeakHandler.IHandler,
    View.OnClickListener, MusicDetailContract.View {

    private val mHandler: WeakHandler by lazy { WeakHandler(this) }
    private val UPDATE_PROGRESS_INTERVAL: Long = 1000
    private val MESSAGE_UPDATE_PROGRESS = 0X10
    private var mPlayer: PlayBackService? = null

    override fun getLayoutResID(): Int {
        return R.layout.fragment_music_detail
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun init(savedInstanceState: Bundle?) {
        if (mActivity is MusicDetailActivity) {
            mPlayer = (mActivity as MusicDetailActivity).getPlayBackService()
            initMusicDetail()
        }
    }

    fun initMusicDetail() {
        val music = mPlayer?.getPlayingSong()

        var coverUrl = music?.picUrl
        if (coverUrl == null || coverUrl.isEmpty()) {
            coverUrl = music?.song?.picUrl
        }
        GlideHelper.setRoundImageResource(
            cover,
            coverUrl,
            20,
            R.drawable.base_icon_default_cover,
            0,
            0
        )
        music?.let {
            tv_music_name.text = it.name
            tv_artist_name.text = it.song?.artists?.get(0)?.name ?: it.artists?.get(0)?.name
                    ?: UIUtils.getString(R.string.unknow)
            tv_album_name.text = it.song?.album?.name ?: it.name
            var duration = it.song?.duration ?: 0
            if (duration == 0) {
                duration = it.duration
            }
            LoginCache.likeResult?.ids?.let { list ->
                if (list.contains(it.id)) {
                    iv_like.setImageResource(R.drawable.base_icon_like_heart)
                } else {
                    iv_like.setImageResource(R.drawable.base_icon_unlike_heart)
                }
            }
            tvDuration.text = formatDuration(duration)
            seekBar.progress = initProgress(mPlayer!!.getProgress())
            updateProgressTextWithDuration(mPlayer!!.getProgress())
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (p2) {
                        updateProgressTextWithDuration(getDuration(p1))
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS)
                }

                override fun onStopTrackingTouch(p0: SeekBar) {
                    mPlayer?.seekTo(getDuration(p0.progress))
                    if (mPlayer!!.isPlaying()) {
                        mHandler.sendEmptyMessage(MESSAGE_UPDATE_PROGRESS)
                    }
                }

            })
            iv_play_pause.setOnClickListener(this)
            iv_play_last.setOnClickListener(this)
            iv_play_next.setOnClickListener(this)
            iv_comments.setOnClickListener(this)
            iv_play_mode.setOnClickListener(this)
            iv_play_list.setOnClickListener(this)
            iv_like.setOnClickListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        upDateButtonStatus(mPlayer?.isPlaying() ?: false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS)
        mPlayer = null
    }

    override fun handleMsg(msg: Message) {
        when (msg.what) {
            MESSAGE_UPDATE_PROGRESS -> {
                if (mPlayer != null && mPlayer!!.isPlaying()) {
                    val progress = seekBar.max * mPlayer!!.getProgress() / getCurrentSongDuration()
                    updateProgressTextWithDuration(mPlayer!!.getProgress())
                    if (progress >= 0 && progress <= seekBar.max) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            seekBar.setProgress(progress, true)
                        } else {
                            seekBar.progress = progress
                        }
                        mHandler.sendEmptyMessageDelayed(
                            MESSAGE_UPDATE_PROGRESS,
                            UPDATE_PROGRESS_INTERVAL
                        )
                    }
                }
            }
        }
    }

    private fun updateProgressTextWithDuration(duration: Int) {
        tvProgress.text = formatDuration(duration)
    }

    private fun initProgress(progress: Int): Int {
        return seekBar.max * progress / getCurrentSongDuration()
    }

    private fun getCurrentSongDuration(): Int {
        return mPlayer?.getCurrentSongDuration() ?: 1
    }

    private fun getDuration(progress: Int): Int {
        return (getCurrentSongDuration() * (progress.toFloat() / seekBar.max)).toInt()
    }

    private fun onPlayOrPause() {
        mPlayer?.let {
            if (it.isPlaying()) {
                it.pause()
                upDateButtonStatus(it.isPlaying())
            } else {
                it.resume()
                upDateButtonStatus(it.isPlaying())
            }
        }
    }

    private fun upDateButtonStatus(isPlaying: Boolean) {
        mHandler.sendEmptyMessage(MESSAGE_UPDATE_PROGRESS)
        if (isPlaying) {
            iv_play_pause.setImageDrawable(UIUtils.getDrawable(R.drawable.base_icon_pause_white_64dp))
        } else {
            mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS)
            iv_play_pause.setImageDrawable(UIUtils.getDrawable(R.drawable.base_icon_play_white_64dp))
        }
        when (mPlayer?.getCurrentPlayMode()) {
            PlayMode.LOOP -> {
                iv_play_mode.setImageResource(R.drawable.base_icon_play_cycle)
            }

            PlayMode.SINGLE -> {
                iv_play_mode.setImageResource(R.drawable.base_icon_play_single)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateSong(params: EventBusParams) {
        when (params.key) {
            EventBusKey.KEY_EVENT_UPDATE_PLAY_MODE -> {
                upDateButtonStatus(mPlayer?.isPlaying() ?: false)
            }
            EventBusKey.KEY_EVENT_RESUME_MUSIC, EventBusKey.KEY_EVENT_PAUSE_MUSIC -> {
                mPlayer?.let {
                    upDateButtonStatus(it.isPlaying())
                }
            }
        }
    }

    private fun onPlayNext() {
        mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS)
        mPlayer?.playNext()
    }

    private fun onPlayLast() {
        mHandler.removeMessages(MESSAGE_UPDATE_PROGRESS)
        mPlayer?.playLast()
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.iv_play_pause -> onPlayOrPause()
            R.id.iv_play_last -> onPlayLast()
            R.id.iv_play_next -> onPlayNext()
            R.id.iv_comments -> openComments()
            R.id.iv_play_mode -> changePlayMode()
            R.id.iv_play_list -> openPlayList()
            R.id.iv_like -> updateLikeStatus()
        }
    }

    private fun updateLikeStatus() {
        mPlayer?.getPlayingSong()?.let {
            LoginCache.likeResult?.ids?.let { list ->
                if (list.contains(it.id)) {
                    launch({
                        mPresenter?.unLikeMusic(it.id)
                    }, {
                        ToastUtils.show(
                            UIUtils.getString(R.string.network_error),
                            Toast.LENGTH_SHORT
                        )
                    })
                } else {
                    launch({
                        mPresenter?.likeMusic(it.id)
                    }, {
                        ToastUtils.show(
                            UIUtils.getString(R.string.network_error),
                            Toast.LENGTH_SHORT
                        )
                    })
                }
            }
        }

    }

    private fun openPlayList() {
        ARouterUtils.go(RouterHub.PLAYLISTACTIVITY)
    }

    private fun changePlayMode() {
        mPlayer?.let {
            when (it.getCurrentPlayMode()) {
                PlayMode.LOOP -> {
                    it.changePlayMode(PlayMode.SINGLE)
                    iv_play_mode.setImageResource(R.drawable.base_icon_play_single)
                }

                PlayMode.SINGLE -> {
                    it.changePlayMode(PlayMode.LOOP)
                    iv_play_mode.setImageResource(R.drawable.base_icon_play_cycle)
                }
            }
        }
    }

    private fun openComments() {
        val bundle = Bundle()
        bundle.putString(KEY_ID, mPlayer?.getPlayingSong()?.id ?: "")
        ARouterUtils.go(RouterHub.COMMENTSACTIVITY, bundle)
    }

    override fun onLikeMusic(isSuccess: Boolean, id: String) {
        if (isSuccess) {
            LoginCache.likeResult?.ids?.add(id)
            SpUtils.obtain(SpUtils.SP_USER_INFO, context)
                .save(SpUtils.KEY_USER_LIKE, GsonUtils.toJson(LoginCache.likeResult))
            iv_like.setImageResource(R.drawable.base_icon_like_heart)
            ToastUtils.show(getString(R.string.like_success), Toast.LENGTH_SHORT)
        }
    }

    override fun onDislikeMusic(isSuccess: Boolean, id: String) {
        if (isSuccess) {
            LoginCache.likeResult?.ids?.remove(id)
            SpUtils.obtain(SpUtils.SP_USER_INFO, context)
                .save(SpUtils.KEY_USER_LIKE, GsonUtils.toJson(LoginCache.likeResult))
            iv_like.setImageResource(R.drawable.base_icon_unlike_heart)
            ToastUtils.show(getString(R.string.dislike_success), Toast.LENGTH_SHORT)
        }
    }

    override fun onMusicDetialLoad() {
    }

    override fun onMusicLyricsLoad(lrc: MusicLrc?) {
    }
}