package com.brins.mine.viewholder

import android.view.View
import com.brins.baselib.widget.RoundConstraintLayout
import com.brins.mine.R
import com.brins.networklib.login.UnLoginData
import com.chad.library.adapter.base.BaseViewHolder

class UnLoginViewHolder(itemView: View) : BaseViewHolder<UnLoginData>(itemView) {

    val container: RoundConstraintLayout = itemView.findViewById(R.id.cl_unlogin)
}