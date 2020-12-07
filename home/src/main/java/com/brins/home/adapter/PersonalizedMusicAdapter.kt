package com.brins.home.adapter

import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.module.*
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.home.R
import com.brins.baselib.module.MusicList
import com.brins.baselib.utils.convertNumMillion
import com.brins.bridgelib.musiclist.MusicListBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.networklib.model.personal.PersonalizedMusic
import com.brins.networklib.model.personal.PersonalizedMusicList
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder

/**
 * @author lipeilin
 * @date 2020/7/22
 */
class PersonalizedMusicAdapter(data: MutableList<BaseData>) :
    BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>(data) {

    init {
        addItemType(ITEM_HOME_PERSONALIZED, R.layout.home_item_personalized_music)
        addItemType(ITEM_HOME_PERSONALIZED_MUSIC, R.layout.home_item_personalized_music)
        addItemType(ITEM_HOME_TOP_RECOMMEND, R.layout.home_item_personalized_music)
    }

    private var mListener: OnMusicClickListener? = null

    fun setListener(listener: OnMusicClickListener) {
        this.mListener = listener
    }

    interface OnMusicClickListener {
        fun onMusicClick(music: BaseMusic, position: Int)
    }

    override fun convert(helper: BaseViewHolder, item: BaseData) {
        when (helper.itemViewType) {
            ITEM_HOME_PERSONALIZED -> {
                helper.setText(R.id.name, (item as PersonalizedMusicList).name)
                helper.setText(R.id.playCount, "${convertNumMillion(item.playCount.toLong())}")
                val image: ImageView = helper.getView(R.id.cover)
                ViewCompat.setTransitionName(image, item.picUrl)
                GlideHelper.setRoundImageResource(image, item.picUrl, 10)

                helper.getView<ConstraintLayout>(R.id.rootLayout).setOnClickListener {

                    val bundle = Bundle()
                    bundle.putString(KEY_ID, item.id)
                    BridgeProviders.instance.getBridge(MusicListBridgeInterface::class.java)
                        .toMusicListActivity(bundle)
                    /*val options: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            activity,
                            image,
                            ViewCompat.getTransitionName(image)!!
                        )
                    MusicDetailActivity.startThis(
                        activity as AppCompatActivity,
                        options,
                        image.transitionName,
                        item.id
                    )*/

                }
            }
            ITEM_HOME_PERSONALIZED_MUSIC -> {
                helper.setText(R.id.name, (item as PersonalizedMusic).name)
                helper.setGone(R.id.playCount, true)
                helper.setVisible(R.id.iv_play, true)
                val image: ImageView = helper.getView(R.id.cover)
                ViewCompat.setTransitionName(image, item.picUrl)
                GlideHelper.setRoundImageResource(image, item.picUrl, 10)
                helper.getView<ImageView>(R.id.iv_play).apply {
                    setOnClickListener {
                        mListener?.onMusicClick(item, helper.adapterPosition)
                    }
                    when (item.playStatus) {
                        MusicStatus.FIRST_PLAY, MusicStatus.PAUSE -> setImageResource(R.drawable.home_ic_play)
                        MusicStatus.RESUME -> setImageResource(R.drawable.home_ic_pause)
                    }
                }

            }

            ITEM_HOME_TOP_RECOMMEND -> {
                helper.setText(R.id.name, (item as MusicList).name)
                helper.setText(R.id.playCount, "${convertNumMillion(item.playCount)}")
                val image: ImageView = helper.getView(R.id.cover)
                ViewCompat.setTransitionName(image, item.coverImgUrl)
                GlideHelper.setRoundImageResource(image, item.coverImgUrl, 10)

                helper.getView<ConstraintLayout>(R.id.rootLayout).setOnClickListener {

                    val bundle = Bundle()
                    bundle.putString(KEY_ID, item.id)
                    BridgeProviders.instance.getBridge(MusicListBridgeInterface::class.java)
                        .toMusicListActivity(bundle)
                }
            }
        }

    }
}