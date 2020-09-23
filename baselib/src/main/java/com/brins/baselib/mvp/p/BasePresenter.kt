package com.brins.baselib.mvp.p

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IPresenter
import com.brins.baselib.mvp.IView
import com.brins.baselib.utils.TUtil
import org.greenrobot.eventbus.EventBus

abstract class BasePresenter<M : IModel?, V : IView?> : IPresenter {

    protected var mModel: M? = null

    public var mView: V? = null

    fun attach(view: Any) {
        this.mView = view as V
        this.mModel = TUtil.getSuperT(this, 0)
        subscribe()
    }

    override fun subscribe() {
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun unsubscribe() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        mModel?.onDestroy()
        mModel = null
        mView = null

    }

    open fun useEventBus(): Boolean {
        return false
    }

}