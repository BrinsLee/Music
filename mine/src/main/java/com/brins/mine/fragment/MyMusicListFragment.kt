package com.brins.mine.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.mine.R
import com.brins.mine.viewmodel.MineViewModel

class MyMusicListFragment : BaseMvvmFragment<MineViewModel>() {

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MineViewModel.getInstance(mActivity!!.application)

    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_my_music_list
    }

    override fun reLoad() {
    }

}