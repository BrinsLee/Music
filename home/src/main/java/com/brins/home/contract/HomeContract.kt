package com.brins.home.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.home.model.HomeModel
import com.brins.networklib.model.album.AlbumResult
import com.brins.networklib.model.album.NewestAlbum
import com.brins.networklib.model.banner.BannerResult
import com.brins.networklib.model.music.TopMusic
import com.brins.networklib.model.music.TopMusicResult
import com.brins.baselib.module.MusicList
import com.brins.networklib.model.personal.PersonalizedMusic
import com.brins.networklib.model.personal.PersonalizedMusicList
import com.brins.networklib.model.personal.PersonalizedResult
import com.brins.networklib.model.recommend.RecommendResult
import com.chad.library.adapter.base.model.BaseData


/**
 * @author lipeilin
 * @date 2020/7/22
 */
interface HomeContract {

    interface View : IView {

        fun onPersonalizedMusicListLoad(data: PersonalizedResult<PersonalizedMusicList>?)

        fun onBannerLoad(data: BaseData?)

        fun onNewRecommendLoad(data: RecommendResult<MusicList>?)

        fun onHotRecommendLoad(data: RecommendResult<MusicList>?)

    }

    abstract class Presenter : BasePresenter<HomeModel, View>() {

        abstract suspend fun loadPersonalizedMusicList()

        abstract suspend fun loadBannerData()

        abstract suspend fun loadHotOrNewRecommend(type: String)

    }

    interface Model : IModel {
        suspend fun loadPersonalizedMusicList(): PersonalizedResult<PersonalizedMusicList>

        suspend fun loadPersonalizedMusic(): PersonalizedResult<PersonalizedMusic>

        suspend fun loadBanner(): BannerResult

        suspend fun loadNewestAlbum(): AlbumResult<NewestAlbum>

        suspend fun loadTopMusic(type: Int = 0): TopMusicResult<TopMusic>

        suspend fun loadHotOrNewRecommend(type: String): RecommendResult<MusicList>?
    }

    abstract class ViewModel(application: Application) : BaseViewModel<HomeModel>(application) {

        abstract suspend fun loadNewestAlbum()

        abstract suspend fun loadTopMusic(type: Int = 0)

        abstract suspend fun loadPersonalizedMusic()
    }
}