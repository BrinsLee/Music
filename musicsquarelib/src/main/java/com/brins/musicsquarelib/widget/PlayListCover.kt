package com.brins.musicsquarelib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.musicsquarelib.R

class PlayListCover @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null,
    def: Int = 0
) : RelativeLayout(context, attributeSet, def) {
    private var mCover: ImageView? = null
    private var mName: TextView? = null

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.music_square_playlist_cover, this, true)
        mCover = view.findViewById(R.id.iv_cover)
        mName = view.findViewById(R.id.tv_name)
    }

    fun setText(text: String?) {
        mName?.text = text
    }

    fun setCover(url: String) {
        GlideHelper.setRoundImageResource(mCover, url, 10)
    }

}