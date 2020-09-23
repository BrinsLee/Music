package com.brins.musicdetail.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.musicdetail.model.MusicDetailModel
import com.brins.networklib.model.comment.CommentResult
import com.brins.networklib.model.music.MusicLrc
import com.brins.networklib.model.music.MusicLrcResult

interface MusicDetailContract {

    interface View : IView {
        fun onLikeMusic()

        fun onDislikeMusic()

        fun onMusicDetialLoad()

        fun onMusicLyricsLoad(lrc: MusicLrc?)
    }

    abstract class Presenter : BasePresenter<MusicDetailModel, View>() {

        abstract suspend fun loadMusicLrc(id: String)

    }

    interface Model : IModel {
        suspend fun loadMusicLrc(id: String): MusicLrcResult

        suspend fun loadMusicComment(id: String): CommentResult
    }

    abstract class ViewModel(application: Application) :
        BaseViewModel<MusicDetailModel>(application) {

        abstract suspend fun loadMusicComments(id: String)
    }
}