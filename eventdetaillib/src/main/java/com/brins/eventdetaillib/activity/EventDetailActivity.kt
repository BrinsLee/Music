package com.brins.eventdetaillib.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.config.KEY_EVENT_DATA
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.config.KEY_EVENT_THREADID
import com.brins.baselib.route.RouterHub.Companion.EVENTDETAILACTIVITY
import com.brins.baselib.utils.SizeUtils
import com.brins.baselib.utils.SpanUtils
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.getDateToString
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.ExpandableTextView
import com.brins.baselib.widget.MultiImageView
import com.brins.baselib.widget.ShareMusicListView
import com.brins.bridgelib.event.EventDetailBridgeInterface
import com.brins.bridgelib.picturedetail.PictureDetailBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.eventdetaillib.R
import com.brins.eventdetaillib.presenter.EventDetailPresenter
import com.brins.networklib.model.event.EventData
import kotlinx.android.synthetic.main.activity_event_detail.*
import net.lucode.hackware.magicindicator.FragmentContainerHelper
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

@Route(path = EVENTDETAILACTIVITY)
class EventDetailActivity : BaseMvpActivity<EventDetailPresenter>() {

    @Autowired(name = KEY_EVENT_DATA)
    lateinit var mEvent: EventData
    private var mTitleList: ArrayList<String> = arrayListOf(
        "评论",
        "转发",
        "赞"
    )
    private lateinit var mAdapter: EventCommentPageAdapter

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
            val mList = ArrayList<Fragment>()
            val bundle = Bundle()
            bundle.putString(KEY_EVENT_THREADID, mEvent.info?.threadId)
            mList.add(
                BridgeProviders.instance
                    .getBridge(EventDetailBridgeInterface::class.java)
                    .getEventCommentFragment(bundle)
            )
            mList.add(Fragment())
            mList.add(Fragment())
            mAdapter = EventCommentPageAdapter(supportFragmentManager, mList)
            GlideHelper.setImageResource(
                iv_avatar,
                mEvent.user?.avatarUrl
            )

