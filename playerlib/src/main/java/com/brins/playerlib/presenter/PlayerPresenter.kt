package com.brins.playerlib.presenter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.Toast
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.BasePlayList
import com.brins.baselib.module.PlayMode
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusKey.KEY_EVENT_UPDATE_MUSIC
import com.brins.baselib.utils.eventbus.EventBusKey.KEY_EVENT_UPDATE_PLAY_MODE
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.playerlib.R
import com.brins.playerlib.contract.PlayerContract
import com.brins.playerlib.model.PlayBackService
import com.brins.playerlib.model.PlayBackService.Companion.mIsServiceBound
import org.greenrobot.eventbus.EventBus

class PlayerPresenter : PlayerContract.Presenter() {

    companion object {
        private var mPlaybackService: PlayBackService? = null
    }

    val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mPlaybackService?.onDestroy()
            mPlaybackService = null
            mView?.onPlaybackServiceUnbound()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPlaybackService = (service as PlayBackService.LocalBinder).service
            mPlaybackService!!.bind(this@PlayerPresenter)
            mView?.onPlaybackServiceBound(mPlaybackService!!)
        }

    }

    override fun setPlayList(list: BasePlayList) {
        mPlaybackService?.setPlayList(list)
    }

    override fun setPlayList(list: MutableList<BaseMusic>) {
        mPlaybackService?.setPlayList(list)
    }

    override fun setPlayList(list: MutableList<BaseMusic>, index: Int) {
        mPlaybackService?.setPlayList(list, index)
    }

    override fun play(music: BaseMusic) {
        if (!music.musicUrl.isNullOrEmpty()){
            mPlaybackService?.let {
                if (it.play(music)) {
                    mView?.onSongUpdated(music)
                }
            }
        }else{
            launch({
                val result = mModel?.loadMusicUrl(music.id)
                if (result != null && result.isNotEmpty()) {
                    music.musicUrl = result
                    mPlaybackService?.let {
                        if (it.play(music)) {
                            mView?.onSongUpdated(music)
                        }
                    }

                } else {
                    ToastUtils.show(UIUtils.getString(R.string.music_not_available), Toast.LENGTH_SHORT)
                }
            }, {
                ToastUtils.show(UIUtils.getString(R.string.music_not_available), Toast.LENGTH_SHORT)
            })
        }

    }

    /**
     * 更新歌曲信息
     *
     * @param music
     */
    fun onSongUpdate(music: BaseMusic?) {
        mView?.onSongUpdated(music)
        EventBus.getDefault().post(EventBusParams(KEY_EVENT_UPDATE_MUSIC, music))
    }

    /**
     * 暂停播放
     *
     */
    fun onSongPause() {
        EventBusManager.post(EventBusKey.KEY_EVENT_PAUSE_MUSIC)
        mView?.onSongPause()
    }

    /**
     * 继续播放
     *
     */
    fun onSongPlay() {
        EventBusManager.post(EventBusKey.KEY_EVENT_RESUME_MUSIC)
        mView?.onSongPlay()
    }

    override fun bindPlaybackService() {
        if (!mIsServiceBound) {
            val context = UIUtils.getContext()
            val intent = Intent(context, PlayBackService::class.java)
            mModel?.bindPlaybackService(intent, mConnection, Context.BIND_AUTO_CREATE)
        } else {
            mView?.onPlaybackServiceBound(mPlaybackService!!)
        }
    }

    override fun unbindPlaybackService() {
        if (mIsServiceBound) {
            mModel?.unbindPlaybackService(mConnection)
            mIsServiceBound = false
        }
    }

    override fun delete(music: BaseMusic) {
        if (mPlaybackService?.deleteMusic(music) == true) {
            mView?.onMusicDelete()
        }
    }

    override fun deleteAll() {
        if (mPlaybackService?.deleteAll() == true) {
            mView?.onMusicDelete()
        }
    }

    override fun changePlayMode(mode: PlayMode) {
        mPlaybackService?.changePlayMode(mode)
        mView?.updatePlayMode(mode)
        EventBus.getDefault().post(EventBusParams(KEY_EVENT_UPDATE_PLAY_MODE, mode))
    }
}