package com.brins.lightmusic

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Environment
import com.alibaba.android.arouter.launcher.ARouter
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.config.MAIN_PROCESS_NAME
import com.brins.baselib.database.factory.DatabaseFactory
import com.brins.baselib.module.like.UserLikeMusicResult
import com.brins.baselib.utils.*
import com.brins.baselib.utils.SpUtils.*
import com.brins.bridgelib.BridgeInterface
import com.brins.bridgelib.factory.Factory
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.dailylib.bridge.DailyMusicBridge
import com.brins.eventdetaillib.bridge.EventDetailBridge
import com.brins.find.bridge.FindBridge
import com.brins.home.bridge.HomeBridge
import com.brins.lightmusic.bridge.AppBridge
import com.brins.lightmusic.ui.MainActivity
import com.brins.loacl.bridge.LocalBridge
import com.brins.loginlib.bridge.LoginBridge
import com.brins.mine.bridge.MineBridge
import com.brins.musicdetail.bridge.MusicDetailBridge
import com.brins.musiclistlib.bridge.MusicListBridge
import com.brins.musicsquarelib.bridge.MusicSquareBridge
import com.brins.picturedetaillib.bridge.PictureDetailBridge
import com.brins.radiolib.bridge.RadioBridge
import com.brins.searchlib.bridge.SearchBridge
import com.brins.video.bridge.VideoBridge
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
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
            initBeta()
            initBugly()
            registerBridge()
        }
    }

    private fun initBeta() {
        Beta.autoInit = true
        Beta.autoCheckUpgrade = true
        Beta.upgradeCheckPeriod = 24 * 3600 * 1000
        Beta.initDelay = 3 * 1000
        Beta.largeIconId = R.mipmap.ic_launcher
        Beta.smallIconId = R.mipmap.ic_launcher
        Beta.defaultBannerId = R.mipmap.ic_launcher
        Beta.storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.canShowUpgradeActs.add(MainActivity::class.java)
        Beta.canAutoDownloadPatch = true
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = true
        Beta.upgradeDialogLayoutId = R.layout.dialog_update
        Beta.installTinker()
    }

    private fun initBugly() {
        val strategy = UserStrategy(this).also {
            it.setAppChannel("1001")
            it.setAppVersion(BuildConfig.VERSION_NAME)
        }
//        CrashReport.initCrashReport(this, "f6502c9d26", true, strategy)
        Bugly.init(this, "f6502c9d26", true, strategy)

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
            .register(MineBridge::class.java)
            .register(MusicListBridge::class.java)
            .register(MusicSquareBridge::class.java)
            .register(DailyMusicBridge::class.java)
            .register(LoginBridge::class.java)
            .register(MusicDetailBridge::class.java)
            .register(SearchBridge::class.java)
            .register(FindBridge::class.java)
            .register(PictureDetailBridge::class.java)
            .register(EventDetailBridge::class.java)
            .register(VideoBridge::class.java)
            .register(RadioBridge::class.java)
            .register(LocalBridge::class.java)
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

    override fun onTerminate() {
        super.onTerminate()
        Beta.unInit()
    }
}