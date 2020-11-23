package com.brins.musicdetail.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvvmActivity
import com.brins.baselib.config.KEY_COMMEND_PATH
import com.brins.baselib.config.KEY_ID
import com.brins.baselib.config.MUSIC_COMMENT.Companion.COMMENT_TYPE_MUSIC
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_HOME_SINGLE_TITLE
import com.brins.baselib.module.ITEM_MUSIC_DETAIL_COMMENT
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.route.RouterHub.Companion.COMMENTSACTIVITY
import com.brins.baselib.utils.TimeUtil
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.getStatusBarHeight
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.CommonHeaderView
import com.brins.musicdetail.R
import com.brins.musicdetail.model.SingleTitleData
import com.brins.musicdetail.viewmodel.MusicCommentsViewModel
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.comment.CommentResult
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_comments.*

@Route(path = COMMENTSACTIVITY)
class CommentsActivity : BaseMvvmActivity<MusicCommentsViewModel>() {

    @Autowired(name = KEY_ID)
    lateinit var id: String

    @Autowired(name = KEY_COMMEND_PATH)
    lateinit var path: String

    private var mAdapter: BaseQuickAdapter<BaseData, BaseViewHolder>? = null
    private var mHotCommentObserver: Observer<MutableList<CommentResult.Comment>>? = null
    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MusicCommentsViewModel(this.application)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_comments
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        header.setPadding(0, getStatusBarHeight(this), 0, 0)
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        mAdapter = CommentAdapter()
        recycler.adapter = mAdapter
        recycler.layoutManager = LinearLayoutManager(this)
        (mAdapter as CommentAdapter).setOnLikeCommentListener(object :
            CommentAdapter.onLikeCommentListener {

            override fun onLikeClick(t: Int, item: CommentResult.Comment, position: Int) {
                synchronized(this@CommentsActivity) {
                    launch({
                        mViewModel?.likeOrUnLikeMusicComment(
                            id,
                            item.commentId,
                            t,
                            COMMENT_TYPE_MUSIC
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
            header.initView("${UIUtils.getString(R.string.comment)}(${mViewModel?.getCommentData()?.total ?: 0})")
            val list = mutableListOf<BaseData>()
            list.add(SingleTitleData().setTitle("精彩评论"))
            list.addAll(it)
            list.add(SingleTitleData().setTitle("最新评论"))
            list.addAll(mViewModel?.getMutableCommentData()?.value!!)
            mAdapter!!.setNewData(list)
        }
        mViewModel?.getMutableHotCommentData()?.observe(this, mHotCommentObserver!!)
        launch({
            mViewModel?.loadComments(path, id)
        }, {})
    }

    class CommentAdapter : BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>() {

        private var listener: onLikeCommentListener? = null

        interface onLikeCommentListener {
            fun onLikeClick(t: Int, item: CommentResult.Comment, position: Int)
        }

        init {

            addItemType(ITEM_MUSIC_DETAIL_COMMENT, R.layout.music_detail_item_comment)
            addItemType(ITEM_HOME_SINGLE_TITLE, R.layout.music_detail_item_title)
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