package com.brins.musiclistlib.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.cache.like.LikeCache
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE
import com.brins.baselib.route.RouterHub.Companion.MUSICLISTACTIVITY
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.UIUtils.getScreenWidth
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.CommonHeaderView
import com.brins.musiclistlib.R
import com.brins.musiclistlib.adapter.MusicListAdapter
import com.brins.musiclistlib.contract.MusicListContract
import com.brins.musiclistlib.presenter.MusicListPresenter
import com.brins.musiclistlib.widget.StickNavLayout
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.musiclist.MoreMusicListResult
import com.brins.baselib.module.MusicList
import com.brins.baselib.utils.convertNumMillion
import com.brins.baselib.widget.StickHeaderDecoration
import com.brins.networklib.model.musiclist.MusicListResult
import kotlinx.android.synthetic.main.activity_music_list.*
import kotlin.math.min

@Route(path = MUSICLISTACTIVITY)
class MusicListActivity : BaseMvpActivity<MusicListPresenter>(), MusicListContract.View,
    StickNavLayout.MyStickyListener {

    @Autowired(name = KEY_ID)
    lateinit var id: String

    private var mAdapter: MusicListAdapter? = null

    private var mCurrentIndex = 0

    override fun getLayoutResId(): Int {
        return R.layout.activity_music_list
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)


        //解决toolbar左边距问题
        toolbar.setContentInsetsAbsolute(0, 0)

        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
//        stickynavlayout.setListener(this)
        if (id.isNotEmpty()) {
            showLoading()
            launch({
                mPresenter?.loadMusicListDetail(id)
            }, {})
        }
    }


    override fun onMusicDetailLoad(data: MusicListResult?) {
        hideLoading()
        data?.let {
            it.playlist?.let { list ->
                loadCover(list)
                description.text = list.description

                val frameLayout = FrameLayout(this)
                val pa: ConstraintLayout.LayoutParams =
                    ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )

                pa.bottomToBottom = R.id.coverMusicList
                pa.startToStart = R.id.avatar
                frameLayout.layoutParams = pa
                container_header.addView(frameLayout)

                if (list.subscribers.isNotEmpty()) {
                    for (i in 0 until list.subscribers.size) {
                        if (i < 3) {

                            val avatar = ImageView(this)
                            avatar.background =
                                UIUtils.getDrawable(R.drawable.musiclist_bg_circle_30dp)
                            avatar.setPadding(2, 2, 2, 2)
                            GlideHelper.setCircleImageResource(
                                avatar,
                                list.subscribers[i].avatarUrl
                            )
                            frameLayout.addView(avatar)
                            val layoutParams: FrameLayout.LayoutParams =
                                FrameLayout.LayoutParams(dp2px(30f), dp2px(30f))
                            when (i) {
                                0 -> {
                                    layoutParams.marginStart = dp2px(0f)
                                }
                                1 -> {
                                    layoutParams.marginStart = dp2px(20f)
                                }
                                2 -> {
                                    layoutParams.marginStart = dp2px(40f)
                                    val tv = TextView(this)
                                    tv.setTextColor(Color.WHITE)
                                    tv.textSize = 12f
                                    tv.text =
                                        "${convertNumMillion(list.subscribedCount.toLong())} 关注"
                                    tv.gravity = Gravity.CENTER_VERTICAL
                                    frameLayout.addView(tv)
                                    val params: FrameLayout.LayoutParams =
                                        FrameLayout.LayoutParams(
                                            FrameLayout.LayoutParams.WRAP_CONTENT,
                                            FrameLayout.LayoutParams.WRAP_CONTENT
                                        )
                                    params.gravity = Gravity.CENTER_VERTICAL
                                    params.marginStart = dp2px(80f)
                                    tv.layoutParams = params
                                }
                            }
                            avatar.layoutParams = layoutParams
                        } else {
                            break
                        }
                    }

                }

            }

//            stickynavlayout.bringToFront()
            bindRecyclerViewAdapter(it)
        }
    }

    override fun onMoreMusicDetailLoad(data: MoreMusicListResult?) {
        val list = mutableListOf<BaseData>()
        data?.songs?.let {
            list.addAll(it)
            mAdapter?.addData(list)
            mAdapter?.loadMoreModule?.isEnableLoadMore = true
        }
    }

    private fun loadCover(it: MusicList) {
        GlideHelper.setBlurImageResource(
            cover,
            it.backgroundCoverUrl ?: it.coverImgUrl
        )
        GlideHelper.setRoundImageResource(coverMusicList, it.coverImgUrl, 8)
        GlideHelper.setCircleImageResource(avatar, it.creator?.avatarUrl)
    }

    /**
     *
     * @param it
     */
    private fun bindRecyclerViewAdapter(it: MusicListResult) {
        if (it.playlist?.tracks != null) {
            mCurrentIndex = it.playlist!!.tracks.size
            LikeCache.likeMusicList?.trackIds = it.playlist?.trackIds!!
            val list = mutableListOf<BaseData>()
            list.add(object : BaseData() {
                override val itemType: Int
                    get() = ITEM_HOME_SINGLE_TITLE
            })
            list.addAll(it.playlist?.tracks!!)
            mAdapter = MusicListAdapter(list)
            mAdapter?.loadMoreModule?.setOnLoadMoreListener {
                loadMore(it)
            }
            mAdapter?.loadMoreModule?.isAutoLoadMore = true
            mAdapter?.loadMoreModule?.isEnableLoadMoreIfNotFullPage = false
            musicRecycler.adapter = mAdapter
            val manager = LinearLayoutManager(this)
            manager.isSmoothScrollbarEnabled = true
            musicRecycler.setHasFixedSize(true)
            musicRecycler.isNestedScrollingEnabled = true
            musicRecycler.layoutManager = manager
            musicRecycler.addItemDecoration(StickHeaderDecoration(this))
        }
    }

    private fun loadMore(it: MusicListResult) {
        mAdapter?.loadMoreModule?.isEnableLoadMore = false
        if (it.playlist?.trackCount!! > mCurrentIndex) {

            val loadMore = min(10, it.playlist!!.trackCount - mCurrentIndex)
            val loadMores = arrayListOf<String>()
            for (i in 0 until loadMore) {
                loadMores.add(it.playlist!!.trackIds[mCurrentIndex].id)
                mCurrentIndex++
            }
            launch({
                mPresenter?.loadMoreMusicListDetail(loadMores)
            }, {
                ToastUtils.show("加载失败，请重试", Toast.LENGTH_SHORT)
                mAdapter?.loadMoreModule?.isEnableLoadMore = true
            })
        } else {
            mAdapter?.loadMoreModule?.isEnableLoadMore = false
        }
    }


    override fun imageScale(bottom: Float) {
        val b = bottom + dp2px(20f)
        val height: Float = dp2px(270f).toFloat()
        val mScale: Float = b / height
        val screenWidth = getScreenWidth(this)
        val width: Float = screenWidth * mScale
        val dx: Float = (width - screenWidth) / 2
        cover.layout((0 - dx).toInt(), 0, (screenWidth + dx).toInt(), b.toInt())
    }

}