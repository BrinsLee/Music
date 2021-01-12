package com.brins.eventdetaillib.activity

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.RelativeLayout
import androidx.core.app.ActivityOptionsCompat
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.config.KEY_EVENT_DATA
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.config.TRANSITION_IMAGE
import com.brins.baselib.route.RouterHub.Companion.EVENTDETAILACTIVITY
import com.brins.baselib.utils.SizeUtils
import com.brins.baselib.utils.SpanUtils
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.getDateToString
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.MultiImageView
import com.brins.baselib.widget.ShareMusicListView
import com.brins.bridgelib.picturedetail.PictureDetailBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.eventdetaillib.R
import com.brins.eventdetaillib.presenter.EventDetailPresenter
import com.brins.networklib.model.event.EventData
import kotlinx.android.synthetic.main.activity_event_detail.*

@Route(path = EVENTDETAILACTIVITY)
class EventDetailActivity : BaseMvpActivity<EventDetailPresenter>() {

    @Autowired(name = KEY_EVENT_DATA)
    lateinit var mEvent: EventData

    override fun getLayoutResId(): Int {
        return R.layout.activity_event_detail
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        toolbar.setContentInsetsAbsolute(0, 0)
        iv_return.setOnClickListener {
            finish()
        }
        if (::mEvent.isInitialized) {
            GlideHelper.setImageResource(
                iv_avatar,
                mEvent.user?.avatarUrl
            )

            tv_name.setText(createNickName(mEvent))
            tv_date.setText(getDateToString(mEvent.eventTime))
            et_root.setText(mEvent.jsonData?.msg)
            val view = createShareContent(mEvent)
            view?.let {
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.BELOW, R.id.mi_event_images)
                params.addRule(RelativeLayout.END_OF, R.id.iv_avatar)
                params.topMargin = SizeUtils.dp2px(8f)
                it.layoutParams = params
                rl_event_detail.addView(it)
            }
            mEvent.pics?.let {
                val list = mutableListOf<String>()
                it.forEach { image ->
                    list.add(image.squareUrl)
                }
                mi_event_images.setList(list)
                mi_event_images.setOnItemClickListener(object : MultiImageView.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val bundle = Bundle()
                        bundle.putSerializable(KEY_EVENT_PICTURES, it)
                        bundle.putInt(KEY_EVENT_PICTURE_POS, position)
                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@EventDetailActivity,
                                view,
                                TRANSITION_IMAGE
                            )
                        BridgeProviders.instance.getBridge(PictureDetailBridgeInterface::class.java)
                            .toDetailPictureActivity(bundle, optionsCompat)
                    }

                })
            }

        }

    }

    private fun createShareContent(item: EventData): View? {
        when (item.type) {
            18 -> return createShareMusic(item)
            19 -> return createShareAlbum(item)
            13 -> return createShareMusicList(item)
            else -> return null
        }
        return null
    }

    /**
     * 创建分享专辑
     *
     * @param item
     * @return
     */
    private fun createShareAlbum(item: EventData): ShareMusicListView? {
        var view: ShareMusicListView? = null
        item.jsonData?.let {
            if (it.album != null) {
                view = ShareMusicListView(this)
                view!!.mMusicList.setText("专辑")
                view!!.mName.text = "${it.album?.artist?.name}"
                view!!.mTitle.text = it.album?.name
                GlideHelper.setRoundImageResource(
                    view!!.mCover,
                    it.album?.picUrl,
                    5,
                    R.drawable.base_icon_default_cover,
                    100,
                    100
                )
            }
        }
        return view
    }

    /**
     * 创建分享单曲布局
     *
     * @param item
     * @return
     */
    private fun createShareMusic(item: EventData): ShareMusicListView? {
        var view: ShareMusicListView? = null
        item.jsonData?.let {
            if (it.song != null) {
                view = ShareMusicListView(this)
                view!!.mName.text = "${it.song!!.artists?.get(0)?.name}"
                view!!.mTitle.text = it.song!!.name
                view!!.mMusicList.visibility = View.GONE
                GlideHelper.setRoundImageResource(
                    view!!.mCover,
                    it.song!!.song?.picUrl,
                    5,
                    R.drawable.base_icon_default_cover,
                    100,
                    100
                )
            }
        }
        return view
    }

    /**
     * 创建分享歌单布局
     *
     * @param item
     * @return
     */
    private fun createShareMusicList(item: EventData): ShareMusicListView? {
        var view: ShareMusicListView? = null
        item.jsonData?.let {
            if (it.playlist != null) {
                view = ShareMusicListView(this)
                view!!.mName.text = "by ${it.playlist?.creator?.nickname}"
                view!!.mTitle.text = it.playlist?.name
                GlideHelper.setRoundImageResource(
                    view!!.mCover,
                    it.playlist?.coverImgUrl,
                    5,
                    R.drawable.base_icon_default_cover,
                    100,
                    100
                )
            }
        }
        return view
    }

    private fun createNickName(item: EventData): SpannableStringBuilder? {
        var sub = when (item.type) {
            18 -> getString(R.string.share_song)
            19 -> getString(R.string.share_album)
            17, 28 -> getString(R.string.share_radio)
            22 -> getString(R.string.forward)
            39 -> getString(R.string.publish_video)
            13 -> getString(R.string.share_music_list)
            24 -> getString(R.string.share_article)
            41, 21 -> getString(R.string.share_video)
            else -> ""
        }
        val strBuilder = SpanUtils().append(item.user?.nickname ?: "")
            .setForegroundColor(UIUtils.getColor(R.color.blue_4f7daf))
            .append(" ")
            .append(sub)
            .setForegroundColor(UIUtils.getColor(R.color.default_btn_text)).create()
        return strBuilder

    }


}