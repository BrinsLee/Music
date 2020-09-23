package com.brins.baselib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.brins.baselib.R
import kotlinx.android.synthetic.main.base_titlebar_simple.view.*

class SimpleTitleBar @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
) : RelativeLayout(context, attributeSet), View.OnClickListener {


    private var mClickListener: OnTitleBarClickListener? = null

    var mIvLeft: ImageView? = null

    var mTvTitle: TextView? = null

    var mIvRight: ImageView? = null
    private val mTvRight: TextView? = null

    interface OnTitleBarClickListener {
        fun onLeftClick() {}
        fun onRightClick(viewId: Int) {}
        fun onTitleClick(content: String?) {}
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.base_titlebar_simple, this, true)
        iv_left.setOnClickListener(this)
        iv_right.setOnClickListener(this)
        tv_title.setOnClickListener(this)
        tv_right.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        mClickListener?.let {
            when (v.id) {
                R.id.iv_left -> it.onLeftClick()
                R.id.tv_title -> it.onTitleClick(tv_title.text.toString())
                R.id.iv_right -> it.onRightClick(v.id)
            }
        }
    }

    fun setOnTitleBarClickListener(listener: OnTitleBarClickListener?) {
        mClickListener = listener
    }
}