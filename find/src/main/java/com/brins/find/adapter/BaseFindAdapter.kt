package com.brins.find.adapter

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.baselib.config.KEY_EVENT_DATA
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.module.*
import com.brins.baselib.utils.*
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.*
import com.brins.bridgelib.event.EventDetailBridgeInterface
import com.brins.bridgelib.picturedetail.PictureDetailBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.find.R
import com.brins.find.widget.ImageTextView
import com.brins.networklib.model.event.EventData
import com.brins.networklib.model.title.SingleTitleData2
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder

/**
 * Created by lipeilin
 * on 2020/12/2
 */
class BaseFindAdapter(
    data: MutableList<BaseData>
) : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>(data) {

    private val mCollapsedStatus: SparseBooleanArray = SparseBooleanArray()

    init {
        addItemType(ITEM_FIND_FOLLOW, R.layout.find_item_my_follow)
        addItemType(ITEM_MINE_SINGLE_TITLE, R.layout.find_item_single_title)
        addItemType(ITEM_MINE_EVENT_DATA, R.layout.find_item_event)
    }


    override fun convert(helper: BaseViewHolder, item: BaseData) {

        when (item.itemType) {
            ITEM_MINE_EVENT_DATA -> {
                GlideHelper.setImageResource(
                    helper.getView<CircleImageView>(R.id.iv_avatar),
                    (item as EventData).user?.avatarUrl
                )
                helper.setText(R.id.tv_name, createNickName(item))
                helper.setText(R.id.tv_date, getDateToString(item.eventTime))
                dealJsonData(item)
                helper.getView<ExpandableTextView>(R.id.et_root)
                    .setText(item.jsonData?.msg, mCollapsedStatus, helper.adapterPosition)

                val relativeLayout = helper.getView<RelativeLayout>(R.id.rl_item_root)
                if (relativeLayout.childCount > 5) {
                    relativeLayout.removeViewAt(relativeLayout.childCount - 1)
                }
                val view = createShareContent(item, helper.adapterPosition)
                view?.let {
                    val params = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.addRule(RelativeLayout.BELOW, R.id.mi_event_images)
                    params.addRule(RelativeLayout.END_OF, R.id.iv_avatar)
                    params.topMargin = dp2px(8f)
                    it.layoutParams = params
                    relativeLayout.addView(it)
                }

                item.pics?.let {
                    val list = mutableListOf<String>()
                    it.forEach { image ->
                        list.add(image.squareUrl)
                    }
                    helper.getView<MultiImageView>(R.id.mi_event_images)
                        .setOnItemClickListener(object : MultiImageView.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                toDetailPictureActivity(it, position)
                            }

                        })
                    helper.getView<MultiImageView>(R.id.mi_event_images).setList(list)
                }

                item.info?.let {
                    helper.getView<ImageTextView>(R.id.itv_share)
                        .setText(convertNum(it.shareCount.toLong()))
                    helper.getView<ImageTextView>(R.id.itv_comment)
                        .setText(convertNum(it.commentCount.toLong()))
                    helper.getView<ImageTextView>(R.id.itv_like)
                        .setText(convertNum(it.likedCount.toLong()))
                        .activate(it.liked)
                        .setOnClickListener { view ->
                            if (it.liked) {
                                (view as ImageTextView).unlikeEvent(it.threadId) { result ->
                                    it.liked = result
                                    it.likedCount -= 1
                                    view.setText(convertNum(it.likedCount.toLong()))
                                }
                            } else {
                                (view as ImageTextView).likeEvent(it.threadId) { result ->
                                    it.liked = result
                                    it.likedCount += 1
                                    view.setText(convertNum(it.likedCount.toLong()))
                                }
                            }
                        }

                }


                helper.getView<ConstraintLayout>(R.id.cl_item_root).setOnClickListener {
                    toEventDetailActivity(item)
                }
                helper.getView<ExpandableTextView>(R.id.et_root)
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
            }
            ITEM_FIND_SINGLE_TITLE -> {
                val textView = helper.getView<TextView>(R.id.item_text_view)
                (item as SingleTitleData2).apply {
                    textView.text = this.getTitle()
                }
                textView.setPadding(
                    dp2px(15f),
                    dp2px(15f), 0,
                    dp2px(15f)
                )
            }
        }
    }

    private fun createNickName(item: EventData): SpannableStringBuilder? {
        var sub = when (item.type) {
            18 -> context.getString(R.string.share_song)
            19 -> context.getString(R.string.share_album)
            17, 28 -> context.getString(R.string.share_radio)
            22 -> context.getString(R.string.forward)
            39 -> context.getString(R.string.publish_video)
            13 -> context.getString(R.string.share_music_list)
            24 -> context.getString(R.string.share_article)
            41, 21 -> context.getString(R.string.share_video)
            else -> ""
        }
        val strBuilder = SpanUtils().append(item.user?.nickname ?: "")
            .setForegroundColor(UIUtils.getColor(R.color.blue_4f7daf))
            .append(" ")
            .append(sub)
            .setForegroundColor(UIUtils.getColor(R.color.default_btn_text)).create()
        return strBuilder

    }

    /**
     * 处理jsonData
     *
     * @param item
     */
    private fun dealJsonData(item: EventData) {
        if (item.jsonData == null) {
            val jsonData: EventData.EventJson = GsonUtils.fromJson<EventData.EventJson>(
                item.json,
                EventData.EventJson::class.java
            )
            item.jsonData = jsonData
        } else {
            return
        }
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
            LayoutInflater.from(context).inflate(R.layout.find_item_event, null, false)
        rootview.findViewById<RelativeLayout>(R.id.rl_item_root)
            .background = (UIUtils.getDrawable(R.drawable.base_bg_15dp_f2f2f2))
        rootview.findViewById<ImageView>(R.id.iv_avatar).visibility = View.GONE
        rootview.findViewById<LinearLayout>(R.id.ll_share_root).visibility = View.GONE
        rootview.findViewById<TextView>(R.id.tv_name).visibility = View.GONE
        rootview.findViewById<TextView>(R.id.tv_date).visibility = View.GONE
        dealJsonData(item)
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
            params.addRule(RelativeLayout.END_OF, R.id.iv_avatar)
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
     * 创建分享专辑
     *
     * @param item
     * @return
     */
    private fun createShareAlbum(item: EventData): ShareMusicListView? {
        var view: ShareMusicListView? = null
        item.jsonData?.let {
            if (it.album != null) {
                view = ShareMusicListView(context)
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
                view = ShareMusicListView(context)
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
                view = ShareMusicListView(context)
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

}