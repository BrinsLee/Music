package com.brins.find.presenter

import com.brins.find.contract.FindContract

/**
 * Created by lipeilin
 * on 2020/12/1
 */
class FindPresenter : FindContract.Presenter() {

    override suspend fun loadEvent(lastTime: Int, pageSize: Int) {
        val result = mModel?.loadUserEvent(lastTime, pageSize)
        result?.let {
            mView?.onUserEventLoad(it)
        }
    }
}