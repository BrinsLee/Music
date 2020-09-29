package com.brins.musicsquarelib.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.musicsquarelib.R
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class PlayListCover @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null,
    def: Int = 0
) : RelativeLayout(context, attributeSet, def) {
    private var mCover: ImageView? = null
    private var mName: TextView? = null
    private var mDrawable: Drawable? = null
    private var mListener: onCoverLoadListener? = null

    fun getDrawable(): Drawable? = mDrawable

    interface onCoverLoadListener {
        fun onCoverLoad(drawable: Drawable)
    }

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.music_square_playlist_cover, this, true)
        mCover = view.findViewById(R.id.iv_cover)
        mName = view.findViewById(R.id.tv_name)
    }

    fun setCoverListener(listener: onCoverLoadListener) {
        this.mListener = listener
    }

    fun setText(text: String?) {
        mName?.text = text
    }

    fun setCover(url: String) {
        GlideHelper.setRoundImageResource(mCover, url, 10, object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                resource?.let {
                    mCover?.setImageDrawable(it)
                    mDrawable = it
                    mListener?.onCoverLoad(it)
                }
                return true
            }

        }, 0)
    }

}