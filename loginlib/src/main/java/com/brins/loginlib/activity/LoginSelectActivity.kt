package com.brins.loginlib.activity

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.brins.baselib.activity.BaseActivity
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.baselib.route.RouterHub.Companion.LOGINSELECTACTIVITY
import com.brins.baselib.widget.CommonHeaderView
import com.brins.loginlib.R
import kotlinx.android.synthetic.main.activity_login_select.*

@Route(path = LOGINSELECTACTIVITY)
class LoginSelectActivity : BaseActivity(), View.OnClickListener {


    override fun getLayoutResId(): Int {
        return R.layout.activity_login_select
    }

    override fun init(savedInstanceState: Bundle?) {
        header.setOnBackClickListener(object : CommonHeaderView.OnBackClickListener {
            override fun onBackClick(view: View) {
                finish()
            }

        })
        btn_email_login.setOnClickListener(this)
        btn_phone_login.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btn_email_login -> {
                ARouterUtils.go(RouterHub.LOGINEMAILACTIVITY)
                finish()
            }
            R.id.btn_phone_login -> {
                ARouterUtils.go(RouterHub.LOGINPHONEACTIVITY)
                finish()
            }
        }
    }
}