package com.brins.baselib.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.brins.baselib.R

abstract class BaseAppFragment : Fragment() {

    val TAG = javaClass.simpleName

    private var rootView: FrameLayout? = null
    protected var mActivity: Activity? = null
    var mBundle: Bundle? = null
    var mLoadingFragment: LoadingFragment? = null

    /*实现懒加载*/
    private var isFirst = true // 是否为第一次加载

    private var isOk = false

    protected abstract fun getLayoutResID(): Int
    protected abstract fun reLoad()
    protected abstract fun init(savedInstanceState: Bundle?)
}