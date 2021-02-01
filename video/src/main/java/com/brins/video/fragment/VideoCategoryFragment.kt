package com.brins.video.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.baselib.config.MAINLAND
import com.brins.baselib.fragment.BaseMvpFragment
import com.brins.baselib.module.BaseData
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub.Companion.VIDEODETAILACTIVITY
import com.brins.networklib.helper.ApiHelper.launch
import com.brins.networklib.model.musicvideo.Mv
import com.brins.networklib.model.musicvideo.MvCommentsResult
import com.brins.video.R
import com.brins.video.adapter.VideoAdapter
import com.brins.video.contract.VideoContract
import com.brins.video.presenter.VideoPresenter
import com.chad.library.adapter.base2.BaseQuickAdapter
import com.chad.library.adapter.base2.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_video_category.*

class VideoCategoryFragment : BaseMvpFragment<VideoPresenter>(), VideoContract.View,
    OnItemClickListener {

    private val area: String by lazy { mBundle?.getString("VIDEO_AREA") ?: MAINLAND }
    private var mMusicVideoList = mutableListOf<BaseData>()
    private var mAdapter: VideoAdapter? = null

    companion object {

        fun getInstance(area: String): Fragment {
            val fragment: Fragment = VideoCategoryFragment()
            val bundle = Bundle()
            bundle.putString("VIDEO_AREA", area)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_video_category
    }

    override fun reLoad() {
    }

    override fun init(savedInstanceState: Bundle?) {
        showLoading()
        launch({
            mPresenter?.loadVideoData(15, area)
        }, {
            hideLoading()
        })
    }

    override fun onMusicVideoLoad(mutableList: MutableList<Mv>) {
        hideLoading()
        mMusicVideoList.addAll(mutableList)
        mAdapter = VideoAdapter(mMusicVideoList)
        rv_video.layoutManager = LinearLayoutManager(getMyContext())
        rv_video.adapter = mAdapter
        mAdapter?.setOnItemClickListener(this)
    }

    override fun onMusicVideoCommentLoad(result: MvCommentsResult) {

    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bundle = Bundle()
        bundle.putParcelable("Mv", mMusicVideoList[position] as Mv)
        ARouterUtils.go(VIDEODETAILACTIVITY, bundle)
    }

}