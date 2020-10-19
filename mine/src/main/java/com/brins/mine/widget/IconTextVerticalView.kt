package com.brins.mine.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.brins.mine.R

/**
 * Created by lipeilin
 * on 2020/10/15
 */
class IconTextVerticalView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) : LinearLayout(context, attributeSet, def) {

    val mIcon: ImageView
    val mTitle: TextView
    val mSubTitle: TextView
    private val mTitleColor = Color.BLACK
    private val mSubTitleColor = Color.GRAY
    private var mIconSrc = R.drawable.base_icon_like_heart_black
    private var mTitleStr: String? = ""
    private var mSubTitleStr: String? = ""

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.mine_icon_text_vertical_view, this, true)
        mIcon = view.findViewById(R.id.iv_icon)
        mTitle = view.findViewById(R.id.tv_title)
        mSubTitle = view.findViewById(R.id.tv_subtitle)
        iniView(attributeSet)
    }

    private fun iniView(attributeSet: AttributeSet?) {
        attributeSet?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.IconTextVerticalView)
            mIconSrc = a.getResourceId(
                R.styleable.IconTextVerticalView_icon_src,
                R.drawable.base_icon_like_heart_black
            )
            mIcon.setImageResource(mIconSrc)
            mTitleStr = a.getString(R.styleable.IconTextVerticalView_icon_title)
            mSubTitleStr = a.getString(R.styleable.IconTextVerticalView_icon_subtitle)
            mTitle.setText(mTitleStr)
            mSubTitle.setText(mSubTitleStr)
            val mTitleColor =
                a.getColor(R.styleable.IconTextVerticalView_icon_title_color, Color.BLACK)
            mTitle.setTextColor(mTitleColor)
            val mSubColor =
                a.getColor(R.styleable.IconTextVerticalView_icon_subtitle_color, Color.GRAY)
            mSubTitle.setTextColor(mSubColor)
            a.recycle()
        }
    }
}