            tv_name.setText(createNickName(mEvent))
            tv_date.setText(getDateToString(mEvent.eventTime))
            et_root.setText(mEvent.jsonData?.msg)
            val view = createShareContent(mEvent, 0)
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
                        toDetailPictureActivity(it, position)
                    }

                })
            }
            initNavigator()
        }

    }

    private fun createTabTitle(string: String, commentCount: Int): SpannableStringBuilder? {
        val stringBuilder = SpanUtils().append(string)
            .append("$commentCount").setSpans(
                ForegroundColorSpan(UIUtils.getColor(R.color.default_btn_text)),
                AbsoluteSizeSpan(12, true)
            ).create()
        return stringBuilder

    }

    private fun initNavigator() {
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView: SimplePagerTitleView =
                    ColorTransitionPagerTitleView(context)
                simplePagerTitleView.textSize = 15f
                when (index) {
                    0 -> simplePagerTitleView.text =
                        createTabTitle(mTitleList[index], mEvent.info?.commentCount ?: 0)
                    1 -> simplePagerTitleView.text =
                        createTabTitle(mTitleList[index], mEvent.info?.shareCount ?: 0)
                    2 -> simplePagerTitleView.text =
                        createTabTitle(mTitleList[index], mEvent.info?.likedCount ?: 0)
                }

                simplePagerTitleView.normalColor = UIUtils.getColor(R.color.default_btn_text)
                simplePagerTitleView.selectedColor = UIUtils.getColor(R.color.black)
                simplePagerTitleView.setOnClickListener {
                    vp_event_comment.currentItem = index
                }
                return simplePagerTitleView

            }

            override fun getCount(): Int {
                return mTitleList.size
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_MATCH_EDGE
                linePagerIndicator.lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
                linePagerIndicator.setColors(Color.BLACK)
                return linePagerIndicator
            }

        }
        magicIndicator_event_comment.navigator = commonNavigator
        val titleContainer = commonNavigator.titleContainer
        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return UIUtil.dip2px(mContext, 15.0)
            }
        }
        val fragmentContainerHelper =
            FragmentContainerHelper(magicIndicator_event_comment)
        fragmentContainerHelper.setInterpolator(OvershootInterpolator(2.0f))
        fragmentContainerHelper.setDuration(300)
        vp_event_comment.adapter = mAdapter
        ViewPagerHelper.bind(magicIndicator_event_comment, vp_event_comment)
    }

    private fun createShareContent(item: EventData, pos: Int): View? {
        when (item.type) {
            18 -> return createShareMusic(item)
            19 -> return createShareAlbum(item)
            13 -> return createShareMusicList(item)
            22 -> return createForward(item.jsonData!!.event!!, pos)
            else -> return null
        }
        return null
    }

    /**
     * 创建转发原动态
     *
     * @param item
     * @return
     */
    private fun createForward(item: EventData, pos: Int): View? {
        var rootview =
            LayoutInflater.from(this).inflate(R.layout.event_detail_item_event, null, false)
        rootview.findViewById<RelativeLayout>(R.id.rl_item_root)
            .background = (UIUtils.getDrawable(R.drawable.base_bg_15dp_f2f2f2))
        rootview.findViewById<ExpandableTextView>(R.id.et_root)
            .setText(item.jsonData?.msg, SparseBooleanArray(), pos)
        val relativeLayout = rootview.findViewById<RelativeLayout>(R.id.rl_item_root)
        if (relativeLayout.childCount > 5) {
            relativeLayout.removeViewAt(relativeLayout.childCount - 1)
        }
        val view = createShareContent(item, pos)
        view?.let {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.BELOW, R.id.mi_event_images)
            params.topMargin = SizeUtils.dp2px(8f)
            it.layoutParams = params
            relativeLayout.addView(it)
        }
        item.pics?.let {
            val list = mutableListOf<String>()
            it.forEach { image ->
                list.add(image.squareUrl)
            }
            rootview.findViewById<MultiImageView>(R.id.mi_event_images)
                .setOnItemClickListener(object : MultiImageView.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        toDetailPictureActivity(it, position)
                    }

                })
            rootview.findViewById<MultiImageView>(R.id.mi_event_images).setList(list)
        }

        rootview.findViewById<ConstraintLayout>(R.id.cl_item_root).setOnClickListener {
            toEventDetailActivity(item)
        }
        rootview.findViewById<ExpandableTextView>(R.id.et_root)
            .setOnExpandStateChangeListener(object :
                ExpandableTextView.OnExpandStateChangeListener {
                override fun onTextClick() {
                    toEventDetailActivity(item)
                }

                override fun onExpandStateChanged(
                    textView: TextView?,
                    isExpanded: Boolean
                ) {
                }
            })

        return rootview
    }

    /**
     * 跳转动态详情页
     *
     * @param item
     */
    private fun toEventDetailActivity(item: EventData) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_EVENT_DATA, item)
        BridgeProviders.instance.getBridge(EventDetailBridgeInterface::class.java)
            .toEventDetailActivity(bundle)
    }

    /**
     * 跳转图片展示界面
     *
     * @param it
     * @param position
     */
    private fun toDetailPictureActivity(it: ArrayList<EventData.Image>, position: Int) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_EVENT_PICTURES, it)
        bundle.putInt(KEY_EVENT_PICTURE_POS, position)
        BridgeProviders.instance.getBridge(PictureDetailBridgeInterface::class.java)
            .toDetailPictureActivity(bundle)
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

    class EventCommentPageAdapter(
        manager: FragmentManager,
        private var mList: ArrayList<Fragment>
    ) :
        FragmentPagerAdapter(
            manager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {

        override fun getItem(position: Int): Fragment {
            return mList[position]
        }

        override fun getCount(): Int {
            return mList.size
        }

    }


}