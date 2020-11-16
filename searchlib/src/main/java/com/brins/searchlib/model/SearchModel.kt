package com.brins.searchlib.model

import com.brins.baselib.utils.GsonUtils
import com.brins.baselib.utils.SpUtils
import com.brins.baselib.utils.SpUtils.KEY_SEARCH_HISTORY
import com.brins.baselib.utils.SpUtils.SP_SEARCH_INFO
import com.brins.baselib.utils.UIUtils
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.search.HotSearchResult
import com.brins.searchlib.contract.SearchContract

/**
 * Created by lipeilin
 * on 2020/11/15
 */
class SearchModel : SearchContract.Model {

    override fun loadHistorySearch(): ArrayList<String>? {
        val str =
            SpUtils.obtain(SP_SEARCH_INFO, UIUtils.getContext()).getString(KEY_SEARCH_HISTORY, "")
        val list = GsonUtils.listFromJson<String>(str)
        return list
    }

    override suspend fun loadHotSearch(): HotSearchResult =
        ApiHelper.getSearchService().loadHotSearch().await()

    override suspend fun loadSearchSuggest(input: String) =
        ApiHelper.getSearchService().loadSearchSuggest(input).await()

    override fun addHistorySearch(search: String) {
        val str =
            SpUtils.obtain(SP_SEARCH_INFO, UIUtils.getContext()).getString(KEY_SEARCH_HISTORY, "")
        var list = GsonUtils.listFromJson<String>(str)
        if (list == null) {
            list = ArrayList()
        }
        list.add(search)
        SpUtils.obtain(SP_SEARCH_INFO, UIUtils.getContext())
            .save(KEY_SEARCH_HISTORY, GsonUtils.listToJson(list))
    }

    override fun clearHistorySearch() {
        SpUtils.obtain(SP_SEARCH_INFO, UIUtils.getContext())
            .save(KEY_SEARCH_HISTORY, "")
    }

    override fun onDestroy() {

    }
}