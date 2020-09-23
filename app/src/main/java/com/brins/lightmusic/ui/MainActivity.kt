package com.brins.lightmusic.ui


import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import butterknife.OnClick
import com.brins.baselib.activity.BaseActivity
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.BaseMusic
import com.brins.baselib.module.ITEM_MUSIC_LIST_TRACK_MUSIC
import com.brins.baselib.module.PlayMode
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.baselib.utils.getStatusBarHeight
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.lightmusic.R
import com.brins.lightmusic.adapter.MainPagerAdapter
import com.brins.networklib.model.personal.PersonalizedMusic
import com.brins.playerlib.contract.PlayerContract
import com.brins.playerlib.model.PlayBackService
import com.brins.playerlib.presenter.PlayerPresenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_bottom_bar.*
import kotlinx.android.synthetic.main.view_common_toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : BaseMvpActivity<PlayerPresenter>(), PlayerContract.View {

    private var currentPage = 0

    @Inject
    lateinit var adapter: MainPagerAdapter
    private var mClickTime: Long = 0
    private var mPlayer: PlayBackService? = null
    private val mHandler = Handler()
    private var mProgressCallback = object : Runnable {
        override fun run() {
            if (mPlayer != null && mPlayer!!.isPlaying()) {
                val progress =
                    cover_container.getMax() * mPlayer!!.getProgress() / getCurrentSongDuration()
                if (progress >= 0 && progress <= cover_container.getMax()) {
                    cover_container.setProgress(progress)
                    mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL)
                }
            }
        }

    }

    companion object {
        private val UPDATE_PROGRESS_INTERVAL: Long = 1000
        private val UPDATE_MUSIC_INTERVAL: Long = 500
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.setPadding(0, getStatusBarHeight(this), 0, 0)
        initViewPagerAndTabLay()
        mPresenter?.bindPlaybackService()
    }

    override fun useEventBus(): Boolean {
        return true
    }

    @Suppress("UNCHECKED_CAST")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun playMusic(params: EventBusParams) {
        when (params.key) {
            EventBusKey.KEY_EVENT_PERSONALIZED_MUSIC -> {
                val musicList = params.`object` as MutableList<BaseMusic>
                mPresenter?.setPlayList(musicList)
                val position = params.extra.toInt()
                mPresenter?.play(musicList[position])
            }

            EventBusKey.KEY_EVENT_BANNER_MUSIC -> {
                val music = params.`object` as BaseMusic
                mPresenter?.play(music)
            }
        }
    }

    private fun getCurrentSongDuration(): Int {
        val currentSong = mPlayer!!.getPlayingSong()
        var duration = 1
        if (currentSong != null) {
            duration = currentSong.duration
            if (duration == 0) {
                duration = currentSong.song?.duration ?: 1
            }
        }
        return duration
    }

    private fun initProgress(progress: Int): Int {
        return cover_container.getMax() * progress / getCurrentSongDuration()
    }

    private fun initViewPagerAndTabLay() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        view_pager.adapter = adapter
        view_pager.offscreenPageLimit = 3
        changeTab(0)
    }

    /**
     * 改变底部tabUI
     *
     * @param position 当前位置
     */
    private fun changeTab(position: Int) {
        tab_main_btn.isSelected = false
        tab_main_tv.isSelected = false
        tab_discovery_btn.isSelected = false
        tab_discovery_tv.isSelected = false
        tab_video_btn.isSelected = false
        tab_video_tv.isSelected = false
        tab_singer_btn.isSelected = false
        tab_singer_tv.isSelected = false
        currentPage = position

        when (position) {
            0 -> {
                tab_main_btn.isSelected = true
                tab_main_tv.isSelected = true
                tv_title.text = getString(R.string.app_name)
            }
            1 -> {
                tab_discovery_tv.isSelected = true
                tab_discovery_btn.isSelected = true
                tv_title.text = getString(R.string.discovery_tab)
            }
            2 -> {
                tab_video_btn.isSelected = true
                tab_video_tv.isSelected = true
                tv_title.text = getString(R.string.video_tab)

            }
            3 -> {
                tab_singer_btn.isSelected = true
                tab_singer_tv.isSelected = true
                tv_title.text = getString(R.string.mine)
            }
        }
        view_pager.currentItem = position

    }


    @OnClick(
        R.id.tab_main_btn,
        R.id.tab_main_tv,
        R.id.tab_discovery_btn,
        R.id.tab_discovery_tv,
        R.id.tab_video_btn,
        R.id.tab_video_tv,
        R.id.tab_singer_btn,
        R.id.tab_singer_tv,
        R.id.cover_container
    )
    fun onClick(view: View) {
        when (view.id) {
            R.id.tab_main_btn,
            R.id.tab_main_tv -> {
                changeTab(0)
            }
            R.id.tab_discovery_btn,
            R.id.tab_discovery_tv -> {
                changeTab(1)
            }
            R.id.tab_video_btn,
            R.id.tab_video_tv -> {
                changeTab(2)
            }
            R.id.tab_singer_btn,
            R.id.tab_singer_tv -> {
                changeTab(3)
            }
            R.id.cover_container -> {
                /*if (mPlayer != null && ::playList.isInitialized) {
                    MusicPlayActivity.startThisActivity(this@MainActivity)
                }*/
                ARouterUtils.go(RouterHub.MUSICDETAILACTIVITY)
                return
            }
        }
    }

    override fun onDestroy() {
        mPresenter?.unbindPlaybackService()
        super.onDestroy()
        BridgeProviders.instance.clear()
        exitProcess(0)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (SystemClock.elapsedRealtime() - mClickTime > 1000) {
                    Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_SHORT).show()
                    mClickTime = SystemClock.elapsedRealtime()
                    return true
                } else {
                    System.exit(0)
                }
            }
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onPlaybackServiceBound(service: PlayBackService) {
        mPlayer = service
    }

    override fun onPlaybackServiceUnbound() {
        mPlayer = null
    }

    override fun onSongUpdated(song: BaseMusic?) {
        if (song == null) {
            cover_container.setProgress(0)
            cover_container.cancelRotateAnimation()
            mHandler.removeCallbacks(mProgressCallback)
        }
        song?.let {
            val cover = if (it.picUrl.isEmpty()) {
                it.song?.picUrl
            } else {
                it.picUrl
            }
            cover_container.startRotateAnimation()
            cover_container.setImageBitmap(cover ?: "")
            cover_container.setProgress(initProgress(mPlayer!!.getProgress()))
            mHandler.post(mProgressCallback)

        }
    }

    override fun onSongPlay() {
        mHandler.post(mProgressCallback)
        cover_container.resumeRotateAnimation()
    }

    override fun onSongPause() {
        mHandler.removeCallbacks(mProgressCallback)
        cover_container.pauseRotateAnimation()
    }

    override fun updatePlayMode(playMode: PlayMode) {
        TODO("Not yet implemented")
    }

    override fun onMusicDelete() {

    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

}