package com.brins.loginlib.presenter

import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.database.factory.DatabaseFactory
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.subscribeDbResult
import com.brins.loginlib.R
import com.brins.loginlib.contract.LoginContract
import com.brins.baselib.module.userlogin.UserLoginResult
import com.brins.baselib.utils.SpUtils

/**
 * Created by lipeilin
 * on 2020/10/20
 */
class LoginPresenter : LoginContract.Presenter() {

    override suspend fun emailLogin(email: String, password: String) {
        val result = mModel?.emailLogin(email, password)
        result?.let {
            storeUserInfo(it)
        }
    }

    private fun storeUserInfo(result: UserLoginResult) {
        DatabaseFactory.storeUserInfo(result.account)
            .subscribeDbResult({
                DatabaseFactory.storeUserProfile(result.profile).subscribeDbResult({
                    LoginCache.userAccount = result.account
                    LoginCache.userProfile = result.profile
                    mView?.onLoginSuccess(result)
                }, {
                    mView?.onLoginFail(UIUtils.getString(R.string.login_fail))
                })
            }, {
                mView?.onLoginFail(it.message!!)
            })
    }

    private fun clearUserInfo() {
        DatabaseFactory.deleteUserInfo()?.subscribeDbResult({
            DatabaseFactory.deleteUserProfile()?.subscribeDbResult({
                mView?.onLogoutSuccess()
            }, {
            })

        }, {
            mView?.onLogoutFail(it.message!!)
        })
    }
}