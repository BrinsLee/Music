package com.brins.mine.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.brins.baselib.utils.ToastUtils
import com.brins.mine.R
import com.brins.networklib.helper.ApiHelper
import com.brins.networklib.helper.ApiHelper.await
import com.brins.networklib.helper.ApiHelper.launch

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
    var mNormalDrawable: Int = 0
    var mActivateDrawable: Int = 0

    init {

        val view = LayoutInflater.from(context).inflate(R.layout.mine_image_text_view, this, true)
        mImageView = view.findViewById(R.id.iv_icon)
        mTextView = view.findViewById(R.id.tv_num)
        initView(context, attributeSet)
    }

    private fun initView(context: Context, attributeSet: AttributeSet?) {
        val a = context.obtainStyledAttributes(attributeSet, R.styleable.ImageTextView)
        val text = a.getString(R.styleable.ImageTextView_itv_text)
        mTextView.text = text

        mNormalDrawable = a.getResourceId(
            R.styleable.ImageTextView_itv_drawable,
            R.drawable.base_icon_event_unlike
        )
        mImageView.setImageResource(mNormalDrawable)
        mActivateDrawable = a.getResourceId(
            R.styleable.ImageTextView_itv_activate_drawable,
            R.drawable.base_icon_event_like
        )
        a.recycle()
    }

    fun getText(): String {
        return mTextView.text.toString()
    }

    fun setText(text: String): ImageTextView {
        mTextView.text = text
        return this
    }

    fun activate(activate: Boolean): ImageTextView {
        if (activate) {
            mImageView.setImageResource(mActivateDrawable)
        } else {
            mImageView.setImageResource(mNormalDrawable)
        }
        return this
    }

    fun likeEvent(threadId: String, finish: (boolean: Boolean) -> Unit) {
        launch({
            val result = ApiHelper.getFindService().likeEvent(threadId = threadId).await()
            if (result.code == 200) {
                activate(true)
                finish(true)
            } else {
                ToastUtils.show(context.getString(R.string.like_error), Toast.LENGTH_SHORT)
            }
        }, {
            ToastUtils.show(R.string.network_error, Toast.LENGTH_SHORT)
        })
    }

    fun unlikeEvent(threadId: String, finish: (boolean: Boolean) -> Unit) {
        launch({
            val result = ApiHelper.getFindService().unlikeEvent(threadId = threadId).await()
            if (result.code == 200) {
                activate(false)
                finish(false)
            } else {
                ToastUtils.show(context.getString(R.string.like_error), Toast.LENGTH_SHORT)
            }
        }, {
            ToastUtils.show(R.string.network_error, Toast.LENGTH_SHORT)
        })
    }

}