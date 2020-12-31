package com.brins.find.adapter

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.SparseBooleanArray
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.config.TRANSITION_IMAGE
import com.brins.baselib.module.*
import com.brins.baselib.utils.*
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.*
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
                if (item.jsonData == null) {
                    val jsonData: EventData.EventJson = GsonUtils.fromJson<EventData.EventJson>(
                        item.json,
                        EventData.EventJson::class.java
                    )
                    helper.getView<ExpandableTextView>(R.id.et_root)
                        .setText(jsonData.msg, mCollapsedStatus, helper.adapterPosition)
                    /*helper.getView<RichTextView>(R.id.tv_content)
                        .setRichText(
                            jsonData.msg,
                            getUserModel(jsonData.msg),
                            getTopicModel(jsonData.msg)
                        )*/
                    item.jsonData = jsonData
                } else {
                    helper.getView<ExpandableTextView>(R.id.et_root)
                        .setText(item.jsonData?.msg, mCollapsedStatus, helper.adapterPosition)
                    /*helper.getView<RichTextView>(R.id.tv_content)
                        .setRichText(
                            item.jsonData?.msg, getUserModel(item.jsonData?.msg),
                            getTopicModel(item.jsonData?.msg)
                        )*/

                }

                val relativeLayout = helper.getView<RelativeLayout>(R.id.rl_item_root)
                if (relativeLayout.childCount > 5) {
                    relativeLayout.removeViewAt(relativeLayout.childCount - 1)
                }
                val view = createShareContent(item)
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
                                val bundle = Bundle()
                                bundle.putSerializable(KEY_EVENT_PICTURES, it)
                                bundle.putInt(KEY_EVENT_PICTURE_POS, position)
                                val optionsCompat: ActivityOptionsCompat =
                                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        context as Activity,
                                        view,
                                        TRANSITION_IMAGE
                                    )
                                BridgeProviders.instance.getBridge(PictureDetailBridgeInterface::class.java)
                                    .toDetailPictureActivity(bundle, optionsCompat)
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
            35, 13 -> context.getString(R.string.share_music_list)
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

    private fun createShareContent(item: EventData): View? {
        when (item.type) {
            18 -> return createShareMusic(item)
            35, 13 -> return createShareMusicList(item)
            else -> return null
        }
        return null
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