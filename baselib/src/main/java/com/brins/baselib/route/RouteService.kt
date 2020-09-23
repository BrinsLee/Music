package com.brins.baselib.route

import com.alibaba.android.arouter.facade.template.IProvider

interface RouteService : IProvider {
    fun route(params: String)
}