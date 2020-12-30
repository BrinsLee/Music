package com.brins.baselib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.brins.baselib.R

/**
 * Created by lipeilin
 * on 2020/12/30
 */
class ImageTextView(context: Context, attributeSet: AttributeSet?, def: Int) :
    LinearLayout(context, attributeSet, def) {


    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    val mImageView: ImageView
    val mTextView: TextView

    init {

        val view = LayoutInflater.from(context).inflate(R.layout.base_image_text_view, this, true)
        mImageView = view.findViewById(R.id.iv_icon)
        mTextView = view.findViewById(R.id.tv_num)
        initView(context, attributeSet)
    }

    private fun initView(context: Context, attributeSet: AttributeSet?) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTextView)
        val text = a.getString(R.styleable.ImageTextView_itv_text)
        mTextView.text = text

        val image = a.getResourceId(
            R.styleable.ImageTextView_itv_drawable,
            R.drawable.base_icon_event_share
        )
        mImageView.setImageResource(image)
        a.recycle()
    }

    fun getText(): String {
        return mTextView.text.toString()
    }

    fun setText(text: String) {
        mTextView.text = text
    }


}