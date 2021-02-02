package com.brins.video.widget

import android.content.Context
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import cn.jzvd.JZUtils
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.brins.baselib.utils.SizeUtils.dp2px
import com.brins.video.R

/**
 * Created by lipeilin
 * on 2021/1/29
 */
class JZVideoPlayerView constructor(
    context: Context,
    attrs: android.util.AttributeSet
) : JzvdStd(context, attrs) {
    override fun getLayoutId(): Int {
        return R.layout.jz_layout_std
    }

    companion object {
        fun backPress(): Boolean {
            return JzvdStd.backPress()
        }
    }

    override fun gotoScreenNormal() {
        //goback本质上是goto
        gobakFullscreenTime = System.currentTimeMillis() //退出全屏

        val vg = JZUtils.scanForActivity(context).window
            .decorView as ViewGroup
        vg.removeView(this)
//        Jzvd.CONTAINER_LIST.last.removeAllViews()
        val layoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layoutParams.topToTop = Jzvd.CONTAINER_LIST.last.id
        layoutParams.topMargin = dp2px(45f)
        Jzvd.CONTAINER_LIST.last.addView(this, layoutParams)
        Jzvd.CONTAINER_LIST.pop()

        setScreenNormal() //这块可以放到jzvd中

        JZUtils.showStatusBar(context)
        JZUtils.setRequestedOrientation(context, Jzvd.NORMAL_ORIENTATION)
        JZUtils.showSystemUI(context)
    }
}