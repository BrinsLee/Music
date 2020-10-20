package com.brins.loginlib.model

import com.brins.loginlib.contract.LoginContract
import com.brins.networklib.RetrofitFactory
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await

/**
 * Created by lipeilin
 * on 2020/10/20
 */
class LoginModel : LoginContract.Model {

    override suspend fun emailLogin(email: String, password: String) =
        ApiHelper.getLoginService().loginEmail(email, password).await()

    override fun onDestroy() {
    }
}