package com.brins.baselib.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.baselib.R
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.makeramen.roundedimageview.RoundedImageView


class RoundCoverImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {

    private var mRotateAnimator: ObjectAnimator
    private var mLastAnimationValue: Long = 0
    private var mImageView: RoundedImageView
    private var mProgress: RoundCoverProgress
    private var mMax = 360
        get() = field


    init {
        val view = LayoutInflater.from(context).inflate(R.layout.base_view_bottom_cover, this)
        mImageView = view.findViewById(R.id.bottom_cover)
        mProgress = view.findViewById(R.id.cover_progress)
        mRotateAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f)
        mRotateAnimator.duration = 7200
        mRotateAnimator.repeatCount = ValueAnimator.INFINITE
        mRotateAnimator.interpolator = LinearInterpolator()
        mRotateAnimator.repeatMode = ValueAnimator.RESTART
    }

    fun startRotateAnimation() {
        mRotateAnimator.cancel()
        mRotateAnimator.start()
    }

    fun cancelRotateAnimation() {
        mLastAnimationValue = 0
        mRotateAnimator.cancel()
    }

    fun pauseRotateAnimation() {
        mLastAnimationValue = mRotateAnimator.currentPlayTime
        mRotateAnimator.cancel()
    }

    fun resumeRotateAnimation() {
        mRotateAnimator.start()
        mRotateAnimator.currentPlayTime = mLastAnimationValue
    }

    fun setImageBitmap(bitmapCover: Bitmap?) {
        mImageView.setImageBitmap(bitmapCover)
    }

    fun setImageBitmap(url: String) {
        GlideHelper.setRoundImageResource(mImageView, url, 50)
    }

    fun getMax(): Int {
        return mMax
    }

    fun setProgress(initProgress: Int) {
        mProgress.newAngle = initProgress.toFloat()
    }
}