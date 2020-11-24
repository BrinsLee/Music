package com.brins.searchlib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.brins.baselib.utils.UIUtils
import com.brins.searchlib.R

/**
 * Created by lipeilin
 * on 2020/11/19
 */
class FollowTextView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    def: Int = 0
) : RelativeLayout(context, attributes, def) {

    private val rlAdd: RelativeLayout
    private val ivAdd: ImageView
    private val tvFollow: TextView
    private val mRotatEAnimation: RotateAnimation by lazy {
        RotateAnimation(
            0f, 359 * 20f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.search_follow_view, this, true)
        rlAdd = view.findViewById(R.id.rl_add)
        ivAdd = view.findViewById(R.id.iv_add)
        tvFollow = view.findViewById(R.id.tv_follow)
    }

    fun startLoading() {
        ivAdd.setImageResource(R.drawable.search_icon_loading)
        mRotatEAnimation.apply {
            duration = 800 * 20
            repeatCount = Animation.INFINITE
            repeatMode = Animation.RESTART
            interpolator = LinearInterpolator()
        }
        ivAdd.startAnimation(mRotatEAnimation)
    }

    fun followSuccess() {
        mRotatEAnimation.cancel()
        ivAdd.setImageResource(R.drawable.search_icon_check)
        tvFollow.text = context.getString(R.string.followed)
        tvFollow.setTextColor(UIUtils.getColor(R.color.default_btn_text))
        rlAdd.setBackgroundResource(R.drawable.search_bg_follow_grey_stroke)
    }

}