package com.brins.lightmusic

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.brins.baselib.cache.login.LoginCache
import com.brins.bridgelib.BridgeInterface
import com.brins.bridgelib.factory.Factory
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.baselib.config.MAIN_PROCESS_NAME
import com.brins.baselib.database.factory.DatabaseFactory
import com.brins.baselib.module.like.UserLikeMusicResult
import com.brins.baselib.utils.*
import com.brins.baselib.utils.SpUtils.*
import com.brins.home.bridge.HomeBridge
import com.brins.lightmusic.bridge.AppBridge
import dagger.hilt.android.HiltAndroidApp

//import io.reactivex.plugins.RxJavaPlugins

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        @JvmStatic
        var sInstance: BaseApplication? = null

        fun getInstance(): BaseApplication {
            if (sInstance == null) {
                throw NullPointerException("please inherit com.brins.lightmusic.BaseApplication or call setApplication.")
            }
            return sInstance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
//        RxJavaPlugins.setErrorHandler {
//            //异常处理
//        }
        setApplication(this)
        if (isMainProcess(this)) {/*
            initRxJava()
            initUserData()*/
            UIUtils.init(this)
            AppUtils.init(this)
            initUserData()
            initArouter()
            registerBridge()
        }
    }

    private fun initUserData() {
        LoginCache.isLogin = obtain(SP_USER_INFO, this).getBoolean(KEY_IS_LOGIN, false)
        LoginCache.UserCookie = obtain(SP_USER_INFO, this).getString(KEY_COOKIE, "")
        LoginCache.likeResult = GsonUtils.fromJson(
            obtain(SP_USER_INFO, this).getString(KEY_USER_LIKE, ""),
            UserLikeMusicResult::class.java
        )
        if (LoginCache.isLogin) {
            DatabaseFactory.getUserInfo().subscribeDbResult({
                LoginCache.userAccount = it
            }, {
                it.printStackTrace()
            })

            DatabaseFactory.getUserProfile().subscribeDbResult({
                LoginCache.userProfile = it
            }, {
                it.printStackTrace()
            })
        }
    }

    private fun initArouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }


    @Synchronized
    private fun setApplication(baseApplication: BaseApplication) {
        sInstance = baseApplication
        baseApplication.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
//                AppManager.getAppManager().removeActivity(activity)
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
//                AppManager.getAppManager().addActivity(activity)
            }

        })
    }


    private fun isMainProcess(context: Context): Boolean {
        return MAIN_PROCESS_NAME == getCurrProcessName(context)
    }

    @Suppress("UNCHECKED_CAST")
    private fun registerBridge() {
        BridgeProviders.instance.register(AppBridge::class.java
            , object : Factory {
                override fun <T : BridgeInterface> create(bridgeClazz: Class<T>): T {
                    return AppBridge() as T
                }

            })
            .register(HomeBridge::class.java)
    }


    private fun initRxJava() {
//        RxJavaPlugins.setErrorHandler { Log.e("RxJava", "RX error handler") }
    }

/*    private fun initUserData() {
        AppConfig.isLogin = SpUtils.obtain(SP_USER_INFO, this).getBoolean(KEY_IS_LOGIN, false)
        AppConfig.UserCookie = SpUtils.obtain(SP_USER_INFO, this).getString(KEY_COOKIE, "")
        if (AppConfig.isLogin) {
            DatabaseFactory.getUserInfo().subscribeDbResult({
                AppConfig.userAccount = it
            }, {
                it.printStackTrace()
            })

            DatabaseFactory.getUserProfile().subscribeDbResult({
                AppConfig.userProfile = it
            }, {
                it.printStackTrace()
            })
        }
    }*/


}