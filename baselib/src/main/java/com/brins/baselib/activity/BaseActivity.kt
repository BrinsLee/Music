package com.brins.baselib.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder
import com.alibaba.android.arouter.launcher.ARouter
import com.brins.baselib.R
import com.brins.baselib.fragment.LoadingFragment
import com.brins.baselib.mvp.IView
import com.brins.baselib.utils.ActivityManager
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.baselib.utils.setTextDark
import com.brins.baselib.utils.setTranslucent
import kotlinx.android.synthetic.main.base_activity_base.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity : AppCompatActivity(), IView {

    var mContext: Context? = null
    var mBundle: Bundle? = null
    private lateinit var mRootView: FrameLayout
    var mLoadingFragment: LoadingFragment? = null
    private var mUnBinder: Unbinder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityManager.INSTANCE.addActivity(this)
        setContentView(R.layout.base_activity_base)
        mContext = this
        mBundle = intent.extras
        mRootView = appbase_fr_root
        val mContentView = View.inflate(this, getLayoutResId(), null)
        if (mContentView != null) {
            mRootView.addView(mContentView)
        }
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        if (useButterKinfe()) {
            mUnBinder = ButterKnife.bind(this, mRootView)
        }
        ARouter.getInstance().inject(this)
        createLoadingView()
        init(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        if (hideToolBar())
            setStatusBar()
        Log.d("BaseActivity", "onStart")
    }

    protected abstract fun getLayoutResId(): Int

    abstract fun init(savedInstanceState: Bundle?)

    /**
     *
     * 创建加载弹框
     */
    private fun createLoadingView(): LoadingFragment? {
        mLoadingFragment = LoadingFragment.Companion.showSelf()
        return mLoadingFragment
    }

    /**
     * 是否沉浸式状态栏,默认为true
     *
     * @return
     */
    open fun hideToolBar(): Boolean {
        return true
    }

    /**
     * 是否使用EventBus
     * @return
     */
    open fun useEventBus(): Boolean {
        return true
    }

    /**
     * 是否使用ButterKnife，默认为true
     *
     * @return
     */
    open fun useButterKinfe(): Boolean {
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

    /**
     * 设置状态栏
     */
    protected open fun setStatusBar() {
//        StatusBarHelper.getInstance().setWindowTranslucentStatus(this)
        setTranslucent(this)
        setTextDark(window, true)
    }

    /**
     * home键默认行为
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // 默认返回键行为
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 返回键默认行为
     */
    override fun onBackPressed() {
        // 返回键返回事件
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            // 相当于getSupportFragmentManager().popBackStack();
            super.onBackPressed()
        }
    }

    override fun finish() {
        ActivityManager.INSTANCE.removeActivity(this)
        super.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onEvent(event: EventBusParams?) {
    }

    override fun getMyContext(): Context {
        return mContext!!
    }

    override fun showLoading() {
        mLoadingFragment?.show(supportFragmentManager)

    }

    override fun hideLoading() {
        mLoadingFragment?.dismiss()
        mLoadingFragment = null
    }
}