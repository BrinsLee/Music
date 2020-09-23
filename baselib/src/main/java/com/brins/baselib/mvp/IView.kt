package com.brins.baselib.mvp

import android.content.Context
import android.os.Message


interface IView {

    fun showLoading()

    fun hideLoading()

    fun handleMyMessage(message: Message) {}

    fun getMyContext(): Context
}