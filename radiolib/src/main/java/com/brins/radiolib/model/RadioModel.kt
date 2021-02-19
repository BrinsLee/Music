package com.brins.radiolib.model

import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.model.radio.DjProgramResult
import com.brins.networklib.model.radio.RadioResult
import com.brins.radiolib.contract.RadioContract

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class RadioModel : RadioContract.Model {

    override suspend fun getRecommendRadio(limit: Int): RadioResult =
        ApiHelper.getRadioService().getRecommendRadio(limit).await()

    override suspend fun getRadioProgram(rid: String, limit: Int): DjProgramResult =
        ApiHelper.getRadioService().getRadioProgram(rid, limit).await()

    override fun onDestroy() {

    }


}