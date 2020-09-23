package com.brins.baselib.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.utils.TUtil


abstract class BaseMvpActivity<P : BasePresenter<out IModel, out IView>> : BaseActivity() {

    protected var TAG = this.javaClass.simpleName
    var mPresenter: P? = null

    override fun showLoading() {
    }

    override fun hideLoading() {

    }

    @CallSuper
    override fun init(savedInstanceState: Bundle?) {
        mPresenter = TUtil.getT(this, 0)
        mPresenter?.attach(this)
    }

    override fun onDestroy() {
        mPresenter?.unsubscribe()
        mPresenter = null
        super.onDestroy()
    }
}
