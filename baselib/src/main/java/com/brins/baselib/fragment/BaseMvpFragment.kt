package com.brins.baselib.fragment


import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.utils.TUtil

abstract class BaseMvpFragment<P : BasePresenter<out IModel, out IView>> : BaseFragment() {


    var mPresenter: P? = null

    override fun onCreateViewAfterBinding() {
        mPresenter = TUtil.getT(this, 0)
        mPresenter?.attach(this)
        super.onCreateViewAfterBinding()
    }

    override fun onDestroy() {
        mPresenter?.unsubscribe()
        mPresenter = null
        super.onDestroy()
    }
}