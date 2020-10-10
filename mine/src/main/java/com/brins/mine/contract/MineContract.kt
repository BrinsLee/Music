package com.brins.mine.contract

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.mine.model.MineModel



interface MineContract {

    interface View : IView {


    }

    abstract class Presenter : BasePresenter<MineModel, View>() {



    }

    interface Model : IModel {

    }
}