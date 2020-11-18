package com.brins.networklib.model.search

import com.google.gson.annotations.SerializedName

class SearchResult<T> {

    @SerializedName("result")
    var dataBean: SearchResultBean<T>? = null

}