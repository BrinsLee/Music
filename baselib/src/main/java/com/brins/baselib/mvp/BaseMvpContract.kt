package com.brins.baselib.mvp

interface BaseMvpContract {

    interface CallBack<D> {
        fun onSuccees(d: D)
        fun onFailed(code: Int, msg: String, data: D)
        fun onError()
    }
}