package com.brins.dailylib.presenter

import com.brins.dailylib.contract.DailyContract

/**
 * Created by lipeilin
 * on 2020/11/1
 */
class DailyMusicPresenter : DailyContract.Presenter() {

    override suspend fun getDailyMusic() {
        val result = mModel?.getDailyMusic()
        result?.let {
            mView?.onDailyMusicLoad(it)
        }
    }
}