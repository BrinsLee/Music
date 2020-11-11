package com.brins.playerlib.model

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.brins.baselib.BaseMvpService
import com.brins.baselib.database.factory.DatabaseFactory
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.BasePlayList
import com.brins.baselib.module.PlayMode
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.subscribeDbResult
import com.brins.playerlib.broadcast.HeadsetButtonReceiver
import com.brins.playerlib.contract.IPlayback
import com.brins.playerlib.presenter.PlayerPresenter
import java.util.ArrayList

class PlayBackService : BaseMvpService<PlayerPresenter>(), IPlayback,
    HeadsetButtonReceiver.onHeadsetListener {

    companion object {
        @JvmStatic
        private val CHANNEL_ID = "com.brins.lightmusic.notification.channel"
        private val NOTIFICATION_ID = 1
        var mIsServiceBound: Boolean = false
    }

    private val mPlayer: Player by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Player() }
    private val mBinder = LocalBinder()
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var remoteView: RemoteViews

    inner class LocalBinder : Binder() {
        val service: PlayBackService
            get() = this@PlayBackService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        HeadsetButtonReceiver(this)
        mIsServiceBound = true
//        EventBus.getDefault().register(this)

//        MediaSessionManager(this)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    override fun onDestroy() {
        releasePlayer()
//        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun getPlayingSong(): BaseMusic? {
        return mPlayer.getPlayingSong()
    }

    override fun setPlayList(list: BasePlayList) {
        mPlayer.setPlayList(list)
    }

    override fun setPlayList(list: MutableList<BaseMusic>) {
        mPlayer.setPlayList(list)
    }

    override fun setPlayList(list: MutableList<BaseMusic>, index: Int) {
        mPlayer.setPlayList(list, index)
    }

    override fun getPlayList(): BasePlayList {
        return mPlayer.getPlayList()
    }

    override fun play(music: BaseMusic): Boolean {
        return mPlayer.play(music)
    }

    override fun play(): Boolean {
        return mPlayer.play()
    }

    override fun play(url: String): Boolean {
        return mPlayer.play(url)
    }

    override fun playLast(): Boolean {
        return mPlayer.playLast()
    }

    override fun playNext(): Boolean {
        return mPlayer.playNext()
    }

    override fun resume(): Boolean {
        val resume = mPlayer.resume()
        if (resume) {
            mPresenter?.onSongPlay()
        }
        return resume
    }

    override fun pause(): Boolean {
        val paused = mPlayer.pause()
        if (paused) {
            mPresenter?.onSongPause()
        }
        return paused
    }

    override fun isPlaying(): Boolean {
        return mPlayer.isPlaying()
    }

    override fun stop() {
        mPlayer.stop()
    }

    override fun getProgress(): Int {
        return mPlayer.getProgress()
    }

    override fun releasePlayer() {
        mPlayer.releasePlayer()
    }

    override fun getLoadPrecent(): Int {
        return mPlayer.getLoadPrecent()
    }

    override fun seekTo(progress: Int): Boolean {
        return mPlayer.seekTo(progress)
    }

    override fun getCurrentSongDuration(): Int {
        return mPlayer.getCurrentSongDuration()
    }

    override fun changePlayMode(mode: PlayMode) {
        mPlayer.changePlayMode(mode)
    }

    override fun getCurrentPlayMode(): PlayMode {
        return mPlayer.getCurrentPlayMode()
    }

    override fun deleteMusic(music: BaseMusic): Boolean {
        return mPlayer.deleteMusic(music)
    }

    override fun deleteAll(): Boolean {
        return mPlayer.deleteAll()
    }

    /**
     * 负责播放器的类
     *
     */
    inner class Player : IPlayback, MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnBufferingUpdateListener {
        private val TAG = "Player"

        private val mAudioManager: AudioManager by lazy {
            UIUtils.getApplication().getSystemService(
                Context.AUDIO_SERVICE
            ) as AudioManager
        }

        /**
         * 音频属性
         */
        @RequiresApi(Build.VERSION_CODES.O)
        private val mAudioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()

        /**
         * 音频焦点请求
         */
        @RequiresApi(Build.VERSION_CODES.O)
        private val mAudioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(mAudioAttributes).setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(this).build()

        /**
         * 媒体播放器
         */
        private var mPlayer: MediaPlayer = MediaPlayer()
        private var mPlayList: BasePlayList = BasePlayList()
        private var mPlayOnAudioFocus = false
        private val mCallbacks = ArrayList<IPlayback.Callback>(1)
        private var isPaused: Boolean = false
        private var isPlayingBeforeLoseFocuse = true
        private var mLoadPrecent = 0

        init {
            mPlayer.setOnCompletionListener(this)
            mPlayer.setOnBufferingUpdateListener(this)
            mPlayer.setWakeMode(
                UIUtils.getApplication(),
                PowerManager.PARTIAL_WAKE_LOCK
            )
        }

        /**
         * 设置播放列表
         *
         * @param list
         */
        override fun setPlayList(list: BasePlayList) {
            mPlayList = list
        }

        /**
         * 设置播放列表
         *
         * @param list
         */
        override fun setPlayList(list: MutableList<BaseMusic>) {
            mPlayList.add(list)
        }

        /**
         * 设置播放列表及当前音乐位置
         *
         * @param list
         * @param index
         */
        override fun setPlayList(list: MutableList<BaseMusic>, index: Int) {
            mPlayList.add(list)
            mPlayList.setPlayingIndex(index)
        }

        /**
         * 获取播放列表
         *
         * @return
         */
        override fun getPlayList(): BasePlayList {
            return mPlayList
        }

        /**
         * 播放音乐
         *
         * @param music
         * @return
         */
        override fun play(music: BaseMusic): Boolean {
            mPlayOnAudioFocus = requestFocus()
            if (mPlayOnAudioFocus) {
                if (mPlayList.getCurrentSong()?.id == music.id && isPaused) {
                    mPlayer.start()
                    return true
                }
                mPlayList.add(music)
                if (mPlayList.prepare()) {
                    try {
                        DatabaseFactory.addRecentlyMusic(music)
                            .subscribeDbResult({}, {
                                Log.d("DataBaseFactory", it.message!!)
                            })
                        mPlayer.reset()
                        mPlayer.setDataSource(music.musicUrl)
                        mPlayer.prepare()
                        mPlayer.start()
                        mPresenter?.onSongUpdate(music)

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        return false
                    }
                    return true
                }

            }
            return false
        }

        /**
         * 播放音乐
         *
         * @return
         */
        override fun play(): Boolean {
            mPlayOnAudioFocus = requestFocus()
            if (mPlayOnAudioFocus) {
                if (isPaused) {
                    mPlayer.start()
                    return true
                }
                if (mPlayList.prepare()) {
                    val music = mPlayList.getCurrentSong()
                    music?.let {
                        if (it.musicUrl.isNullOrEmpty()) {
                            mPresenter?.play(it)
                            return false
                        }
                        try {
                            DatabaseFactory.addRecentlyMusic(music)
                                .subscribeDbResult({}, {
                                    Log.d("DataBaseFactory", it.message!!)
                                })
                            mPlayer.reset()
                            mPlayer.setDataSource(it.musicUrl)
                            mPlayer.prepare()
                            mPlayer.start()
                            mPresenter?.onSongUpdate(it)

                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            return false
                        }
                        return true
                    }
                    return false
                }
            }
            return false
        }

        /**
         * 通过url播放音乐
         *
         * @param url
         * @return
         */
        override fun play(url: String): Boolean {
            mPlayOnAudioFocus = requestFocus()
            if (mPlayOnAudioFocus) {
                if (isPaused) {
                    mPlayer.start()
//                    notifyPlayStatusChanged(true)
                    return true
                }
                if (mPlayList.prepare()) {
                    try {
                        mPlayList.getCurrentSong()?.let {
                            DatabaseFactory.addRecentlyMusic(it)
                                .subscribeDbResult({}, {
                                    Log.d("DataBaseFactory", it.message!!)
                                })
                        }
                        mPlayer.reset()
                        mPlayer.setDataSource(url)
                        mPlayer.prepare()
                        mPlayer.start()

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

            }
            return true
        }

        /**
         * 播放上一首
         *
         * @return
         */
        override fun playLast(): Boolean {
            isPaused = false
            mPlayList.last()
            return play()
        }

        /**
         * 播放下一首
         *
         * @return
         */
        override fun playNext(): Boolean {
            isPaused = false
            mPlayList.next()
            return play()
        }

        /**
         * 继续播放
         *
         * @return
         */
        override fun resume(): Boolean {
            if (mPlayOnAudioFocus && !mPlayer.isPlaying && isPlayingBeforeLoseFocuse) {
                mPlayer.start()
                isPaused = false
                return true
            }
            return false
        }

        /**
         * 暂停
         *
         * @return
         */
        override fun pause(): Boolean {
            Log.d(TAG, "$isPlayingBeforeLoseFocuse")
            Log.d(TAG, "$mPlayOnAudioFocus")
            if (mPlayer.isPlaying) {
                mPlayer.pause()
                isPaused = true
                return true
            }
            return false
        }

        /**
         * 是否正在播放
         *
         * @return
         */
        override fun isPlaying(): Boolean {
            var isPlay = false
            try {
                isPlay = mPlayer.isPlaying
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return isPlay
        }

        /**
         * 停止
         *
         */
        override fun stop() {
            isPaused = false
            mPlayer.stop()
        }

        /**
         * 获取播放进度
         *
         * @return
         */
        override fun getProgress(): Int {
            return mPlayer.currentPosition
        }

        /**
         * 播放完成
         *
         * @param p0
         */
        override fun onCompletion(p0: MediaPlayer?) {
            if (mPlayList.getPlayMode() === PlayMode.LIST && mPlayList.getPlayingIndex() === mPlayList.getNumOfSongs() - 1) {


            } else if (mPlayList.getPlayMode() === PlayMode.SINGLE) {
                play()
            } else {
                val hasNext = mPlayList.hasNext(true)
                if (hasNext) {
                    playNext()
                }
            }
        }

        /**
         * 音频焦点获取/丢失
         *
         * @param p0
         */
        override fun onAudioFocusChange(focusChange: Int) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    mPlayOnAudioFocus = false
                    isPlayingBeforeLoseFocuse = mPlayer.isPlaying
                    pause()
                }
                AudioManager.AUDIOFOCUS_GAIN -> {
                    mPlayOnAudioFocus = true
                    resume()
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest)
                    } else {
                        mAudioManager.abandonAudioFocus(this)
                    }
                    stop()
                }
            }
        }

        /**
         * 释放播放器资源
         *
         */
        override fun releasePlayer() {
            mPlayer.reset()
            mPlayer.release()
        }

        /**
         * 获取正在播放的音乐
         *
         * @return
         */
        override fun getPlayingSong(): BaseMusic? {
            return mPlayList.getCurrentSong()
        }

        override fun getLoadPrecent(): Int {
            return mLoadPrecent
        }

        override fun seekTo(progress: Int): Boolean {
            if (mPlayList.getNumOfSongs() == 0) {
                return false
            }
            val duration = getCurrentSongDuration()
            if (duration <= progress) {
                onCompletion(mPlayer)
            } else {
                mPlayer.seekTo(progress)
            }
            return true
        }

        /**
         * 请求音频焦点
         *
         * @return
         */
        private fun requestFocus(): Boolean {
            // Request audio focus for playback
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val result = mAudioManager.requestAudioFocus(mAudioFocusRequest)
                result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            } else {
                val result = mAudioManager.requestAudioFocus(
                    this,
                    // Use the music stream.
                    AudioManager.STREAM_MUSIC,
                    // Request permanent focus.
                    AudioManager.AUDIOFOCUS_GAIN
                )
                result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            }
        }

        /**
         * 加载进度监听
         *
         * @param p0
         * @param p1 百分比
         */
        override fun onBufferingUpdate(p0: MediaPlayer?, p1: Int) {
            mLoadPrecent = p1
        }

        /**
         * 获取音乐时长
         *
         * @return
         */
        override fun getCurrentSongDuration(): Int {
            val currentSong = getPlayingSong()
            var duration = 1
            if (currentSong != null) {
                duration = currentSong.duration
                if (duration == 0) {
                    duration = currentSong.song!!.duration
                }
            }
            return duration
        }

        /**
         * 设置播放模式
         *
         * @param mode
         */
        override fun changePlayMode(mode: PlayMode) {
            mPlayList.setPlayMode(mode)
        }

        override fun getCurrentPlayMode(): PlayMode {
            return mPlayList.getPlayMode()
        }

        override fun deleteMusic(music: BaseMusic): Boolean {
            return mPlayList.delete(music)
        }

        override fun deleteAll(): Boolean {
            return mPlayList.deleteAll()
        }
    }

    //耳机线控
    override fun playOrPause() {
        if (mPlayer.isPlaying()) {
            pause()
        } else {
            resume()
        }
    }

    override fun playNextSong() {
        playNext()
    }

    override fun playPreviousSong() {
        playLast()
    }
}