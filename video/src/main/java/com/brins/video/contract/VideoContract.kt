package com.brins.video.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.networklib.model.musicvideo.Mv
import com.brins.networklib.model.musicvideo.MvCommentsResult
import com.brins.networklib.model.musicvideo.MvMetaResult
import com.brins.networklib.model.musicvideo.MvResult
import com.brins.video.model.VideoModel

/**
 * Created by lipeilin
 * on 2021/1/29
 */
interface VideoContract {

    interface View : IView {

        fun onMusicVideoLoad(mutableList: MutableList<Mv>)

        fun onMusicVideoCommentLoad(result: MvCommentsResult)
    }

    abstract class Presenter : BasePresenter<VideoModel, View>() {
        abstract suspend fun loadVideoData(limit: Int, area: String)

        abstract suspend fun loadVideoComments(id: String)

    }

    abstract class ViewModel(application: Application) : BaseViewModel<VideoModel>(application) {
    }

    interface Model : IModel {
        suspend fun loadVideoData(limit: Int, area: String): MvResult

        suspend fun loadUrl(id: String): MvMetaResult

        suspend fun loadVideoComments(id: String): MvCommentsResult
    }
}