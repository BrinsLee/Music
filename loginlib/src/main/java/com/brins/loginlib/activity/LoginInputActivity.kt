package com.brins.loginlib.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseMvpActivity
import com.brins.baselib.cache.login.LoginCache
import com.brins.baselib.module.userlogin.UserLoginResult
import com.brins.baselib.route.RouterHub.Companion.LOGININPUTACTIVITY
import com.brins.baselib.utils.GsonUtils
import com.brins.baselib.utils.KeyboardUtils
import com.brins.baselib.utils.SpUtils
import com.brins.baselib.utils.ToastUtils
import com.brins.baselib.utils.eventbus.EventBusKey.KEY_EVENT_LOGIN_SUCCESS
import com.brins.baselib.utils.eventbus.EventBusManager
import com.brins.loginlib.R
import com.brins.loginlib.contract.LoginContract
import com.brins.loginlib.presenter.LoginPresenter
import com.brins.networklib.helper.ApiHelper.launch
import kotlinx.android.synthetic.main.activity_login_input.*

@Route(path = LOGININPUTACTIVITY)
class LoginInputActivity : BaseMvpActivity<LoginPresenter>(), View.OnClickListener,
    LoginContract.View {

    override fun getLayoutResId(): Int {
        return R.layout.activity_login_input
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        bt_email_login.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        et_username.postDelayed({
            KeyboardUtils.showSoftInput(et_username)
        }, 300)
    }

    fun onCancelInput(view: View) {}
    override fun onClick(p0: View?) {
        showLoading()
        KeyboardUtils.hideSoftInput(this)
        launch({
            mPresenter?.emailLogin(et_username.text.toString(), et_password.text.toString())
        }, {
            ToastUtils.show(getString(R.string.login_error), Toast.LENGTH_SHORT)
            hideLoading()
        })
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
        TODO("Not yet implemented")
    }

    override fun onLogoutFail(msg: String) {
        TODO("Not yet implemented")
    }
}