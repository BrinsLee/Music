package com.brins.baselib.fragment

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel

abstract class BaseMvvmFragment<VM : BaseViewModel<out IModel>> : BaseFragment() {

    protected open var mViewModel: VM? = null
    protected var mLoadingData: MutableLiveData<LoadingState>? = null
    protected var mLoadingStateObserver: Observer<LoadingState>? = null
    protected var mLoadingStateCallBack: LoadingStateCallBack? = null

    abstract fun getViewModel(): BaseViewModel<out IModel>?

    override fun init(savedInstanceState: Bundle?) {
        mViewModel = getViewModel() as VM?
        mViewModel?.let {
            mLoadingData = it.mLoadingData
            mLoadingStateObserver = object : Observer<LoadingState> {
                override fun onChanged(t: LoadingState?) {

                    when (t?.state) {
                        LoadingState.LOADING_STATE_SUCCESS -> mLoadingStateCallBack?.onLoadingSuccess()

                        LoadingState.LOADING_STATE_FAIL -> mLoadingStateCallBack?.onLoadingFail()
                    }
                }

            }
            mLoadingData!!.observe(this, mLoadingStateObserver!!)
        }
    }

    interface LoadingStateCallBack {
        fun onLoadingSuccess()
        fun onLoadingFail()
    }

    override fun onDestroy() {
        if (mViewModel != null) {
            mViewModel?.unsubscribe()
            mViewModel = null
        }
        super.onDestroy()
    }
}