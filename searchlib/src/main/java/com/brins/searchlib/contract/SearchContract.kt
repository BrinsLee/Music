package com.brins.searchlib.contract

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.networklib.model.search.HotSearchResult
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

        abstract fun loadHistorySearch()

        abstract suspend fun loadHotSearch()

        abstract suspend fun loadSearchSuggest(input: String)

        abstract fun addHistorySearch(search: String)

        abstract fun clearHistorySearch()

    }

    interface Model : IModel {

        fun loadHistorySearch(): ArrayList<String>?

        suspend fun loadHotSearch(): HotSearchResult

        suspend fun loadSearchSuggest(input: String): SearchSuggestResult

        fun addHistorySearch(search: String)

        fun clearHistorySearch()
    }
}