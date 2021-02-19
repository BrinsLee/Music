package com.brins.baselib.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.brins.baselib.R

/**
 * Created by lipeilin
 * on 2021/2/18
 */
class PermissionDialog(
    context: Context,
    var name: String,
    var desc: String,
    var onNextClickListener: OnNextClickListener
) : Dialog(context, R.style.Dialog) {

    private var mTvName: TextView? = null
    private var mTvDesc: TextView? = null
    private var mActivity : Activity? = null

    init {
        mActivity = context as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_dialog_permission)
        if (window == null){
            dismiss()
            return
        }
        window?.apply {
            setGravity(Gravity.CENTER)
            setBackgroundDrawableResource(R.color.translucent_000000)
            //设置为全屏dialog
            val params = window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }

        initView()
    }

    private fun initView() {
        mTvName = findViewById(R.id.tv_permissionName)
        mTvDesc = findViewById(R.id.tv_permission_desc)
        mTvName!!.text = name
        mTvDesc!!.text = desc
        findViewById<TextView>(R.id.tv_permission_next).setOnClickListener {
            mActivity?.let {
                if (!it.isFinishing || !it.isDestroyed){
                    dismiss()
                }
                onNextClickListener.onNextClick()
            }

        }
    }

    interface OnNextClickListener {
        fun onNextClick()
    }
}