package com.brins.playerlib.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.ImageView
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.brins.baselib.BaseMvpService
import com.brins.baselib.config.*
import com.brins.baselib.database.factory.DatabaseFactory
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.BasePlayList
import com.brins.baselib.module.PlayMode
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.utils.subscribeDbResult
import com.brins.bridgelib.app.AppBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.playerlib.R
import com.brins.playerlib.broadcast.HeadsetButtonReceiver
import com.brins.playerlib.contract.IPlayback
import com.brins.playerlib.presenter.PlayerPresenter
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
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
    private val playMusicReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (!action.isNullOrEmpty()) {
                when (action) {
                    ACTION_PRV -> playLast()
                    ACTION_NEXT -> playNext()
                    ACTION_PAUSE -> pause()
                    ACTION_PLAY -> play()
                }
            }
        }

    }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cancelNotification()
            updateNotification()
        }
        val filter = IntentFilter()
        filter.addAction(ACTION_PLAY)
        filter.addAction(ACTION_PAUSE)
        filter.addAction(ACTION_NEXT)
        filter.addAction(ACTION_PRV)
        registerReceiver(playMusicReceiver, filter)
//        EventBus.getDefault().register(this)

//        MediaSessionManager(this)

    }

    /**
     * 更新通知
     *
     */
    private fun updateNotification() {
        remoteView = RemoteViews(packageName, R.layout.view_notification)
        val song = getPlayList().getCurrentSong()
        song?.let {
            val cover = if (it.picUrl.isEmpty()) {
                it.song?.picUrl
            } else {
                it.picUrl
            }

            if (it.bitmapCover == null) {
                GlideHelper.setImageResource(
                    ImageView(this),
                    cover,
                    200,
                    200,
                    R.drawable.base_icon_default_cover,
                    true,
                    object : RequestListener<BitmapDrawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<BitmapDrawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: BitmapDrawable?,
                            model: Any?,
                            target: Target<BitmapDrawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource?.let { bitmapDrawable ->
                                it.bitmapCover = bitmapDrawable.bitmap
                                remoteView.setImageViewBitmap(
                                    R.id.widget_album,
                                    bitmapDrawable.bitmap
                                )
                                createNotificationChannel()
                            }
                            return true
                        }

                    }
                )
            } else {
                remoteView.setImageViewBitmap(
                    R.id.widget_album,
                    it.bitmapCover
                )
            }

            remoteView.setTextViewText(R.id.widget_title, it.name)
            remoteView.setTextViewText(
                R.id.widget_artist,
                getArtists(it)
            )

            remoteView.setImageViewResource(
                R.id.widget_play,
                if (isPlaying()) R.drawable.base_icon_pause_white_64dp else R.drawable.base_icon_play_white_64dp
            )

        }
        createNotificationChannel()
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

    /**
     * 创建通知频道
     *
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_MIN
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance)
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            val intent = BridgeProviders.instance.getBridge(AppBridgeInterface::class.java)
                .getSplashActivity(this)
            val intentGo = PendingIntent.getActivity(
                this,
                CODE_MAIN,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            remoteView.setOnClickPendingIntent(R.id.notice, intentGo)
            val intentClose = Intent(ACTION_EXIST)
            remoteView.setOnClickPendingIntent(
                R.id.widget_close,
                PendingIntent.getBroadcast(
                    this,
                    CODE_CLOSE,
                    intentClose,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            val playorpause = Intent()
            if (isPlaying()) {
                playorpause.action = ACTION_PAUSE
                val intent_play = PendingIntent.getBroadcast(
                    this,
                    CODE_PAUSE,
                    playorpause,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                remoteView.setOnClickPendingIntent(R.id.widget_play, intent_play)
            } else {
                playorpause.action = ACTION_PLAY
                val intent_play = PendingIntent.getBroadcast(
                    this,
                    CODE_PLAY,
                    playorpause,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                remoteView.setOnClickPendingIntent(R.id.widget_play, intent_play)
            }

            val prv = Intent(ACTION_PRV)
            val intent_prv =
                PendingIntent.getBroadcast(this, CODE_PRV, prv, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.widget_prev, intent_prv)

            val next = Intent(ACTION_NEXT)
            val intent_next =
                PendingIntent.getBroadcast(this, CODE_NEXT, next, PendingIntent.FLAG_UPDATE_CURRENT)
            remoteView.setOnClickPendingIntent(R.id.widget_next, intent_next)

            builder.setCustomContentView(remoteView)
                .setSmallIcon(R.drawable.base_icon_music_note_white_48dp)
                .setContentTitle("轻籁")
            mNotificationManager = getSystemService(NotificationManager::class.java)
            mNotificationManager.createNotificationChannel(channel)
            startForeground(NOTIFICATION_ID, builder.build())

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

    private fun cancelNotification() {
        if (::mNotificationManager.isInitialized) {
            mNotificationManager.cancelAll() //从状态栏中移除通知
        }
    }

    override fun onDestroy() {
        releasePlayer()
        unregisterReceiver(playMusicReceiver)
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
        val play = mPlayer.play(music)
        if (play) {
            updateNotification()
        }
        return play
    }

    override fun play(): Boolean {
        val play = mPlayer.play()
        if (play) {
            updateNotification()
        }
        return play
    }

    override fun play(url: String): Boolean {
        val play = mPlayer.play(url)
        if (play) {
            updateNotification()
        }
        return play
    }

    override fun playLast(): Boolean {
        val play = mPlayer.playLast()
        if (play) {
            updateNotification()
        }
        return play
    }

    override fun playNext(): Boolean {
        val play = mPlayer.playNext()
        if (play) {
            updateNotification()
        }
        return play
    }

    override fun resume(): Boolean {
        val resume = mPlayer.resume()
        if (resume) {
            mPresenter?.onSongPlay()
            updateNotification()
        }
        return resume
    }

    override fun pause(): Boolean {
        val paused = mPlayer.pause()
        if (paused) {
            mPresenter?.onSongPause()
            updateNotification()
        }
        return paused
    }

    override fun isPlaying(): Boolean {
        return mPlayer.isPlaying()
    }

    override fun stop() {
        mPlayer.stop()
        updateNotification()
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