package com.brins.picturedetaillib.contract

import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvp.IView
import com.brins.baselib.mvp.p.BasePresenter
import com.brins.picturedetaillib.model.DetailPictureModel

/**
 * Created by lipeilin
 * on 2020/12/27
 */
interface DetailPictureContract {

    interface View : IView {

    }

    abstract class Presenter : BasePresenter<DetailPictureModel, View>() {

    }

    interface Model : IModel {

    }
}