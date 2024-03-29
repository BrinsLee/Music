package com.brins.lightmusic.ui


import android.content.Intent
import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import com.brins.baselib.utils.WeakHandler
import com.brins.lightmusic.R



class SplashActivity : AppCompatActivity() , WeakHandler.IHandler {

    private var mForceGoMain: Boolean = false
    private val AD_TIME_OUT : Long = 2000
    private val MSG_GO_MAIN = 1
    //开屏广告是否已经加载
    private var mHasLoaded: Boolean = false
    private val mHandler = WeakHandler(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        setTranslucent(this)
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT)
    }
    /*
    private fun loadSplashAd() {
        val adSlot = AdSlot.Builder()
            .setCodeId("801121648")
            .setSupportDeepLink(true)
            .setImageAcceptedSize(1080, 1920)
            .build()
        mTTAdNative.loadSplashAd(adSlot , object : TTAdNative.SplashAdListener{
            override fun onSplashAdLoad(p0: TTSplashAd?) {
                mHasLoaded = true
                mHandler.removeCallbacksAndMessages(null)
                if (p0 == null) {
                    return
                }
                val view = p0.splashView
                mSplashContainer.removeAllViews()
                mSplashContainer.addView(view)
                p0.setSplashInteractionListener( object : TTSplashAd.AdInteractionListener{
                    override fun onAdClicked(p0: View?, p1: Int) {
                        Log.d(TAG, "点击开屏广告")
                    }

                    override fun onAdSkip() {
                        Log.d(TAG, "开屏广告跳过")
                        jump2MainSplash()
                    }

                    override fun onAdShow(p0: View?, p1: Int) {
                        Log.d(TAG, "开屏广告显示")
                    }

                    override fun onAdTimeOver() {
                        Log.d(TAG, "开屏广告倒计时结束")
                        jump2MainSplash()
                    }

                })
            }

            override fun onTimeout() {
                mHasLoaded = true
                jump2MainSplash()
            }

            override fun onError(p0: Int, p1: String?) {
                mHasLoaded = true
                jump2MainSplash()
            }


        })
    }
*/

    override fun onResume() {
        if (mForceGoMain) {
            mHandler.removeCallbacksAndMessages(null)
            jump2MainSplash()
        }
        super.onResume()
    }

    private fun jump2MainSplash() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStop() {
        super.onStop()
        mForceGoMain = true
    }

    override fun handleMsg(msg: Message) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                jump2MainSplash()
            }
        }
    }

}
