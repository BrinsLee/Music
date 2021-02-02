package com.brins.eventdetaillib.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.config.KEY_EVENT_THREADID
import com.brins.baselib.config.MUSIC_COMMENT
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE
import com.brins.baselib.module.ITEM_MUSIC_DETAIL_COMMENT
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.route.RouterHub.Companion.EVENTCOMMENTFRAGMENT
import com.brins.baselib.utils.TimeUtil
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.eventdetaillib.R
import com.brins.eventdetaillib.model.SingleTitleData
import com.brins.eventdetaillib.viewmodel.EventViewModel
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.model.comment.CommentResult
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_event_comment.*

@Route(path = EVENTCOMMENTFRAGMENT)
class EventCommentFragment private constructor() : BaseMvvmFragment<EventViewModel>() {


    private var mHotCommentObserver: Observer<MutableList<CommentResult.Comment>>? = null
    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null

    lateinit var mThreadId: String

    companion object {
        fun getInstance(bundle: Bundle): EventCommentFragment {
            val fragment = EventCommentFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return EventViewModel(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_event_comment
    }

    override fun reLoad() {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mThreadId = arguments?.getString(KEY_EVENT_THREADID, "")!!
    }

    override fun needParent(): Boolean {
        return false
    }

    override fun useEventBus(): Boolean {
        return false
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        mAdapter = CommentAdapter()
        recycler.adapter = mAdapter
        recycler.layoutManager = LinearLayoutManager(mActivity)
        (mAdapter as CommentAdapter).setOnLikeCommentListener(object :
            CommentAdapter.onLikeCommentListener {

            override fun onLikeClick(t: Int, item: CommentResult.Comment, position: Int) {
                synchronized(this@EventCommentFragment) {
                    ApiHelper.launch({
                        mViewModel?.likeOrUnLikeEventComment(
                            mThreadId,
                            item.commentId,
                            t,
                            MUSIC_COMMENT.COMMENT_TYPE_MUSIC
                        )
                        if (t == 1) {
                            item.likedCount++
                        } else {
                            item.likedCount--
                        }
                        item.liked = !item.liked
                        mAdapter?.notifyItemChanged(position)

                    }, {})
                }
            }

        })
        mHotCommentObserver = Observer {
            hideLoading()
            val list = mutableListOf<BaseData>()
            if (it.isNullOrEmpty() && mViewModel?.getMutableCommentData()?.value!!.isEmpty()) {
                tv_empty_view.visibility = View.VISIBLE
            } else {
                tv_empty_view.visibility = View.GONE
                if (it.isNotEmpty()) {
                    list.add(SingleTitleData().setTitle("精彩评论"))
                    list.addAll(it)
                }
                if (mViewModel?.getMutableCommentData()?.value!!.isNotEmpty()) {
                    list.add(SingleTitleData().setTitle("最新评论"))
                    list.addAll(mViewModel?.getMutableCommentData()?.value!!)
                }
                mAdapter!!.setNewData(list)
            }

        }
        mViewModel?.getMutableHotCommentData()?.observe(this, mHotCommentObserver!!)
        ApiHelper.launch({
            showLoading()
            mViewModel?.getEventComment(mThreadId)
        }, {})
    }

    class CommentAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        private var listener: onLikeCommentListener? = null

        interface onLikeCommentListener {
            fun onLikeClick(t: Int, item: CommentResult.Comment, position: Int)
        }

        init {

            addItemType(ITEM_MUSIC_DETAIL_COMMENT, R.layout.event_detail_item_comment)
            addItemType(ITEM_HOME_SINGLE_TITLE, R.layout.event_detail_item_title)
        }

        fun setOnLikeCommentListener(listener: onLikeCommentListener) {
            this.listener = listener
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            when (helper.itemViewType) {
                ITEM_MUSIC_DETAIL_COMMENT -> {
                    GlideHelper.setCircleImageResource(
                        helper.getView<ImageView>(R.id.iv_avatar),
                        (item as CommentResult.Comment).user?.avatarUrl
                    )
                    helper.setText(R.id.tv_username, item.user?.nickname)
                    helper.setText(R.id.tv_publish_time, TimeUtil.getTimeStandardOnlyYMD(item.time))
                    val likeCount: String =
                        if (item.likedCount == 0) "点赞" else item.likedCount.toString()
                    helper.setText(
                        R.id.tv_like_count,
                        likeCount
                    )
                    helper.setText(R.id.tv_content, item?.content)
                    if (item.liked) {
                        helper.getView<ImageView>(R.id.iv_like)
                            .setImageResource(R.drawable.base_shape_comment_like)
                        helper.getView<ImageView>(R.id.iv_like).setOnClickListener {
                            //取消点赞
                            listener?.onLikeClick(0, item, helper.adapterPosition)
                        }
                    } else {
                        helper.getView<ImageView>(R.id.iv_like)
                            .setImageResource(R.drawable.base_shape_comment_unlike)

                        helper.getView<ImageView>(R.id.iv_like).setOnClickListener {
                            //点赞
                            listener?.onLikeClick(1, item, helper.adapterPosition)
                        }
                    }
                }

                ITEM_HOME_SINGLE_TITLE -> {
                    helper.setText(R.id.item_text_view, (item as SingleTitleData).getTitle())
                }
            }
        }

    }

}