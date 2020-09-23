package com.brins.baselib.mvvm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.brins.baselib.fragment.LoadingState
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IPresenter
import com.brins.baselib.utils.TUtil
import org.greenrobot.eventbus.EventBus

open class BaseViewModel<M : IModel>(application: Application) : AndroidViewModel(application),
    IPresenter {

    var mLoadingData: MutableLiveData<LoadingState> =
        MutableLiveData<LoadingState>()
    var mModel: M? = null


    init {
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
        if (mModel != null) {
            mModel!!.onDestroy()
            mModel = null
        }
    }

    open fun useEventBus(): Boolean {
        return false
    }


}