package com.brins.radiolib.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.networklib.model.radio.DjProgramResult
import com.brins.networklib.model.radio.RadioResult
import com.brins.radiolib.model.RadioModel

/**
 * Created by lipeilin
 * on 2021/2/5
 */
interface RadioContract {
    interface View : IView {
        fun onRecRadioLoad(result: RadioResult)

        fun onPerRadioLoad(result: DjProgramResult)
    }


    abstract class Presenter : BasePresenter<RadioModel, View>() {
        abstract suspend fun getRecommendRadio(limit: Int = 6)

        abstract suspend fun getPersonalizedRadio()
    }

    abstract class ViewModel(application: Application) : BaseViewModel<RadioModel>(application) {
        abstract suspend fun getRecommendRadio(limit: Int = 6)

        abstract suspend fun getPersonalizedRadio()
    }

    interface Model : IModel {
        suspend fun getRecommendRadio(limit: Int = 6): RadioResult

        suspend fun getPersonalizedRadio(): DjProgramResult
    }
}