package com.brins.baselib.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import java.util.*

class ActivityManager private constructor() {

    private var mActivityStack: Stack<Activity> = Stack()

    companion object {
        val INSTANCE: ActivityManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ActivityManager()
        }
    }

    fun startActivity(clazz: Class<*>?) {
        startActivity(clazz, null)
    }

    fun startActivity(clazz: Class<*>?, bundle: Bundle?) {
        if (currentActivity() == null) return
        val intent = Intent(currentActivity(), clazz)
        if (bundle != null) intent.putExtras(bundle)
        currentActivity()?.startActivity(intent)
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity 页面实例
     * @return
     */
    fun addActivity(activity: Activity?) {
        if (activity == null) return
        mActivityStack.add(activity)
    }

    /**
     * 从堆栈移除Activity
     *
     * @param activity 页面实例
     * @return
     */
    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            mActivityStack.remove(activity)
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     *
     * @return activity 页面实例
     */
    fun currentActivity(): Activity? {
        var activity: Activity? = null
        try {
            if (mActivityStack.size > 0) {
                activity = mActivityStack.lastElement()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return activity
    }

    /**
     * 获取当前Activity的前一个activity
     *
     * @return activity 页面实例
     */
    fun preActivity(): Activity? {
        var activity: Activity? = null
        try {
            if (mActivityStack.size > 1) {
                activity = mActivityStack[mActivityStack.size - 2]
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return activity
    }

    /**
     * 判断某一个activity是否为当前activity
     *
     * @param activity 页面实例
     * @return
     */
    fun isCurrent(activity: Activity?): Boolean {
        if (activity == null || currentActivity() == null) return false
        return activity === currentActivity()
    }

    /**
     * 判断某一个activity是否为当前activity
     *
     * @param clazz 页面类
     * @return
     */
    fun isCurrent(clazz: Class<*>?): Boolean {
        if (clazz == null || currentActivity() == null) return false
        return currentActivity()!!::class.java == clazz
    }

    /**
     * activity栈中是否存在该activity
     *
     * @param clazzStr 页面类名
     * @return
     */
    fun hasActivity(clazzStr: String): Boolean {
        return try {
            val cls = Class.forName(clazzStr)
            hasActivity(cls)
        } catch (e: java.lang.Exception) {
            false
        }
    }

    /**
     * activity栈中是否存在该activity
     *
     * @param clazz 页面类名
     * @return
     */
    fun hasActivity(clazz: Class<*>?): Boolean {
        if (clazz == null) return false
        for (i in 0 until activityCounts()) {
            val activity = mActivityStack[i]
            if (activity.javaClass == clazz) {
                return true
            }
        }
        return false
    }

    /**
     * 判断堆栈中是否存在该页面实例
     *
     * @param activity 页面实例
     * @return
     */
    fun hasActivity(activity: Activity?): Boolean {
        if (activity == null) return false
        for (i in 0 until activityCounts()) {
            val activitys = mActivityStack[i]
            if (activitys === activity) return true
        }
        return false
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        try {
            val activity = mActivityStack.lastElement()
            finishActivity(activity)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity 页面实例
     */
    fun finishActivity(activity: Activity?) {
        if (activity == null) return
        try {
            if (activity != null) {
                if (mActivityStack.size == 1) { //兼容activity stack为空，调不起新的activity-----后台弹出界面
                    activity.window.decorView.postDelayed({
                        mActivityStack.remove(activity)
                        activity.finish()
                    }, 500)
                } else {
                    mActivityStack.remove(activity)
                    activity.finish()
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param clazz 页面类
     */
    fun finishActivity(clazz: Class<*>?) {
        if (clazz == null) return
        try {
            for (activity in mActivityStack) {
                if (activity.javaClass == clazz) {
                    finishActivity(activity)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 结束所有Activity,除了当前Activity
     *
     * @param clazz 页面类
     */
    fun finishAllActivityExceptOne(clazz: Class<*>?) {
        if (clazz == null) return
        for (i in 0 until activityCounts()) {
            val activity = mActivityStack[i]
            if (activity.javaClass != clazz) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 关闭直到此activity
     *
     * @param clazz 页面类（如果clazz为空，则关闭直至栈顶的Activity）
     */
    fun finishUtilThisActivity(clazz: Class<*>?) {
        val length: Int = activityCounts()
        if (clazz == null) {
            for (i in 0 until length - 1) {
                finishActivity(mActivityStack.lastElement())
            }
            return
        }
        for (i in 0 until length) {
            val activity = mActivityStack.lastElement()
            if (activity.javaClass != clazz) {
                finishActivity(activity)
            } else {
                break
            }
        }
    }

    /**
     * 结束所有Activity,除了当前Activity
     */
    fun finishAllActivityExceptCurrent() {
        val currentActivity = currentActivity()
        if (currentActivity != null) {
            finishAllActivityExceptOne(currentActivity.javaClass)
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        try {
            for (i in mActivityStack.indices) {
                if (null != mActivityStack[i]) {
                    mActivityStack[i].finish()
                }
            }
            mActivityStack.clear()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取指定类名的Activity
     *
     * @param clazz 页面类
     */
    fun getActivity(clazz: Class<*>?): Activity? {
        if (clazz == null) return null
        try {
            for (activity in mActivityStack) {
                if (activity.javaClass == clazz) {
                    return activity
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getActivity(name: String?): Activity? {
        if (name == null) return null
        try {
            for (activity in mActivityStack) {
                if (activity.javaClass.simpleName == name) {
                    return activity
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 拿到应用程序当前活跃Activity的个数
     *
     * @return counts Activity的个数
     */
    fun activityCounts(): Int {
        var counts = 0
        if (mActivityStack.size > 0) {
            counts = mActivityStack.size
        }
        return counts
    }

    /**
     * 退出应用程序
     */
    fun exit() {
        try {
            if (currentActivity() != null) finishAllActivity()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 是否在后台
     * @param context
     * @param className
     * @return
     */
    fun isForeground(
        context: Context?,
        className: String
    ): Boolean {
        if (context == null || TextUtils.isEmpty(className)) return false
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
        val list =
            am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className.contains(cpn.className)) {
                return true
            }
        }
        return false
    }

    fun getLauncherActivity(pkg: String): String? {
        if (UIUtils.isSpace(pkg)) return ""
        val intent =
            Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(pkg)
        val pm = AppUtils.getContext().packageManager
        val info =
            pm.queryIntentActivities(intent, 0)
        return if (info == null || info.size == 0) {
            ""
        } else info[0].activityInfo.name
    }

    fun isActivityNotFinish(activity: Activity?): Boolean {
        return activity != null && !activity.isFinishing && !activity.isDestroyed
    }

}