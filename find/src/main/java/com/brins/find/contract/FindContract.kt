package com.brins.find.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.find.model.FindModel
import com.brins.networklib.model.follow.FollowResult

/**
 * Created by lipeilin
 * on 2020/12/1
 */
interface FindContract {

    interface View : IView {
        fun onUserEventLoad()
    }

    abstract class Presenter : BasePresenter<FindModel, View>() {

    }

    abstract class ViewModel(application: Application) : BaseViewModel<FindModel>(application) {
        abstract suspend fun getMyFollows(uid: String)
    }

    interface Model : IModel {
        suspend fun loadUserEvent()

        suspend fun loadMyFollows(uid: String): FollowResult
    }
}