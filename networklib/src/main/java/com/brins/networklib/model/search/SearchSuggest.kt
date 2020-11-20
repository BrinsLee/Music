package com.brins.networklib.model.search

import com.google.gson.annotations.SerializedName

class SearchSuggest {
    @SerializedName("allMatch")
    var allMatch : ArrayList<AllMatch>? = null
}