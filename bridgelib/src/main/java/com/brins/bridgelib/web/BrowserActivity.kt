package com.brins.bridgelib.web

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseActivity
import com.brins.baselib.route.RouterHub.Companion.BROWSERACTIVITY
import com.brins.baselib.utils.AppUtils
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.getStatusBarHeight
import com.brins.baselib.widget.SimpleTitleBar
import com.brins.bridgelib.R
import kotlinx.android.synthetic.main.activity_browser.*
import wendu.dsbridge.DWebView

@Route(path = BROWSERACTIVITY)
class BrowserActivity : BaseActivity() {

    @Autowired(name = "KEY_URL")
    lateinit var mUrl: String
    var mDWebView: DWebView? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_browser
    }

    override fun init(savedInstanceState: Bundle?) {
        if (mUrl.isEmpty()) {
            finish()
        }
        tb_browser.setPadding(0, getStatusBarHeight(this), 0, 0)
        tb_browser.setOnTitleBarClickListener(object : SimpleTitleBar.OnTitleBarClickListener {
            override fun onLeftClick() {
                super.onLeftClick()
            }

            override fun onRightClick(viewId: Int) {
                super.onRightClick(viewId)
            }

            override fun onTitleClick(content: String?) {
                super.onTitleClick(content)
            }
        })
        initWebView()
        initWebSetting()
        buildWebViewClient()

    }

    private fun buildWebViewClient() {
        mDWebView?.setWebViewClient(WebViewClient())
        mDWebView?.setWebChromeClient(WebChromeClient())
    }

    private fun initWebSetting() {
        val webSettings: WebSettings = mDWebView!!.settings
        DWebView.setWebContentsDebuggingEnabled(AppUtils.isAppDebug())
        // ua，格式：系统UserAgent+空格+giao#+key/value+空格+key/value+空格+key/value......
        webSettings.javaScriptEnabled = true
        webSettings.saveFormData = true
        // 存储
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.setAppCachePath(UIUtils.getContext().getCacheDir().getAbsolutePath())
        webSettings.setAppCacheEnabled(false)
        // 页面自适应
        webSettings.defaultTextEncodingName = "UTF-8"
        webSettings.useWideViewPort = true
        // 当页面宽度大于WebView宽度时，缩小使页面宽度等于WebView宽度
        webSettings.loadWithOverviewMode = true
        webSettings.textZoom = 100
        // 是否支持多窗口，默认值false
        webSettings.setSupportMultipleWindows(true);
        // 是否可用Javascript(window.open)打开窗口，默认值 false
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSettings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSettings.setAllowFileAccess(false);
    }

    private fun initWebView() {
        mDWebView = DWebView(mContext)
        fl_h5_container.addView(mDWebView)
        mDWebView!!.loadUrl(mUrl)
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }
}