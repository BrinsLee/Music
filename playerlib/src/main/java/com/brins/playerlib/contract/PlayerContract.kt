package com.brins.playerlib.contract

import android.content.Intent
import android.content.ServiceConnection
import androidx.annotation.Nullable
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.BasePlayList
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.playerlib.model.PlayerModel
import com.brins.baselib.module.PlayMode
import com.brins.playerlib.model.PlayBackService

interface PlayerContract {

    interface View : IView {

        fun onPlaybackServiceBound(service: PlayBackService)

        fun onPlaybackServiceUnbound()

        fun onSongUpdated(@Nullable song: BaseMusic?)

        fun onSongPlay()

        fun onSongPause()

        fun updatePlayMode(playMode: PlayMode)

        fun onMusicDelete()
    }

    abstract class Presenter : BasePresenter<PlayerModel, View>() {

        /**
         * 设置播放列表
         *
         * @param list
         */
        abstract fun setPlayList(list: BasePlayList)

        /**
         * 设置播放列表
         *
         * @param list
         */
        abstract fun setPlayList(list: MutableList<BaseMusic>)

        /**
         * 根据id播放音乐
         *
         * @param id
         */
        abstract fun play(music: BaseMusic)

        /**
         *绑定播放音乐服务
         */
        abstract fun bindPlaybackService()

        /**
         * 解除绑定
         */
        abstract fun unbindPlaybackService()

        /**
         * 删除音乐
         *
         * @param music
         */
        abstract fun delete(music: BaseMusic)

        /**
         * 移除列表
         *
         */
        abstract fun deleteAll()

        /**
         * 切换播放模式
         *
         * @param mode
         */
        abstract fun changePlayMode(mode : PlayMode)
    }

    interface Model : IModel {

        fun bindPlaybackService(
            intent: Intent,
            mConnection: ServiceConnection,
            bindAutoCreate: Int
        )

        fun unbindPlaybackService(mConnection: ServiceConnection)

        suspend fun checkMusicUsable(id: String): Boolean

        suspend fun loadMusicUrl(id: String): String

        suspend fun loadMusicComment(id: String)
    }
}