package com.brins.radiolib.adapter

import android.widget.TextView
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_MINE_SINGLE_TITLE
import com.brins.baselib.module.ITEM_RADIO_RECOMMEND_DATA
import com.brins.baselib.module.ITEM_RADIO_SINGLE_TITLE
import com.brins.baselib.utils.SizeUtils
import com.brins.networklib.model.title.SingleTitleData2
import com.brins.radiolib.R
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class BaseRadioAdapter(data: MutableList<BaseData>) :
    BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>(data) {

    init {
        addItemType(ITEM_MINE_SINGLE_TITLE, R.layout.radio_item_single_title)
        addItemType(ITEM_RADIO_RECOMMEND_DATA, R.layout.radio_item_recommend)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        when (item.itemType) {
            ITEM_MINE_SINGLE_TITLE -> {

                helper.getView<TextView>(R.id.item_text_view).setPadding(
                    SizeUtils.dp2px(15f),
                    0, 0,
                    0
                )
                helper.setText(R.id.item_text_view, (item as SingleTitleData2).getTitle())
            }

            ITEM_RADIO_RECOMMEND_DATA -> {

            }
        }
    }
}