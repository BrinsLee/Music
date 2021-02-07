package com.brins.radiolib.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.brins.networklib.model.radio.Radio
import com.brins.networklib.model.radio.RadioResult
import com.brins.radiolib.contract.RadioContract

/**
 * Created by lipeilin
 * on 2021/2/7
 */
class RadioViewModel private constructor(application: Application) :
    RadioContract.ViewModel(application) {


    private lateinit var mRadioData: RadioResult
    private val mRadioLiveData: MutableLiveData<MutableList<Radio>> = MutableLiveData()

    fun getRadioData(): RadioResult = mRadioData
    fun getRadioLiveData(): MutableLiveData<MutableList<Radio>> = mRadioLiveData

    companion object {

        @Volatile
        private var instance: RadioViewModel? = null
        fun getInstance(application: Application): RadioViewModel {
            if (instance != null) {
                return instance!!
            }
            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = RadioViewModel(application)
                    instance!!
                }
            }
        }
    }

    override suspend fun getRecommendRadio(limit: Int) {
        val result = mModel?.getRecommendRadio(limit)
        result?.let {
            mRadioData = result
            mRadioLiveData.value = it.djRadios
        }
    }

    override suspend fun getPersonalizedRadio() {
        TODO("Not yet implemented")
    }


}