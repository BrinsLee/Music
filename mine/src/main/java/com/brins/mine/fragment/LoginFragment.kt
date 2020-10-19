package com.brins.mine.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.UIUtils
import com.brins.mine.R
import com.brins.mine.viewmodel.MineViewModel
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseMvvmFragment<MineViewModel>() {

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MineViewModel.getInstance(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_login
    }

    override fun reLoad() {
        TODO("Not yet implemented")
    }

    override fun needParent(): Boolean {
        return false
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        val spannableFollow = SpannableStringBuilder()
        spannableFollow.append(UIUtils.getString(R.string.follows))
        spannableFollow.append(": 0")
        val relativeSizeSpan = RelativeSizeSpan(0.7f)
        val foreColor = ForegroundColorSpan(Color.GRAY)
        spannableFollow.setSpan(
            relativeSizeSpan,
            3,
            spannableFollow.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableFollow.setSpan(
            foreColor, 3, spannableFollow.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        tv_follows.text = spannableFollow

        val spannableFan = SpannableStringBuilder()
        spannableFan.append(UIUtils.getString(R.string.fans))
        spannableFan.append(": 0")
        val relativeSizeSpan2 = RelativeSizeSpan(0.7f)
        spannableFan.setSpan(
            relativeSizeSpan2,
            3,
            spannableFan.length,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        spannableFan.setSpan(
            foreColor, 3, spannableFan.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE
        )
        tv_fans.text = spannableFan
    }
}