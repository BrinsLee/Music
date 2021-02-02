package com.brins.bridgelib.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.brins.bridgelib.BridgeInterface

/**
 * Created by lipeilin
 * on 2020/12/31
 */
interface EventDetailBridgeInterface : BridgeInterface {

    fun toEventDetailActivity(bundle: Bundle)

    fun getEventCommentFragment(bundle: Bundle): Fragment
}