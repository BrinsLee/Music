package com.brins.radiolib.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.brins.baselib.utils.TUtil
import com.brins.networklib.model.radio.DjProgram
import com.brins.networklib.model.radio.DjProgramResult
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

    private lateinit var mRadioProgramData: DjProgramResult
    private val mProgramLiveData: MutableLiveData<MutableList<DjProgram>> = MutableLiveData()

    fun getRadioProgramData(): DjProgramResult = mRadioProgramData
    fun getProgramLiveData(): MutableLiveData<MutableList<DjProgram>> = mProgramLiveData

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

    override suspend fun getRadioProgram(rid: String, limit: Int) {
        if (mModel == null) {
            mModel = TUtil.getSuperT(this, 0)
        }
        val result = mModel?.getRadioProgram(rid, limit)
        result?.let {
            mRadioProgramData = it
            mProgramLiveData.value = it.programs
        }
    }

    fun getRadioByPos(pos: Int): Radio? {
        return if (::mRadioData.isInitialized && !mRadioData.djRadios.isNullOrEmpty()) {
            mRadioData.djRadios!![pos]
        } else {
            null
        }
    }

    fun getRadioByRid(rid: String): Radio? {
        if (::mRadioData.isInitialized && !mRadioData.djRadios.isNullOrEmpty()) {
            mRadioData.djRadios!!.forEach { radio ->
                if (radio.id == rid) {
                    return radio
                }
            }

        }
        return null

    }

}