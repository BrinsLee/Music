package com.brins.searchlib.presenter

import com.brins.searchlib.contract.SearchContract

/**
 * Created by lipeilin
 * on 2020/11/15
 */
class SearchPresenter : SearchContract.Presenter() {

    override fun loadHistorySearch() {
        val result = mModel?.loadHistorySearch()
        result?.let {
            mView?.onLoadHistorySearch(it)
        }
    }

    override suspend fun loadHotSearch() {
        val result = mModel?.loadHotSearch()
        result?.let {
            mView?.onLoadHotSearch(it)
        }
    }

    override fun addHistorySearch(search: String) {
        mModel?.addHistorySearch(search)
    }

    override suspend fun loadSearchSuggest(input: String) {
        val result = mModel?.loadSearchSuggest(input)
        result?.let {
            mView?.onSuggestSearchLoad(it)
        }
    }

    override fun clearHistorySearch() {
        mModel?.clearHistorySearch()
        mView?.onHistorySearchClear()
    }

    override fun subscribe() {
        super.subscribe()
    }

    override fun unsubscribe() {
        mView = null
    }
}