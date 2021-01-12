package com.brins.picturedetaillib.bridge

import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.brins.baselib.route.ARouterUtils
import com.brins.baselib.route.RouterHub
import com.brins.bridgelib.picturedetail.PictureDetailBridgeInterface
import com.brins.picturedetaillib.fragment.DetailPictureFragment

/**
 * Created by lipeilin
 * on 2020/12/27
 */
class PictureDetailBridge : PictureDetailBridgeInterface {
    override fun toDetailPictureActivity(
        bundle: Bundle
    ) {
        ARouterUtils.go(RouterHub.DETAILPICTUREACTIVITY, bundle)
    }

    override fun getDetailPictureFragment(bundle: Bundle): Fragment {
        return DetailPictureFragment.newInstance(bundle)
    }
}