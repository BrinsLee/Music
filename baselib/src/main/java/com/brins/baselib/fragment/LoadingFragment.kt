package com.brins.baselib.fragment

import android.os.Bundle
import android.view.WindowManager
import com.brins.baselib.R

class LoadingFragment private constructor() : BaseDialogFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.base_loading_layout
    }

    companion object {
        @Synchronized
        fun showSelf(): LoadingFragment {
            return LoadingFragment()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        retainInstance = true
        super.onCreate(savedInstanceState)
    }

    override fun isCanceledOnTouchOutside(): Boolean {
        return true
    }

    override fun isInterceptKeyCodeBack(): Boolean {
        return true
    }

    override fun getDialogAnimResId(): Int {
        return R.style.CustomCenterDialogAnim
    }

    override fun getDialogWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    override fun getDialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

}