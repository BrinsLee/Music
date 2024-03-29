package com.brins.musicdetail.fragment

import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.config.KEY_COMMEND_PATH
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.config.MUSIC_COMMENT.Companion.MUSIC_COMMENT
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.BaseMusic
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
import com.brins.musicdetail.widget.PlayListPopup
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.music.MusicLrc
import com.brins.playerlib.model.PlayBackService
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.SimpleCallback
import kotlinx.android.synthetic.main.fragment_music_detail.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MusicDetailFragment : BaseMvpFragment<MusicDetailPresenter>(), WeakHandler.IHandler,
    View.OnClickListener, MusicDetailContract.View, PlayListPopup.OnClickListener {

    private val mHandler: WeakHandler by lazy { WeakHandler(this) }
    private val UPDATE_PROGRESS_INTERVAL: Long = 1000
    private val MESSAGE_UPDATE_PROGRESS = 0X10
    private var mPlayer: PlayBackService? = null
    private var mPlayListPopup: PlayListPopup? = null
    private var mDetector: GestureDetector? = null


    override fun getLayoutResID(): Int {
        return R.layout.fragment_music_detail
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun init(savedInstanceState: Bundle?) {
        if (mActivity is MusicDetailActivity) {
            mDetector = GestureDetector(mActivity, object :
                GestureDetector.SimpleOnGestureListener() {
                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {

                    return if (velocityY > 4000) {
                        (mActivity as MusicDetailActivity).finish()
                        true
                    } else {
                        super.onFling(e1, e2, velocityX, velocityY)
                    }
                }
            })
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
        if (!coverUrl.isNullOrEmpty()) {
            GlideHelper.setRoundImageResource(
                cover,
                coverUrl,
                20,
                R.drawable.base_icon_default_cover,
                0,
                0
            )
        } else if (music?.bitmapCover != null) {
            cover.setImageBitmap(music?.bitmapCover)
        }

        music?.let {
            tv_music_name.text = it.name
            tv_artist_name.text = getArtists(it)
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
        cl_root.setOnTouchListener { v, event -> mDetector!!.onTouchEvent(event) }
    }

    private fun getArtists(data: BaseMusic): String {
        val builder = StringBuilder()
        var artists = data.artists
        if (artists.isNullOrEmpty()) {
            artists = data.song?.artists
        }
        artists?.let {
            for (i in 0 until it.size) {
                builder.append(it[i].name)
                if (i != it.size - 1)
                    builder.append("，")
            }
        }
        if (artists.isNullOrEmpty()) {
            builder.append("未知")
        }
        return builder.toString()
    }

    override fun onResume() {
        super.onResume()
        upDateButtonStatus(mPlayer?.isPlaying() ?: false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPlayListPopup = null
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
        if (mPlayListPopup == null || !mPlayListPopup!!.isShow) {
            mPlayListPopup = showPopupList()
        }
//        ARouterUtils.go(RouterHub.PLAYLISTACTIVITY)
    }

    private fun showPopupList(): PlayListPopup {
        val list = mutableListOf<BaseData>()
        list.addAll(mPlayer!!.getPlayList().getSong())
        val popup = PlayListPopup(context!!, list, mPlayer?.getPlayingSong()?.id ?: "")
        XPopup.Builder(context)
            .hasShadowBg(true)
            .setPopupCallback(object : SimpleCallback() {
                override fun onShow() {
                    super.onShow()
                    mPlayListPopup?.setPlayModel(mPlayer?.getCurrentPlayMode())
                }
            })
            .asCustom(popup)
            .show()
        popup.setClickListener(this)
        return popup
    }

    private fun changePlayMode() {
        mPlayer?.let {
            when (it.getCurrentPlayMode()) {
                PlayMode.LOOP -> {
                    it.changePlayMode(PlayMode.SINGLE)
                    iv_play_mode.setImageResource(R.drawable.base_icon_play_single)
                    mPlayListPopup?.let { popup ->
                        if (popup.isShow) {
                            popup.setPlayModel(PlayMode.SINGLE)
                        }
                    }
                }

                PlayMode.SINGLE -> {
                    it.changePlayMode(PlayMode.LOOP)
                    iv_play_mode.setImageResource(R.drawable.base_icon_play_cycle)
                    mPlayListPopup?.let { popup ->
                        if (popup.isShow) {
                            popup.setPlayModel(PlayMode.LOOP)
                        }
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun openComments() {
        val bundle = Bundle()
        bundle.putString(KEY_ID, mPlayer?.getPlayingSong()?.id ?: "")
        bundle.putString(KEY_COMMEND_PATH, MUSIC_COMMENT)
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

    override fun onPlayModeClick() {
        changePlayMode()
    }

    override fun onTrashCanClick() {
        (mActivity as? MusicDetailActivity)?.deleteAll()
    }

    override fun onDeleteItem(item: BaseMusic, adapterPosition: Int) {
        mPlayer?.let {
            if (it.deleteMusic(item)) {
                mPlayListPopup?.let { popup ->
                    if (popup.isShow) {
                        popup.deleteItem(adapterPosition)
                    }
                }
            }

        }
    }

    override fun onItemClick(item: BaseMusic, adapterPosition: Int) {
        mPlayer?.let {
            if (it.play(item)) {
                updateSong(EventBusParams(EventBusKey.KEY_EVENT_RESUME_MUSIC, null))
                mPlayListPopup?.let { popup ->
                    if (popup.isShow()) {
                        popup.updateData(item, adapterPosition)
                    }
                }
            }
        }
    }
}