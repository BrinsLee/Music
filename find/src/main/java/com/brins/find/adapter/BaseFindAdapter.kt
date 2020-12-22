package com.brins.find.adapter

import android.text.SpannableStringBuilder
import android.widget.TextView
import com.brins.baselib.module.*
import com.brins.baselib.utils.*
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.CircleImageView
import com.brins.baselib.widget.MultiImageView
import com.brins.find.R
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
                    helper.setText(
                        R.id.tv_content,
                        jsonData.msg
                    )
                    item.jsonData = jsonData
                } else {
                    helper.setText(
                        R.id.tv_content,
                        item.jsonData?.msg
                    )
                }

                item.pics?.let {
                    val list = mutableListOf<String>()
                    it.forEach { image ->
                        list.add(image.squareUrl)
                    }
                    helper.getView<MultiImageView>(R.id.mi_event_images).setList(list)
                }
            }
            ITEM_FIND_SINGLE_TITLE -> {
                val textView = helper.getView<TextView>(R.id.item_text_view)
                (item as SingleTitleData2).apply {
                    textView.text = this.getTitle()
                }
                textView.setPadding(
                    SizeUtils.dp2px(15f),
                    SizeUtils.dp2px(15f), 0,
                    SizeUtils.dp2px(15f)
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

}