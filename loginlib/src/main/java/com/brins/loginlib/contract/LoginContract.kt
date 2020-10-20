package com.brins.loginlib.contract

import android.app.Application
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.loginlib.model.LoginModel
import com.brins.baselib.module.userlogin.UserLoginResult

/**
 * Created by lipeilin
 * on 2020/10/20
 */
interface LoginContract {

    interface View : IView {

        fun onLoginSuccess(result: UserLoginResult)

        fun onLoginFail(msg: String)

        fun onLogoutSuccess()

        fun onLogoutFail(msg: String)
    }

    abstract class Presenter : BasePresenter<LoginModel, View>() {

        abstract suspend fun emailLogin(email: String, password: String)

    }

    interface Model : IModel {

        suspend fun emailLogin(email: String, password: String): UserLoginResult

    }

    abstract class ViewModel(application: Application) : BaseViewModel<LoginModel>(application) {

    }
}