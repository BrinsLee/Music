package com.brins.baselib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.brins.baselib.R

/**
 * Created by lipeilin
 * on 2020/12/29
 */
class ShareMusicListView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    val mCover: ImageView

    val mTitle: TextView

    val mName: TextView

    val mMusicList: TextView

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.base_share_music_list_view, this, true)

        mCover = view.findViewById(R.id.iv_cover)
        mTitle = view.findViewById(R.id.tv_share_name)
        mName = view.findViewById(R.id.tv_share_creator)
        mMusicList = view.findViewById(R.id.tv_music_list)
    }

}