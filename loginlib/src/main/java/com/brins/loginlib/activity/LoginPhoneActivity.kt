package com.brins.loginlib.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.cache.like.LikeCache
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.module.userlogin.UserLoginResult
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.route.RouterHub.Companion.LOGINPHONEACTIVITY
import com.brins.baselib.utils.GsonUtils
import com.brins.baselib.utils.KeyboardUtils
import com.brins.baselib.utils.SpUtils
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.eventbus.EventBusKey
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.baselib.widget.CommonHeaderView
import com.brins.loginlib.R
import com.brins.loginlib.contract.LoginContract
import com.brins.loginlib.presenter.LoginPresenter
import com.brins.networklib.helper.ApiHelper
import kotlinx.android.synthetic.main.activity_login_phone.*

@Route(path = LOGINPHONEACTIVITY)
class LoginPhoneActivity : BaseMvpActivity<LoginPresenter>(), View.OnClickListener,
    LoginContract.View {
    override fun getLayoutResId(): Int {
        return R.layout.activity_login_phone
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        bt_phone_login.setOnClickListener(this)
        login_type.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        et_username.postDelayed({
            KeyboardUtils.showSoftInput(et_username)
        }, 300)
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(this)
    }

    fun onCancelInput(view: View) {}


    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_type -> {
                ARouterUtils.go(RouterHub.LOGINEMAILACTIVITY)
                finish()
            }
            R.id.bt_phone_login -> {
                if (et_username.text.isNullOrEmpty() || et_password.text.isNullOrEmpty()) {
                    ToastUtils.show(getString(R.string.login_error), Toast.LENGTH_SHORT)
                    return
                }
                showLoading()
                KeyboardUtils.hideSoftInput(this)
                ApiHelper.launch({
                    mPresenter?.phoneLogin(et_username.text.toString(), et_password.text.toString())
                }, {
                    ToastUtils.show(getString(R.string.login_error), Toast.LENGTH_SHORT)
                    hideLoading()
                })
            }
        }
    }

    override fun onLoginSuccess(result: UserLoginResult) {
        hideLoading()
        SpUtils.obtain(SpUtils.SP_USER_INFO, this)
            .save(SpUtils.KEY_USER_LIKE, GsonUtils.toJson(LoginCache.likeResult))
        SpUtils.obtain(SpUtils.SP_USER_INFO, this).save(SpUtils.KEY_IS_LOGIN, true)
        EventBusManager.post(EventBusKey.KEY_EVENT_LOGIN_SUCCESS)
        finish()
    }

    override fun onLoginFail(msg: String) {
        hideLoading()
        ToastUtils.show(msg, Toast.LENGTH_SHORT)
    }

    override fun onLogoutSuccess() {
        LoginCache.clear()
        LikeCache.clear()
    }

    override fun onLogoutFail(msg: String) {
    }

}