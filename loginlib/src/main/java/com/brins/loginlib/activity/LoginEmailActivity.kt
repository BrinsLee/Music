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
import com.brins.baselib.route.RouterHub.Companion.LOGINEMAILACTIVITY
import com.brins.baselib.route.RouterHub.Companion.LOGINPHONEACTIVITY
import com.brins.baselib.utils.GsonUtils
import com.brins.baselib.utils.KeyboardUtils
import com.brins.baselib.utils.SpUtils
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.eventbus.EventBusKey.KEY_EVENT_LOGIN_SUCCESS
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.baselib.widget.CommonHeaderView
import com.brins.loginlib.R
import com.brins.loginlib.contract.LoginContract
import com.brins.loginlib.presenter.LoginPresenter
import com.brins.networklib.helper.ApiHelper.launch
import kotlinx.android.synthetic.main.activity_login_email.*
import kotlinx.android.synthetic.main.activity_login_email.et_password
import kotlinx.android.synthetic.main.activity_login_email.et_username

@Route(path = LOGINEMAILACTIVITY)
class LoginEmailActivity : BaseMvpActivity<LoginPresenter>(), View.OnClickListener,
    LoginContract.View {

    override fun getLayoutResId(): Int {
        return R.layout.activity_login_email
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        bt_email_login.setOnClickListener(this)
        login_type.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        et_username.postDelayed({
            KeyboardUtils.showSoftInput(et_username)
        }, 300)
    }

    fun onCancelInput(view: View) {}

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.login_type -> {
                ARouterUtils.go(LOGINPHONEACTIVITY)
                finish()
            }
            R.id.bt_email_login -> {
                if (et_username.text.isNullOrEmpty() || et_password.text.isNullOrEmpty()) {
                    ToastUtils.show(getString(R.string.login_error), Toast.LENGTH_SHORT)
                    return
                }
                showLoading()
                KeyboardUtils.hideSoftInput(this)
                launch({
                    mPresenter?.emailLogin(et_username.text.toString(), et_password.text.toString())
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
        EventBusManager.post(KEY_EVENT_LOGIN_SUCCESS)
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

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(this)
    }
}