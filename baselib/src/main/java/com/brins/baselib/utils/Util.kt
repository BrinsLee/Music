package com.brins.baselib.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.*
import android.icu.util.Calendar
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.text.format.Time
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.collection.SimpleArrayMap
import com.brins.baselib.R
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 获取当前进程名
 * @param context
 * @return
 */
fun getCurrProcessName(context: Context): String? {
    try {
        val currProcessId = android.os.Process.myPid()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfos = am.runningAppProcesses
        if (processInfos != null) {
            for (info in processInfos) {
                if (info.pid == currProcessId) {
                    return info.processName
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}


/**
 * 设置状态栏文字颜色
 *
 * @param window
 * @param isDark
 */
fun setTextDark(window: Window, isDark: Boolean) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        val decorView = window.decorView
        val systemUiVisibility = decorView.systemUiVisibility
        if (isDark) {
            decorView.systemUiVisibility =
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decorView.systemUiVisibility =
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

fun hideStatusBar(activity: Activity) {
    val window = activity.window
    val decorView = window.decorView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    } else {
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}

/**
 * 设置状态栏透明
 *
 * @param activity
 */
fun setTranslucent(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val decorView = window.decorView
        val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}

/**
 * 获取状态栏高度
 *
 * @param context
 * @return
 */
fun getStatusBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return context.resources.getDimensionPixelSize(resourceId)
}

val TYPEFACE_CACHE = SimpleArrayMap<String, WeakReference<Typeface>>()

fun formatDuration(duration: Int): String {
    val durationSecond = duration / 1000
    var minute = durationSecond / 60
    val hour = minute / 60
    minute %= 60
    val second = durationSecond % 60
    return if (hour != 0)
        String.format("%2d:%02d:%02d", hour, minute, second)
    else
        String.format("%02d:%02d", minute, second)


}

fun getCalendarDay(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
    } else {
        val time = Time()
        time.setToNow()
        time.monthDay.toString()
    }
}

fun getDateToString(time: Long): String? {
    val d = Date(time)
    val sf = SimpleDateFormat("yyyy/MM/dd/ HH:mm")
    return sf.format(d)
}

fun getDateToString(time: Long, format: String?): String? {
    val d = Date(time * 1000)
    val sf = SimpleDateFormat(format)
    return sf.format(d)
}

fun createRadialGradientBitmap(
    context: Context,
    darkColor: Int,
    color: Int
): Bitmap {
    val bgColors = IntArray(3)
    bgColors[0] = color
    bgColors[1] = color
    bgColors[2] = Color.WHITE
    val stop = floatArrayOf(0.2f, 0.2f, 0.9f)

    val bgBitmap = Bitmap.createBitmap(
        UIUtils.getScreenWidth(context),
        UIUtils.getScreenHeight(context),
        Bitmap.Config.RGB_565
    )
    val canvas = Canvas()
    val paint = Paint()
    canvas.setBitmap(bgBitmap)
    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR)
    val gradient = RadialGradient(
        bgBitmap.width.toFloat() - 100,
        -bgBitmap.width.toFloat() / 3,
        bgBitmap.width.toFloat(),
        bgColors,
        stop,
        Shader.TileMode.CLAMP
    )
    paint.shader = gradient
    paint.isAntiAlias = true
    val rectF = RectF(0f, 0f, bgBitmap.width.toFloat(), bgBitmap.height.toFloat())
    canvas.drawRoundRect(rectF, 20f, 20f, paint)
    canvas.drawRect(rectF, paint)
    return bgBitmap
}

fun setAlpha(fraction: Float, color: Int): Int {
    val r = color shr 16 and 0xff
    val g = color shr 8 and 0xff
    val b = color and 0xff

    return (256 * fraction).toInt() shl 24 or (r shl 16) or (g shl 8) or b
}

fun <T> Single<T>.subscribeDbResult(
    onSuccess: (data: T) -> Unit,
    onFailed: (e: Throwable) -> Unit
) {
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : SingleObserver<T> {
            override fun onSuccess(t: T) {
                onSuccess(t)
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                onFailed(e)
            }


        })
}

fun Completable.subscribeDbResult(
    onSuccess: (data: Boolean) -> Unit,
    onFailed: (e: Throwable) -> Unit
) {
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : CompletableObserver {
            override fun onComplete() {
                onSuccess(true)
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                onFailed(e)
            }


        })
}

/**
 *
 * 正则 @xxx: @(\S+):
 * @param content
 * @return
 */
val REGEX_NICKNAME = "@(\\S+) "
fun getUserModel(content: String?): ArrayList<UserModel>? {

    if (content.isNullOrEmpty()) {
        return null
    }
    val strs: ArrayList<UserModel> = ArrayList()
    val r: Pattern = Pattern.compile(REGEX_NICKNAME)
    val m: Matcher = r.matcher(content)
    while (m.find()) {
        strs.add(UserModel(m.group(), m.group()))
    }
    return strs
}

/**
 *
 * 正则 #xxx#: #(\S+)#
 * @param context
 * @return
 */
val REGEX_TOPIC = "#(\\S+)#"
fun getTopicModel(content: String?): ArrayList<TopicModel>? {
    if (content.isNullOrEmpty()) {
        return null
    }
    val strs: ArrayList<TopicModel> = ArrayList()
    val r: Pattern = Pattern.compile(REGEX_TOPIC)
    val m: Matcher = r.matcher(content)
    while (m.find()) {
        strs.add(TopicModel(m.group(), m.group()))
    }
    return strs
}

fun string2Bitmap(bitmapString: String?): Bitmap? {
    var bitmap: Bitmap? = null
    if (bitmapString != null) {
        try {
            val b = Base64.decode(bitmapString, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(
                b, 0,
                b.size
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    return bitmap ?: BitmapFactory.decodeResource(
        UIUtils.getContext().resources,
        R.drawable.base_icon_default_cover
    )
}

//高斯模糊
fun rsBlur(context: Context, source: Bitmap, radius: Float): Bitmap {

    var inputBmp = source
    //(1)
    val renderScript = RenderScript.create(context)

    Log.i("Render", "scale size:" + inputBmp.width + "*" + inputBmp.height)

    // Allocate memory for Renderscript to work with
    //(2)
    val input = Allocation.createFromBitmap(renderScript, inputBmp)
    val output = Allocation.createTyped(renderScript, input.type)
    //(3)
    // Load up an instance of the specific script that we want to use.
    val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    //(4)
    scriptIntrinsicBlur.setInput(input)
    //(5)
    // Set the blur radius
    scriptIntrinsicBlur.setRadius(radius)
    //(6)
    // Start the ScriptIntrinisicBlur
    scriptIntrinsicBlur.forEach(output)
    //(7)
    // Copy the output to the blurred bitmap
    output.copyTo(inputBmp)
    //(8)
    renderScript.destroy()

    return inputBmp
}