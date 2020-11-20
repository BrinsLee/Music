package com.brins.mine.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.View
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.fragment.BaseMvvmFragment
import com.brins.baselib.module.userlogin.UserProfileBean
import com.brins.baselib.mvp.IModel
import com.brins.baselib.mvvm.BaseViewModel
import com.brins.baselib.utils.UIUtils
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusParams
import com.brins.baselib.utils.glidehelper.GlideHelper
import com.brins.bridgelib.login.LoginBridgeInterface
import com.brins.bridgelib.provider.BridgeProviders
import com.brins.mine.R
import com.brins.mine.viewmodel.MineViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LoginFragment : BaseMvvmFragment<MineViewModel>(), View.OnClickListener {

    override fun getViewModel(): BaseViewModel<out IModel>? {
        return MineViewModel.getInstance(mActivity!!.application)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_login
    }

    override fun reLoad() {
    }

    override fun needParent(): Boolean {
        return false
    }


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        if (LoginCache.isLogin && LoginCache.userProfile != null) {
            setUserData(LoginCache.userProfile!!)
        } else {
            tv_follows.text = getFollowOrFanNum(UIUtils.getString(R.string.follows), 0)
            tv_fans.text = getFollowOrFanNum(UIUtils.getString(R.string.fans), 0)
        }

        cl_unlogin.setOnClickListener(this)
    }

    private fun getFollowOrFanNum(decoration: String, num: Int): SpannableStringBuilder {
        val spannableFollow = SpannableStringBuilder()
        spannableFollow.append(decoration)
        spannableFollow.append(": $num")
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
        return spannableFollow
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.cl_unlogin -> {
                BridgeProviders.instance.getBridge(LoginBridgeInterface::class.java)
                    .toLoginSelectActivity()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginSuccess(params: EventBusParams) {
        when (params.key) {
            EventBusKey.KEY_EVENT_LOGIN_SUCCESS -> {
                LoginCache.userProfile?.let {
                    setUserData(it)
                }
            }

        }
    }

    private fun setUserData(it: UserProfileBean) {
        GlideHelper.setRoundImageResource(iv_avatar, it.avatarUrl, 10)
        tv_login.text = it.nickname
        tv_follows.text = getFollowOrFanNum(UIUtils.getString(R.string.follows), it.follows)
        tv_fans.text = getFollowOrFanNum(UIUtils.getString(R.string.fans), it.followeds)
    }
}