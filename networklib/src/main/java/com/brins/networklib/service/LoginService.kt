package com.brins.networklib.service

import com.brins.baselib.config.LOGIN.Companion.LOGIN_EMAIL_LOGIN
import com.brins.baselib.module.userlogin.UserLoginResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lipeilin
 * on 2020/10/20
 */
interface LoginService {

    @GET(LOGIN_EMAIL_LOGIN)
    fun loginEmail(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<UserLoginResult>

}