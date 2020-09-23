package com.brins.baselib.fragment


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.brins.baselib.R
import com.brins.baselib.mvp.IView
import com.brins.baselib.utils.eventbus.EventBusParams
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


abstract class BaseFragment : Fragment(), IView {

    val TAG = javaClass.simpleName

    private var rootView: FrameLayout? = null
    protected var mActivity: Activity? = null
    var mBundle: Bundle? = null
    var mLoadingFragment: LoadingFragment? = null

    /*实现懒加载*/
    protected open var mIsViewBinding: Boolean = false
    protected open var mIsVisibleToUser: Boolean = false
    protected open var mHadLoaded = false

    protected abstract fun getLayoutResID(): Int
    protected abstract fun reLoad()
    protected abstract fun init(savedInstanceState: Bundle?)

    @CallSuper
    protected open fun onCreateViewAfterBinding() {
        mIsViewBinding = true
        checkLoad()
    }


    private fun checkLoad() {
        if (!mHadLoaded && mIsViewBinding && mIsVisibleToUser) {
            onLazyLoad()
            mHadLoaded = true
        }
    }


    open fun onLazyLoad() {}

    protected open fun beforeCreateView() {}

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisibleToUser = isVisibleToUser
        if (isVisibleToUser) {
            checkLoad()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.base_fragment_base, container, false)
        rootView = view.findViewById(R.id.appbase_fr_root)
        mActivity = activity
        mBundle = arguments
        val mContentView = inflater.inflate(getLayoutResID(), null)
        if (mContentView != null) {
            rootView!!.addView(mContentView)
        }
        createLoadingView()
        if (useEventBus()) EventBus.getDefault().register(this)
        if (useARouter()) ARouter.getInstance().inject(this)
        val parent = rootView?.parent
        return rootView
    }


    /**
     *
     * 创建加载弹框
     */
    private fun createLoadingView(): LoadingFragment? {
        mLoadingFragment = LoadingFragment.Companion.showSelf()
        return mLoadingFragment
    }

    /**
     * 是否使用EventBus,默认为true
     *
     * @return
     */
    open fun useEventBus(): Boolean {
        return true
    }

    /**
     * 是否使用ARouter，默认为true
     *
     * @return
     */
    open fun useARouter(): Boolean {
        return true
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
        onCreateViewAfterBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mIsViewBinding = false
        mIsVisibleToUser = false
        mHadLoaded = false
        (rootView?.parent as? ViewGroup)?.removeView(rootView)
    }


    override fun showLoading() {
        mLoadingFragment?.show(childFragmentManager)
    }

    override fun hideLoading() {
        mLoadingFragment?.dismiss()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus())
            EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(event: EventBusParams?) {
    }

    override fun getMyContext(): Context {
        return context!!
    }
}
