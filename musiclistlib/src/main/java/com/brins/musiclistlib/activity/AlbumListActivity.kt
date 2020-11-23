package com.brins.musiclistlib.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.config.KEY_COMMEND_PATH
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.config.MUSIC_COMMENT.Companion.ALBUM_COMMENT
import com.brins.baselib.module.BaseData
import com.brins.baselib.route.RouterHub.Companion.ALBUMLISTACTIVITY
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.utils.handleNum
import com.brins.baselib.utils.setTranslucent
import com.brins.baselib.widget.CommonHeaderView
import com.brins.bridgelib.musicdetail.MusicDetailBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.musiclistlib.R
import com.brins.musiclistlib.adapter.MusicListAdapter
import com.brins.musiclistlib.contract.AlbumContract
import com.brins.musiclistlib.presenter.AlbumPresenter
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.model.album.AlbumListResult
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_album_list.*
import kotlinx.android.synthetic.main.activity_album_list.header
import kotlinx.android.synthetic.main.activity_album_list.toolbar
import kotlin.math.abs

@Route(path = ALBUMLISTACTIVITY)
class AlbumListActivity : BaseMvpActivity<AlbumPresenter>(), AlbumContract.View {

    @Autowired(name = "KEY_ID")
    lateinit var id: String
    private var mAdapter: MusicListAdapter? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_album_list
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.setContentInsetsAbsolute(0, 0)
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        header.setTitleAlpha(0f)
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val offset: Float = abs(verticalOffset * 1.0f) / appBarLayout.totalScrollRange
            iv_cover.alpha = (1 - offset)
            tv_playlist_name.alpha = (1 - offset)
            header.setTitleAlpha(offset)
        })
        if (id.isNotEmpty()) {
            showLoading()
            ApiHelper.launch({
                mPresenter?.loadAlbumDetail(id)
            }, {})
        }
        rl_comment.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(KEY_ID, id)
            bundle.putString(KEY_COMMEND_PATH, ALBUM_COMMENT)
            BridgeProviders.instance.getBridge(MusicDetailBridgeInterface::class.java)
                .toCommentsActivity(bundle)
        }
    }

    override fun setStatusBar() {
        setTranslucent(this)
    }

    override fun onAlbumDetailLoad(data: AlbumListResult?) {
        data?.let {
            hideLoading()
            tv_playlist_name.setText(it.album?.name)
            header.initView(it.album?.name)
            tv_comment.text = handleNum(it.album?.info?.commentCount ?: 0)
            tv_share.text = handleNum(it.album?.info?.shareCount ?: 0)
            GlideHelper.setRoundImageResource(
                iv_cover,
                it.album?.picUrl,
                10,
                R.drawable.base_icon_default_cover
            )
            GlideHelper.setBlurImageResource(iv_cover_bg, it.album?.blurPicUrl, 25f)

            if (!it.songs.isNullOrEmpty()) {
                val list = mutableListOf<BaseData>()
                list.addAll(it.songs!!)
                mAdapter = MusicListAdapter(list)
                rv_song.adapter = mAdapter
                val manager = LinearLayoutManager(this)
                manager.isSmoothScrollbarEnabled = true
                rv_song.setHasFixedSize(true)
                rv_song.isNestedScrollingEnabled = true
                rv_song.layoutManager = manager
            }
        }
    }
}