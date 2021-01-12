package com.brins.eventdetaillib.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.eventdetaillib.model.EventModel
import com.brins.networklib.model.comment.CommentResult

/**
 * Created by lipeilin
 * on 2020/12/31
 */
interface EventDetailContract {

    interface View : IView {


    }

    abstract class Presenter : BasePresenter<EventModel, View>() {


    }

    interface Model : IModel {

        suspend fun getEventComment(id: String) : CommentResult

        suspend fun getEventForward(id: String)

        suspend fun likeOrUnLikeComment(id: String, cid: String, t: Int, type: Int)

    }

    abstract class ViewModel(application: Application) : BaseViewModel<EventModel>(application) {

        abstract suspend fun getEventComment(id: String)

        abstract suspend fun getEventForward(id: String)

        abstract suspend fun likeOrUnLikeEventComment(id: String, cid: String, t: Int, type: Int)

    }
}