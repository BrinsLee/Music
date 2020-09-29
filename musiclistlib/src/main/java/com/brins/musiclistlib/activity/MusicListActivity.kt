package com.brins.musiclistlib.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE
import com.brins.baselib.route.RouterHub.Companion.MUSICLISTACTIVITY
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.UIUtils.getScreenWidth
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.utils.handleNum
import com.brins.baselib.utils.setTranslucent
import com.brins.baselib.widget.CommonHeaderView
import com.brins.musiclistlib.R
import com.brins.musiclistlib.adapter.MusicListAdapter
import com.brins.musiclistlib.contract.MusicListContract
import com.brins.musiclistlib.presenter.MusicListPresenter
import com.brins.musiclistlib.widget.StickNavLayout
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.musiclist.MusicList
import com.brins.networklib.model.musiclist.MusicListResult
import kotlinx.android.synthetic.main.activity_music_list.*

@Route(path = MUSICLISTACTIVITY)
class MusicListActivity : BaseMvpActivity<MusicListPresenter>(), MusicListContract.View,
    StickNavLayout.MyStickyListener {

    @Autowired(name = "KEY_ID")
    lateinit var id: String
    private var mAdapter: MusicListAdapter? = null

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

    override fun setStatusBar() {
        setTranslucent(this)
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
                                    tv.text = "${handleNum(list.subscribedCount)}w关注"
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
            val list = mutableListOf<BaseData>()
            list.add(object : BaseData() {
                override val itemType: Int
                    get() = ITEM_HOME_SINGLE_TITLE
            })
            list.addAll(it.playlist?.tracks!!)
            mAdapter = MusicListAdapter(list)
            musicRecycler.adapter = mAdapter
            val manager = LinearLayoutManager(this)
            manager.isSmoothScrollbarEnabled = true
            musicRecycler.setHasFixedSize(true)
            musicRecycler.isNestedScrollingEnabled = true
            musicRecycler.layoutManager = manager
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

/*    override fun setStatusBar() {
        StatusBarHelper.getInstance().setWindowTranslucentStatus(this)
    }*/
}