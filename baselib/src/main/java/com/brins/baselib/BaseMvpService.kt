package com.brins.baselib

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter

abstract class BaseMvpService<P : BasePresenter<out IModel, out IView>> : Service() {

    protected var TAG = this.javaClass.simpleName
    var mPresenter: P? = null

    override fun onCreate() {
        super.onCreate()
    }

    fun bind(presenter: P) {
        this.mPresenter = presenter
    }

    override fun onDestroy() {
        this.mPresenter = null
        super.onDestroy()
    }
}
