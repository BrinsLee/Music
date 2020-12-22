package com.brins.find.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.find.model.FindModel
import com.brins.networklib.model.event.EventsResult
import com.brins.networklib.model.follow.FollowResult

/**
 * Created by lipeilin
 * on 2020/12/1
 */
interface FindContract {

    interface View : IView {
        fun onUserEventLoad(result: EventsResult)
    }

    abstract class Presenter : BasePresenter<FindModel, View>() {
        abstract suspend fun loadEvent(lastTime: Int = -1, pageSize: Int = 30)
    }

    abstract class ViewModel(application: Application) : BaseViewModel<FindModel>(application) {
        abstract suspend fun getMyFollows(uid: String)
    }

    interface Model : IModel {
        suspend fun loadUserEvent(lastTime: Int, pageSize: Int): EventsResult

        suspend fun loadMyFollows(uid: String): FollowResult
    }
}