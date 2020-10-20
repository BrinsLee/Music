package com.brins.mine.viewmodel

import android.app.Application
import com.brins.mine.contract.MineContract

class MineViewModel private constructor(application: Application) :
    MineContract.ViewModel(application) {


    companion object {

        @Volatile
        private var instance: MineViewModel? = null
        fun getInstance(application: Application): MineViewModel {
            if (instance != null) {
                return instance!!
            }
            return synchronized(this) {
                if (instance != null) {
                    instance!!
                } else {
                    instance = MineViewModel(application)
                    instance!!
                }
            }
        }
    }

    override fun getMyMusicLists(id: String) {
        mModel
    }
}