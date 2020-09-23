package com.brins.playerlib.contract

import androidx.annotation.Nullable
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.BasePlayList
import com.brins.baselib.module.PlayMode

interface IPlayback {

    /**
     * 设置当前播放列表
     *
     * @param list
     */
    fun setPlayList(list: BasePlayList)

    /**
     * 设置播放列表
     *
     * @param list
     */
    fun setPlayList(list: MutableList<BaseMusic>)

    /**
     * 获取播放列表
     *
     * @return
     */
    fun getPlayList(): BasePlayList

    /**
     * 播放
     *
     * @return
     */
    fun play(music: BaseMusic): Boolean

    /**
     * 播放
     *
     * @return
     */
    fun play(): Boolean


    /**
     * 播放
     *
     * @param url
     * @return
     */
    fun play(url: String): Boolean

    /**
     * 播放上一首
     *
     * @return
     */
    fun playLast(): Boolean

    /**
     * 播放下一首
     *
     * @return
     */
    fun playNext(): Boolean

    /**
     * 继续播放
     *
     * @return
     */
    fun resume(): Boolean

    /**
     * 暂停
     *
     * @return
     */
    fun pause(): Boolean

    /**
     * 是否正在播放
     *
     * @return
     */
    fun isPlaying(): Boolean

    /**
     * 停止
     *
     */
    fun stop()

    /**
     * 获取播放进度
     *
     * @return
     */
    fun getProgress(): Int

    /**
     * 释放资源
     *
     */
    fun releasePlayer()

    /**
     * 获取正在播放的音乐
     *
     * @return
     */
    fun getPlayingSong(): BaseMusic?

    /**
     * 获取加载进度
     *
     * @return
     */
    fun getLoadPrecent(): Int

    /**
     * 拖动播放进度条
     *
     * @param progress
     * @return
     */
    fun seekTo(progress: Int): Boolean

    /**
     * 获取音乐时长
     *
     * @return
     */
    fun getCurrentSongDuration(): Int

    /**
     * 设置播放模式
     *
     * @param mode
     * @return
     */
    fun changePlayMode(mode: PlayMode)

    /**
     * 获取当前的播放模式
     *
     * @return
     */
    fun getCurrentPlayMode(): PlayMode

    /**
     * 删除音乐
     *
     * @param music
     */
    fun deleteMusic(music: BaseMusic): Boolean

    /**
     * 删除全部音乐
     *
     */
    fun deleteAll(): Boolean

    interface Callback {

        fun onPlayStatusChanged(isPlaying: Boolean, @Nullable music: BaseMusic?)
    }

}