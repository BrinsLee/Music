package com.brins.loacl.contarct

import android.app.Application
import android.database.Cursor
import androidx.loader.app.LoaderManager
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.loacl.model.LocalModel

/**
 * Created by lipeilin
 * on 2021/2/18
 */
interface LocalContract {
    interface View : IView {
        fun onLocalMusicLoaded()
    }

    abstract class Presenter : BasePresenter<LocalModel, View>() {
        abstract suspend fun loadLocalMusic()
    }

    interface Model : IModel {

        fun loadLocalMusic(
            manager: LoaderManager,
            callback: LoaderManager.LoaderCallbacks<Cursor>
        )
    }

    abstract class ViewModel(application: Application) : BaseViewModel<LocalModel>(application) {

        abstract fun loadLocalMusic(manager: LoaderManager)
    }
}