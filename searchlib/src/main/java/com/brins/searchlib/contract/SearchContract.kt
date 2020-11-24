package com.brins.searchlib.contract

import android.app.Application
import com.brins.baselib.module.*
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.networklib.model.follow.FollowOrUnFollowResult
import com.brins.networklib.model.search.HotSearchResult
import com.brins.networklib.model.search.SearchResult
import com.brins.networklib.model.search.SearchSuggestResult
import com.brins.searchlib.model.SearchModel

/**
 * Created by lipeilin
 * on 2020/11/15
 */
interface SearchContract {


    interface View : IView {
        fun onLoadHistorySearch(result: ArrayList<String>)

        fun onLoadHotSearch(result: HotSearchResult)

        fun onAddHistorySearch(result: String)

        fun onHistorySearchClear()

        fun onSuggestSearchLoad(result: SearchSuggestResult)

    }

    abstract class Presenter : BasePresenter<SearchModel, View>() {

        abstract suspend fun loadHotSearch()

        abstract suspend fun loadSearchSuggest(input: String)

        abstract fun loadHistorySearch()

        abstract fun addHistorySearch(search: String)

        abstract fun clearHistorySearch()

    }

    interface Model : IModel {

        suspend fun loadHotSearch(): HotSearchResult

        suspend fun loadSearchSuggest(input: String): SearchSuggestResult

        suspend fun loadMusicSearchResult(input: String, type: Int): SearchResult<BaseMusic>

        suspend fun loadAlbumSearchResult(input: String, type: Int): SearchResult<BaseMusic.Album>

        suspend fun loadArtistSearchResult(input: String, type: Int): SearchResult<BaseMusic.Artist>

        suspend fun loadMusicListSearchResult(input: String, type: Int): SearchResult<MusicList>

        suspend fun loadUserSearchResult(input: String, type: Int): SearchResult<UserProfile>

        suspend fun loadMusicVideoSearchResult(
            input: String,
            type: Int
        ): SearchResult<BaseMusicVideo>

        suspend fun loadRadioSearchResult(input: String, type: Int): SearchResult<DjRadio>

        suspend fun followUser(id: String): FollowOrUnFollowResult

        fun loadHistorySearch(): ArrayList<String>?

        fun addHistorySearch(search: String)

        fun clearHistorySearch()

    }

    abstract class ViewModel(application: Application) : BaseViewModel<SearchModel>(application) {
        abstract suspend fun loadSearchResult(input: String, type: Int = 1)

        abstract suspend fun followUser(user: UserProfile, finish: (bool: Boolean) -> Unit)
    }
}