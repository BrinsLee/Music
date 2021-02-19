package com.brins.radiolib.presenter

import com.brins.radiolib.contract.RadioContract

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class RadioPresenter : RadioContract.Presenter() {

    override suspend fun getRecommendRadio(limit: Int) {
        val result = mModel?.getRecommendRadio(limit)
        result?.let {
            mView?.onRecRadioLoad(it)
        }
    }

    override suspend fun getRadioProgram(rid: String, limit: Int) {
        val result = mModel?.getRadioProgram(rid, limit)
        result?.let {
            mView?.onPerRadioLoad(it)
        }
    }
}