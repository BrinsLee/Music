package com.brins.eventdetaillib.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.eventdetaillib.model.EventModel

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

    }

    abstract class ViewModel(application: Application) : BaseViewModel<EventModel>(application) {

    }
}