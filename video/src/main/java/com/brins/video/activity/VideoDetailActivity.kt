package com.brins.video.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.module.BaseData
import com.brins.baselib.module.ITEM_VIDEO_MUSIC_VIDEO_COMMENT
import com.brins.baselib.route.RouterHub.Companion.VIDEODETAILACTIVITY
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.baselib.widget.CommonHeaderView
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.musicvideo.Mv
import com.brins.networklib.model.musicvideo.MvCommentsBean
import com.brins.networklib.model.musicvideo.MvCommentsResult
import com.brins.video.R
import com.brins.video.contract.VideoContract
import com.brins.video.presenter.VideoPresenter
import com.chad.library.adapter.base2.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base2.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Route(path = VIDEODETAILACTIVITY)
class VideoDetailActivity : BaseMvpActivity<VideoPresenter>(),
    CommonHeaderView.OnBackClickListener, VideoContract.View {

    private var mCurrentMv: Mv? = null
    private lateinit var mCommentAdapter: CommentAdapter


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        head.setOnBackClickListener(this)
        mCurrentMv = intent?.getParcelableExtra("Mv")
        if (mCurrentMv != null) {
            loadMvComments()
            head.title = "${mCurrentMv!!.dataBean.artistName} ${mCurrentMv!!.dataBean.name}"
            videoPlayer.setAllControlsVisiblity(GONE, GONE, VISIBLE, GONE, VISIBLE, GONE, GONE)
            GlideHelper.setImageResource(videoPlayer.thumbImageView, mCurrentMv!!.dataBean.cover)
            videoPlayer.setUp(
                mCurrentMv!!.metaDataBean.url,
                mCurrentMv!!.dataBean.name,
                Jzvd.SCREEN_NORMAL
            )
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_video_detail
    }


    override fun onBackClick(view: View) {
        onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Log.d("VideoDetailActivity==", "onPause")
        Jzvd.releaseAllVideos()
    }


    //MVP View

    private fun loadMvComments() {
        launch({
            showLoading()
            mPresenter?.loadVideoComments(mCurrentMv!!.dataBean.id)
        }, { hideLoading() })
    }


    override fun onMusicVideoLoad(mutableList: MutableList<Mv>) {
    }

    override fun onMusicVideoCommentLoad(result: MvCommentsResult) {
        hideLoading()
        result.comments?.let {
            val list = arrayListOf<BaseData>()
            it.forEach { commentsBean ->
                list.add(commentsBean)
            }
            mCommentAdapter = CommentAdapter(list)
            recyclerView.adapter = mCommentAdapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onBackPressed() {
        if (JzvdStd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("VideoDetailActivity==", "onDestroy")
    }

    class CommentAdapter(data: ArrayList<BaseData>) :
        BaseMultiItemQuickAdapter<BaseData, BaseViewHolder>(data) {

        init {
            addItemType(ITEM_VIDEO_MUSIC_VIDEO_COMMENT, R.layout.video_item_comment)
        }

        override fun convert(helper: BaseViewHolder, item: BaseData) {
            when (item.itemType) {
                ITEM_VIDEO_MUSIC_VIDEO_COMMENT -> {

                    helper.setText(R.id.textViewName, (item as MvCommentsBean).user?.nickname)
                    helper.setText(R.id.textViewArtist, item.content)
                    GlideHelper.setCircleImageResource(
                        helper.getView<ImageView>(R.id.imgCover),
                        item.user?.avatarUrl,
                        R.drawable.base_icon_default_avatar
                    )
                }
            }
        }

    }
}