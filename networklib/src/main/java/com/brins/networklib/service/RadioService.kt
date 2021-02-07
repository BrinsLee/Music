package com.brins.networklib.service

import com.brins.baselib.config.RADIO
import com.brins.networklib.model.radio.DjProgramResult
import com.brins.networklib.model.radio.RadioResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by lipeilin
 * on 2021/2/5
 */
interface RadioService {

    /**
     * 获取推荐电台
     *
     * @param limit
     * @return
     */
    @GET(RADIO.RECOMMEND_RADIO)
    fun getRecommendRadio(@Query("limit") limit: Int = 6): Call<RadioResult>

    /**
     * 获取猜你喜欢电台
     *
     * @return
     */
    @GET(RADIO.PERSONALIZED_RADIO)
    fun getPersonailzedRadio(): Call<DjProgramResult>
}