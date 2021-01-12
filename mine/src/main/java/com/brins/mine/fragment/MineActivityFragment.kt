package com.brins.mine.fragment

import android.app.Activity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.util.SparseBooleanArray
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.config.KEY_EVENT_DATA
import com.brins.baselib.config.KEY_EVENT_PICTURES
import com.brins.baselib.config.KEY_EVENT_PICTURE_POS
import com.brins.baselib.config.TRANSITION_IMAGE
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_MINE_EVENT_DATA
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.*
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.CircleImageView
import com.brins.baselib.widget.ExpandableTextView
import com.brins.baselib.widget.MultiImageView
import com.brins.baselib.widget.ShareMusicListView
import com.brins.bridgelib.event.EventDetailBridgeInterface
import com.brins.bridgelib.picturedetail.PictureDetailBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.mine.R
import com.brins.mine.viewmodel.MineViewModel
import com.brins.mine.widget.ImageTextView
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.event.EventData
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_mine_activity.*


class MineActivityFragment : BaseMvvmFragment<MineViewModel>() {

    private var mEventDataObserver: Observer<MutableList<EventData>>? = null
    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MineViewModel.getInstance(mActivity!!.application)

    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_mine_activity
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mEventDataObserver = Observer {
            if (it.isEmpty()) {
                tv_empty_event.visibility = View.VISIBLE
                rv_event.visibility = View.GONE
            } else {
                tv_empty_event.visibility = View.GONE
                rv_event.visibility = View.VISIBLE
                val list = mutableListOf<BaseData>()
                list.addAll(it)
                mAdapter = MyEventAdapter()
                mAdapter!!.animationEnable = true
                mAdapter!!.setAnimationWithDefault(BaseQuickAdapter.AnimationType.SlideInBottom)
                rv_event.adapter = mAdapter
                rv_event.layoutManager = LinearLayoutManager(context)
                mAdapter!!.setNewData(list)
            }

        }
        mViewModel?.getMutableEventData()?.observe(this, mEventDataObserver!!)
        launch({
            mViewModel?.getEventData(LoginCache.userAccount!!.id)
        }, {
            ToastUtils.show(R.string.network_error, Toast.LENGTH_SHORT)
        })

    }

    class MyEventAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        private val mCollapsedStatus: SparseBooleanArray = SparseBooleanArray()

        init {
            addItemType(ITEM_MINE_EVENT_DATA, R.layout.mine_item_event)
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
                        item.jsonData = jsonData
                    } else {
                        helper.getView<ExpandableTextView>(R.id.et_root)
                            .setText(item.jsonData?.msg, mCollapsedStatus, helper.adapterPosition)
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
                        params.topMargin = SizeUtils.dp2px(8f)
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

                    helper.getView<ConstraintLayout>(R.id.cl_item_root).setOnClickListener {
                        val bundle = Bundle()
                        bundle.putSerializable(KEY_EVENT_DATA, item)
                        BridgeProviders.instance.getBridge(EventDetailBridgeInterface::class.java)
                            .toEventDetailActivity(bundle)
                    }
                    helper.getView<ExpandableTextView>(R.id.et_root)
                        .setOnExpandStateChangeListener(object :
                            ExpandableTextView.OnExpandStateChangeListener {
                            override fun onTextClick() {
                                val bundle = Bundle()
                                bundle.putSerializable(KEY_EVENT_DATA, item)
                                BridgeProviders.instance.getBridge(EventDetailBridgeInterface::class.java)
                                    .toEventDetailActivity(bundle)
                            }

                            override fun onExpandStateChanged(
                                textView: TextView?,
                                isExpanded: Boolean
                            ) {
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
}