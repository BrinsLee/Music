package com.brins.dailylib.contract

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.dailylib.model.DailyModel
import com.brins.networklib.model.daily.DailyMusicResult

/**
 * Created by lipeilin
 * on 2020/11/1
 */
interface DailyContract {

    interface View : IView {
        fun onDailyMusicLoad(result : DailyMusicResult)

    }

    abstract class Presenter : BasePresenter<DailyModel, View>() {

        abstract suspend fun getDailyMusic()

    }

    interface Model : IModel {
        suspend fun getDailyMusic() : DailyMusicResult
    }
}