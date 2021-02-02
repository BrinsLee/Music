package com.brins.bridgelib.picturedetail

import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.brins.bridgelib.BridgeInterface

/**
 * Created by lipeilin
 * on 2020/12/27
 */
interface PictureDetailBridgeInterface : BridgeInterface {

    fun toDetailPictureActivity(bundle: Bundle)

    fun getDetailPictureFragment(bundle: Bundle): Fragment
}